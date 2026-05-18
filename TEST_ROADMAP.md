# Test Roadmap

## Strategy

- All tests use **in-memory DuckDB** (`jdbc:duckdb:`) for real integration testing.
- **Mockito** is used for unit tests where a full database is unnecessary (registry logic, wrapper delegation).
- DuckDB **spatial extension** is loaded for geometry tests (`INSTALL spatial; LOAD spatial;`).
- DuckDB **JSON** is a core type (no extension needed). The JDBC driver returns `JsonNode` objects for JSON columns.

## DuckDB Type Mapping

| App Type | Column Type | Read SQL Type | Write SQL Type | Write Strategy | Read Strategy |
|---|---|---|---|---|---|
| `JsonBox` | `JSON` | `JsonNode` | `String` | `.value()` → `String` via `setObject` | `JsonNode.toString()` → `JsonBox` via `getObject(int)` (no type hint) |
| `Point` / `LineString` | `GEOMETRY` | `Blob` (WKB) | `byte[]` (WKB) | `WKBWriter.write()` → `ST_GeomFromWKB(?)` | `Blob.getBytes()` → `WKBReader.read()` via `getObject(int)` (no type hint, `supportsTypedGetObject() = false`) |

## Test Suites

### 1. `TypeTransformerRegistryTest` (Mockito unit tests)
- `toSql()` finds matching write transformer, falls back to original value
- `toSql()` returns original when no write transformer matches
- `fromSql()` finds matching read transformer, throws `TypeConversionException` on unsupported conversion
- `fromSql()` returns raw value when already target type
- `fromSqlDefaultType()` transforms when match, returns raw when no match
- `mapType()` returns read SQL type for registered app type (`supportsTypedGetObject() = true`)
- `mapType()` returns original type when no transformer registered
- `fromSqlMap()` converts all entries, drops entries where `supportsTypedGetObject() = false`
- Null handling for all methods

### 2. `JsonBoxDuckDBTest` (in-memory DuckDB)
- Create table with `JSON` column
- Insert `JsonBox` via `RetyperPreparedStatement.setObject()`
- Read back via `RetyperResultSet.getObject(columnIndex, JsonBox.class)`
- Read back via `RetyperResultSet.getObject(columnIndex)` (default type)
- Null `JsonBox` round-trip
- Nested JSON object round-trip

### 3. `GeometryDuckDBTest` (in-memory DuckDB with spatial extension)
- Load spatial extension
- Create table with `GEOMETRY` column
- Insert JTS `Point` via `RetyperPreparedStatement.setObject()` with WKB + `ST_GeomFromWKB(?)`
- Read back via `RetyperResultSet.getObject(columnIndex, Point.class)`
- Insert JTS `LineString` and read back
- Null geometry round-trip
- `registry.fromSql()` with WKB Blob → Geometry conversion

### 4. `RetyperResultSetTest` (Mockito unit tests)
- `getObject(int, Class)` delegates with `registry.mapType()` and `registry.fromSql()`
- `getObject(String, Class)` follows the same mapType-then-fromSql flow
- `getObject(int, Map)` delegates with `registry.fromSqlMap()`
- `updateObject()` delegates with `registry.toSql()`
- `getStatement()` returns the wrapping `RetyperStatement`

### 5. `RetyperConnectionTest` (Mockito unit tests)
- `createStatement()` returns `RetyperStatement`
- `prepareStatement()` returns `RetyperPreparedStatement`
- `prepareCall()` returns `RetyperCallableStatement`
- `unwrap()` / `isWrapperFor()` behavior

### 6. `RetyperDatasourceTest` (Mockito unit tests)
- `getConnection()` returns `RetyperConnection`
- `unwrap()` / `isWrapperFor()` behavior
