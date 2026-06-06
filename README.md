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

## Case 3 — Spatial Geometry (PostGIS / DuckDB spatial)

```java
class GeometryReadTransformer implements ReadTypeTransformer<Geometry, Blob> {
    private final GeometryFactory gf = new GeometryFactory();
    public Class<Geometry> getAppType() { return Geometry.class; }
    public Class<Blob> getReadSqlType() { return Blob.class; }
    public boolean supportsTypedGetObject() { return false; }
    public Geometry fromSql(Blob v) {
        try { return new WKBReader(gf).read(v.getBytes(1, (int) v.length()));
        } catch (SQLException | ParseException e) {
            throw new TypeConversionException("WKB error", e);
        } finally { try { v.free(); } catch (SQLException ignored) {} }
    }
}
class GeometryWriteTransformer implements WriteTypeTransformer<Geometry, byte[]> {
    public Class<Geometry> getAppType() { return Geometry.class; }
    public Class<byte[]> getWriteSqlType() { return byte[].class; }
    public byte[] toSql(Geometry v) { return new WKBWriter().write(v); }
}

var registry = new TypeTransformerRegistry();
registry.registerRead(new GeometryReadTransformer());
registry.registerWrite(new GeometryWriteTransformer());

try (var conn = new RetyperConnection(DriverManager.getConnection("jdbc:duckdb:"), registry)) {
    try (var ps = conn.prepareStatement("INSERT INTO t VALUES (ST_GeomFromWKB(?))")) {
        ps.setObject(1, gf.createPoint(new Coordinate(30.0, 10.0)));
        ps.execute();
    }
    try (var stmt = conn.createStatement();
         var rs = stmt.executeQuery("SELECT geom FROM t")) {
        rs.next();
        Point result = rs.getObject(1, Point.class);
    }
}
```

## How it works

`RetyperDatasource` chains JDBC decorators (`RetyperConnection` → `RetyperPreparedStatement` / `RetyperResultSet`). Every `setObject()` or `getObject()` is intercepted and routed to `TypeTransformerRegistry`, which holds two independent transformer lists:

- **Write** (`WriteTypeTransformer`) — each transformer is tried in registration order; the first whose `getAppType()` matches the value's runtime type (via `instanceof`) is used. If none matches, the value is passed through unchanged.
- **Read** (`ReadTypeTransformer`) — each transformer is tried in registration order; the first whose pair (`getReadSqlType()`, `getAppType()`) is compatible with the SQL value's runtime type and the requested target type is used. If none matches and the value is already of the target type, it is returned as-is; otherwise a `TypeConversionException` is thrown.

This means the registration order matters when multiple transformers could handle the same type. Register the most specific one first.

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
