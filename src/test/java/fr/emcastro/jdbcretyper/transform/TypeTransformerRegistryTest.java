package fr.emcastro.jdbcretyper.transform;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

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
        var result = registry.toSql(new TestAppValue("hello"));
        assertEquals("SQL:hello", result);
    }

    @Test
    // Check that toSql() returns the original value unchanged when
    // no write transformer handles the value's type.
    void toSql_returnsOriginalWhenNoMatch() {
        var result = registry.toSql("unregistered");
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
        var result = registry.fromSql("SQL:hello", TestAppValue.class);
        assertEquals("SQL:hello", result.value());
    }

    @Test
    // Check that fromSql() returns the raw value directly when it is
    // already an instance of the requested type.
    void fromSql_returnsRawValueWhenAlreadyTargetType() {
        var result = registry.fromSql("raw", String.class);
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
        var result = registry.fromSqlDefaultType("SQL:hello");
        assertInstanceOf(TestAppValue.class, result);
        assertEquals("SQL:hello", ((TestAppValue) result).value());
    }

    @Test
    // Check that fromSqlDefaultType() returns the raw value as-is
    // when no read transformer handles the SQL value's type.
    void fromSqlDefaultType_returnsRawWhenNoMatch() {
        var result = registry.fromSqlDefaultType(42);
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
        var result = registry.mapType(TestAppValue.class);
        assertEquals(String.class, result);
    }

    @Test
    // Check that mapType() returns the original type unchanged when
    // the app type has no read transformer.
    void mapType_returnsOriginalWhenNoMatch() {
        var result = registry.mapType(Integer.class);
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

    @Test
    // Check that mapType() returns null when the registered read
    // transformer has supportsTypedGetObject() = false.
    void mapType_returnsNullWhenSupportsTypedGetObjectIsFalse() {
        registry.registerRead(new TestReadTransformerNoTypeHint());
        var result = registry.mapType(TestAppValueNoHint.class);
        assertNull(result);
    }

    @Test
    // Check that fromSqlMap() silently drops entries whose transformer
    // has supportsTypedGetObject() = false, returning a smaller map.
    void fromSqlMap_dropsEntriesWhenSupportsTypedGetObjectIsFalse() {
        registry.registerRead(new TestReadTransformerNoTypeHint());
        Map<String, Class<?>> map = new java.util.HashMap<>();
        map.put("col1", TestAppValue.class);
        map.put("col2", TestAppValueNoHint.class);
        var result = registry.fromSqlMap(map);
        assertEquals(1, result.size());
        assertEquals(String.class, result.get("col1"));
        assertNull(result.get("col2"));
    }

    @Test
    // Check that fromSql() throws TypeConversionException when the
    // matching transformer rejects the value via canTransform().
    void fromSql_skipsWhenCanTransformReturnsFalse() {
        registry.registerRead(new TestReadTransformerSelective(false));
        assertThrows(TypeConversionException.class, () -> registry.fromSql(42, AcceptedValue.class));
    }

    @Test
    // Check that fromSql() falls through to the next transformer when
    // the first matching transformer rejects via canTransform().
    void fromSql_fallsThroughWhenCanTransformReturnsFalse() {
        registry.registerRead(new TestReadTransformerSelective(false));
        registry.registerRead(new TestReadTransformerSelective(true));
        var result = registry.fromSql(42, AcceptedValue.class);
        assertEquals("accepted:42", result.value());
    }

    @Test
    // Check that fromSqlDefaultType() returns the raw SQL value when
    // the matching transformer rejects via canTransform().
    void fromSqlDefaultType_skipsWhenCanTransformReturnsFalse() {
        registry.registerRead(new TestReadTransformerSelective(false));
        var result = registry.fromSqlDefaultType(42);
        assertEquals(42, result);
    }

    @Test
    // Check that fromSqlDefaultType() falls through to the next
    // transformer when the first matching one rejects via canTransform().
    void fromSqlDefaultType_fallsThroughWhenCanTransformReturnsFalse() {
        registry.registerRead(new TestReadTransformerSelective(false));
        registry.registerRead(new TestReadTransformerSelective(true));
        Object result = registry.fromSqlDefaultType(42);
        assertInstanceOf(AcceptedValue.class, result);
        assertEquals("accepted:42", ((AcceptedValue) result).value());
    }

    /**
     * Test app type for selective read transformer.
     */
    record AcceptedValue(String value) {}

    /**
     * Test read transformer with configurable canTransform() for verifying
     * the fallback logic in fromSql() and fromSqlDefaultType().
     */
    static class TestReadTransformerSelective implements ReadTypeTransformer<AcceptedValue, Integer> {

        private final boolean accept;

        TestReadTransformerSelective(boolean accept) {
            this.accept = accept;
        }

        @Override
        public Class<AcceptedValue> getAppType() {
            return AcceptedValue.class;
        }

        @Override
        public Class<Integer> getReadSqlType() {
            return Integer.class;
        }

        @Override
        public boolean canTransform(Integer sqlValue) {
            return accept;
        }

        @Override
        public AcceptedValue fromSql(Integer sqlValue) {
            return new AcceptedValue("accepted:" + sqlValue);
        }
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

    /**
     * Test read transformer with supportsTypedGetObject() = false.
     */
    record TestAppValueNoHint(String value) {}

    static class TestReadTransformerNoTypeHint implements ReadTypeTransformer<TestAppValueNoHint, String> {

        @Override
        public Class<TestAppValueNoHint> getAppType() {
            return TestAppValueNoHint.class;
        }

        @Override
        public Class<String> getReadSqlType() {
            return String.class;
        }

        @Override
        public boolean supportsTypedGetObject() {
            return false;
        }

        @Override
        public TestAppValueNoHint fromSql(String sqlValue) {
            return new TestAppValueNoHint(sqlValue);
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
