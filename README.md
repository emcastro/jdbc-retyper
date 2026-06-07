# JDBC ReTyper

A lightweight JDBC facade that transparently converts custom Java types to and from JDBC drivers, with zero extra dependencies.

## Case 1 — Plain JDBC (using DuckDB as an example)

```java
// Sample custom type
record JsonBox(String value) {}

// Read-Transformer: SQL type (DuckDB JsonNode) → application type (JsonBox)
class JsonBoxReadTransformer implements ReadTypeTransformer<JsonBox, JsonNode> {
    public Class<JsonBox> getAppType() { return JsonBox.class; }
    public Class<JsonNode> getReadSqlType() { return JsonNode.class; }
    public boolean supportsTypedGetObject() { return false; }
    public JsonBox fromSql(JsonNode v) { return new JsonBox(v.toString()); }
}

// Write-Transformer: application type (JsonBox) → SQL type (String). Note: DuckDB is asymmetric on types
class JsonBoxWriteTransformer implements WriteTypeTransformer<JsonBox, String> {
    public Class<JsonBox> getAppType() { return JsonBox.class; }
    public Class<String> getWriteSqlType() { return String.class; }
    public String toSql(JsonBox v) { return v.value(); }
}

// Setup
var registry = new TypeTransformerRegistry();
registry.registerRead(new JsonBoxReadTransformer());
registry.registerWrite(new JsonBoxWriteTransformer());

try (var conn = new RetyperConnection(DriverManager.getConnection("jdbc:duckdb:"), registry)) {
    // Writing use
    try (var ps = conn.prepareStatement("INSERT INTO t VALUES (?)")) {
        ps.setObject(1, new JsonBox("{\"a\":1}"));  // → String
        ps.execute();
    }
    // Reading use
    try (var stmt = conn.createStatement();
         var rs = stmt.executeQuery("SELECT data FROM t")) {
        rs.next();
        JsonBox result = rs.getObject(1, JsonBox.class);  // ← JsonNode → JsonBox
    }
}
```

> **Demo sources:** [`JsonBoxReadTransformer`](src/test/java/fr/emcastro/jdbcretyper/transform/JsonBoxReadTransformer.java),
> [`JsonBoxWriteTransformer`](src/test/java/fr/emcastro/jdbcretyper/transform/JsonBoxWriteTransformer.java),
> [`JsonBoxDuckDBTest`](src/test/java/fr/emcastro/jdbcretyper/JsonBoxDuckDBTest.java)

## Case 2 — Spring `JdbcClient`

```java
@Configuration
class JdbcConfig {
    @Bean
    JdbcClient jdbcClient(DataSource dataSource) { // Injected by Spring
        var registry = new TypeTransformerRegistry();
        registry.registerRead(new JsonBoxReadTransformer());
        registry.registerWrite(new JsonBoxWriteTransformer());
        return JdbcClient.create(new RetyperDatasource(dataSource, registry));
    }
}

@Repository
class ExampleRepository {
    private final JdbcClient jdbcClient;
    ExampleRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
    JsonBox extractA(JsonBox json) {
        return jdbcClient.sql("SELECT (? -> 'a') AS a")
            .param(1, json)                    // → String
            .query().singleValue();            // ← JsonBox
    }
}
```

> **Demo sources:** [`SpringJdbcClientApplication`](demo/src/main/java/fr/emcastro/jdbcretyper/demo/spring/SpringJdbcClientApplication.java),
> [`JdbcConfig`](demo/src/main/java/fr/emcastro/jdbcretyper/demo/spring/config/JdbcConfig.java),
> [`ExampleRepository`](demo/src/main/java/fr/emcastro/jdbcretyper/demo/spring/repository/ExampleRepository.java),
> [`JsonBoxReadTransformer`](demo/src/main/java/fr/emcastro/jdbcretyper/demo/spring/transform/JsonBoxReadTransformer.java),
> [`JsonBoxWriteTransformer`](demo/src/main/java/fr/emcastro/jdbcretyper/demo/spring/transform/JsonBoxWriteTransformer.java)

## Case 3 — Spatial Geometry with PostGIS

```java
class GeometryReadTransformer implements ReadTypeTransformer<Geometry, PGobject> {
    private final WKBReader reader = new WKBReader();
    public Class<Geometry> getAppType() { return Geometry.class; }
    public Class<PGobject> getReadSqlType() { return PGobject.class; }
    public boolean canTransform(PGobject v) { return "geometry".equals(v.getType()); }
    public Geometry fromSql(PGobject v) {
        try { return reader.read(WKBReader.hexToBytes(v.getValue()));
        } catch (ParseException e) {
            throw new TypeConversionException("Unable to convert PGobject to Geometry", e);
        }
    }
}
class GeometryWriteTransformer implements WriteTypeTransformer<Geometry, PGobject> {
    private final WKBWriter writer = new WKBWriter();
    public Class<Geometry> getAppType() { return Geometry.class; }
    public Class<PGobject> getWriteSqlType() { return PGobject.class; }
    public PGobject toSql(Geometry v) {
        try {
            PGobject pg = new PGobject();
            pg.setType("geometry");
            pg.setValue(WKBWriter.toHex(writer.write(v)));
            return pg;
        } catch (SQLException e) {
            throw new TypeConversionException("Unable to convert Geometry to PGobject", e);
        }
    }
}

var registry = new TypeTransformerRegistry();
registry.registerRead(new GeometryReadTransformer());
registry.registerWrite(new GeometryWriteTransformer());

try (var conn = new RetyperConnection(DriverManager.getConnection(url, user, password), registry)) {
    var point = new WKTReader().read("POINT (1 2)");
    try (var ps = conn.prepareStatement("SELECT ST_SnapToGrid(ST_Buffer(?, 1, 1), 1e-10)")) {
        ps.setObject(1, point);
        try (var rs = ps.executeQuery()) {
            rs.next();
            Geometry result = (Geometry) rs.getObject(1);
        }
    }
}
```

> **Demo sources:** [`PostgisApplication`](demo/src/main/java/fr/emcastro/jdbcretyper/demo/postgis/PostgisApplication.java),
> [`GeometryReadTransformer`](demo/src/main/java/fr/emcastro/jdbcretyper/demo/postgis/transform/GeometryReadTransformer.java),
> [`GeometryPostgisWriteTransformer`](demo/src/main/java/fr/emcastro/jdbcretyper/demo/postgis/transform/GeometryPostgisWriteTransformer.java),
> [`JsonBoxReadTransformer`](demo/src/main/java/fr/emcastro/jdbcretyper/demo/postgis/transform/JsonBoxReadTransformer.java),
> [`JsonBoxPostgresWriteTransformer`](demo/src/main/java/fr/emcastro/jdbcretyper/demo/postgis/transform/JsonBoxPostgresWriteTransformer.java)

## How it works

`RetyperDatasource` chains JDBC decorators (`RetyperConnection` → `RetyperPreparedStatement` / `RetyperResultSet`). Every `setObject()` or `getObject()` is intercepted and routed to `TypeTransformerRegistry`, which holds two independent transformer lists:

- **Write** (`WriteTypeTransformer`) — each transformer is tried in registration order; the first whose `getAppType()` matches the value's runtime type (via `instanceof`) is used. If none matches, the value is passed through unchanged.
- **Read** (`ReadTypeTransformer`) — each transformer is tried in registration order; the first whose pair (`getReadSqlType()`, `getAppType()`) is compatible with the SQL value's runtime type and the requested target type is used. If none matches and the value is already of the target type, it is returned as-is; otherwise a `TypeConversionException` is thrown.

This means the registration order matters when multiple transformers could handle the same type. Register the most specific one first.

Transformer implementations should throw `TypeConversionException` from `fromSql()` and `toSql()` to report conversion failures (parsing errors, invalid data, serialization failures, etc.).

Once the registry is passed to a `RetyperDatasource` or `RetyperConnection`, it must not be modified further — the lists are not thread-safe and are _not_ copied defensively. Configure the registry completely before creating any Retyper instance.

The library (`fr.emcastro:jdbcretyper`) has **zero Spring dependencies**. `JdbcClient` integration is done by simply wrapping the `DataSource`.

## Maven

```xml
<dependency>
    <groupId>fr.emcastro</groupId>
    <artifactId>jdbcretyper</artifactId>
    <version>1.0.0</version>
</dependency>
```

## License

MIT
