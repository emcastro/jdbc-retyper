package fr.emcastro.jdbcretyper.transform;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.emcastro.jdbcretyper.exception.TypeConversionException;

class TypeTransformerRegistryTest {

    private TypeTransformerRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new TypeTransformerRegistry();
        registry.registerRead(new TestReadTransformer());
        registry.registerWrite(new TestWriteTransformer());
    }

    @Test
    // Check that toSql() locates the write transformer by app type
    // and applies the "SQL:" prefix.
    void toSql_findsMatchingTransformer() {
        Object result = registry.toSql(new TestAppValue("hello"));
        assertEquals("SQL:hello", result);
    }

    @Test
    // Check that toSql() returns the original value unchanged when
    // no write transformer handles the value's type.
    void toSql_returnsOriginalWhenNoMatch() {
        Object result = registry.toSql("unregistered");
        assertEquals("unregistered", result);
    }

    @Test
    // Check that toSql(null) short-circuits without a transformer lookup.
    void toSql_handlesNull() {
        assertNull(registry.toSql(null));
    }

    @Test
    // Check that fromSql() locates the read transformer by matching
    // both the target app type and the SQL value's type.
    void fromSql_findsMatchingTransformer() {
        TestAppValue result = registry.fromSql("SQL:hello", TestAppValue.class);
        assertEquals("SQL:hello", result.value());
    }

    @Test
    // Check that fromSql() returns the raw value directly when it is
    // already an instance of the requested type.
    void fromSql_returnsRawValueWhenAlreadyTargetType() {
        String result = registry.fromSql("raw", String.class);
        assertEquals("raw", result);
    }

    @Test
    // Check that fromSql() raises TypeConversionException when no
    // read transformer handles the value and target type.
    void fromSql_throwsOnUnsupportedConversion() {
        assertThrows(TypeConversionException.class, () -> registry.fromSql(42, TestAppValue.class));
    }

    @Test
    // Check that fromSql(null, type) returns null immediately.
    void fromSql_handlesNull() {
        assertNull(registry.fromSql(null, TestAppValue.class));
    }

    @Test
    // Check that fromSqlDefaultType() matches on the SQL value's
    // runtime type and applies the read transformer's conversion.
    void fromSqlDefaultType_transformsWhenMatch() {
        Object result = registry.fromSqlDefaultType("SQL:hello");
        assertInstanceOf(TestAppValue.class, result);
        assertEquals("SQL:hello", ((TestAppValue) result).value());
    }

    @Test
    // Check that fromSqlDefaultType() returns the raw value as-is
    // when no read transformer handles the SQL value's type.
    void fromSqlDefaultType_returnsRawWhenNoMatch() {
        Object result = registry.fromSqlDefaultType(42);
        assertEquals(42, result);
    }

    @Test
    // Check that fromSqlDefaultType(null) returns null.
    void fromSqlDefaultType_handlesNull() {
        assertNull(registry.fromSqlDefaultType(null));
    }

    @Test
    // Check that mapType() returns the read SQL type for a registered
    // app type whose supportsTypedGetObject() is true.
    void mapType_returnsSqlTypeForRegisteredAppType() {
        Class<?> result = registry.mapType(TestAppValue.class);
        assertEquals(String.class, result);
    }

    @Test
    // Check that mapType() returns the original type unchanged when
    // the app type has no read transformer.
    void mapType_returnsOriginalWhenNoMatch() {
        Class<?> result = registry.mapType(Integer.class);
        assertEquals(Integer.class, result);
    }

    @Test
    // Check that fromSqlMap() converts each entry through mapType(),
    // replacing registered types with their SQL types.
    void fromSqlMap_convertsAllEntries() {
        var map = java.util.Map.of("col1", TestAppValue.class, "col2", Integer.class);
        var result = registry.fromSqlMap(map);
        assertEquals(String.class, result.get("col1"));
        assertEquals(Integer.class, result.get("col2"));
    }

    @Test
    // Check that fromSqlMap(null) returns null.
    void fromSqlMap_handlesNull() {
        assertNull(registry.fromSqlMap(null));
    }

    /**
     * Test transformer: TestAppValue <-> String with "SQL:" prefix.
     */
    record TestAppValue(String value) {}

    static class TestReadTransformer implements ReadTypeTransformer<TestAppValue, String> {

        @Override
        public Class<TestAppValue> getAppType() {
            return TestAppValue.class;
        }

        @Override
        public Class<String> getReadSqlType() {
            return String.class;
        }

        @Override
        public TestAppValue fromSql(String sqlValue) {
            return new TestAppValue(sqlValue);
        }
    }

    static class TestWriteTransformer implements WriteTypeTransformer<TestAppValue, String> {

        @Override
        public Class<TestAppValue> getAppType() {
            return TestAppValue.class;
        }

        @Override
        public Class<String> getWriteSqlType() {
            return String.class;
        }

        @Override
        public String toSql(TestAppValue appValue) {
            return "SQL:" + appValue.value();
        }
    }
}
