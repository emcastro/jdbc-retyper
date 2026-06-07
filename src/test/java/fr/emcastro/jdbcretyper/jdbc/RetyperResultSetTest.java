package fr.emcastro.jdbcretyper.jdbc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Statement;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.emcastro.jdbcretyper.transform.TypeTransformerRegistry;

@ExtendWith(MockitoExtension.class)
class RetyperResultSetTest {

    @Mock
    private ResultSet mockResultSet;

    private TypeTransformerRegistry registry;
    private RetyperResultSet retyperResultSet;

    @BeforeEach
    void setUp() {
        registry = spy(new TypeTransformerRegistry());
        retyperResultSet = new RetyperResultSet(mockResultSet, registry, null);
    }

    @Test
    // Check that getObject(int, Class) calls mapType() for the type hint
    // and fromSql() to convert the result.
    void getObject_withClass_delegatesWithMapType() throws SQLException {
        when(registry.mapType(String.class)).thenReturn(String.class);
        when(mockResultSet.getObject(1, String.class)).thenReturn("raw-value");
        when(registry.fromSql("raw-value", String.class)).thenReturn("raw-value");

        retyperResultSet.getObject(1, String.class);

        verify(registry).mapType(String.class);
        verify(mockResultSet).getObject(1, String.class);
        verify(registry).fromSql("raw-value", String.class);
    }

    @Test
    // Check that getObject(String, Class) calls mapType() for the type hint
    // and fromSql() to convert the result when mapType returns a non-null value.
    void getObject_withClass_byLabel_delegatesWithMapType() throws SQLException {
        when(registry.mapType(String.class)).thenReturn(String.class);
        when(mockResultSet.getObject("col", String.class)).thenReturn("raw-value");
        when(registry.fromSql("raw-value", String.class)).thenReturn("raw-value");

        retyperResultSet.getObject("col", String.class);

        verify(registry).mapType(String.class);
        verify(mockResultSet).getObject("col", String.class);
        verify(registry).fromSql("raw-value", String.class);
    }

    @Test
    // Check that getObject(int, Class) falls back to plain getObject(int)
    // when mapType() returns null for the requested type.
    void getObject_withClass_nullMapType_fallsBack() throws SQLException {
        when(registry.mapType(Integer.class)).thenReturn(null);
        when(mockResultSet.getObject(1)).thenReturn(42);
        when(registry.fromSql(42, Integer.class)).thenReturn(42);

        retyperResultSet.getObject(1, Integer.class);

        verify(mockResultSet).getObject(1);
        verify(registry).fromSql(42, Integer.class);
    }

    @Test
    // Check that getObject(String, Class) falls back to plain getObject(String)
    // when mapType() returns null for the requested type.
    void getObject_withClass_byLabel_nullMapType_fallsBack() throws SQLException {
        when(registry.mapType(Integer.class)).thenReturn(null);
        when(mockResultSet.getObject("col")).thenReturn(42);
        when(registry.fromSql(42, Integer.class)).thenReturn(42);

        retyperResultSet.getObject("col", Integer.class);

        verify(mockResultSet).getObject("col");
        verify(registry).fromSql(42, Integer.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    // Check that getObject(int, Map) transforms the type map through
    // fromSqlMap() before forwarding to the delegate.
    void getObject_withMap_delegatesWithFromSqlMap() throws SQLException {
        Map<String, Class<?>> inputMap = Map.of("col", String.class);
        Map<String, Class<?>> convertedMap = Map.of("col", String.class);
        when(registry.fromSqlMap(any())).thenReturn(convertedMap);
        when(mockResultSet.getObject(anyInt(), any(Map.class))).thenReturn("raw-value");

        retyperResultSet.getObject(1, inputMap);

        verify(registry).fromSqlMap(inputMap);
        verify(mockResultSet).getObject(anyInt(), any(Map.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    // Check that getObject(String, Map) applies fromSqlMap() to the
    // type map, then delegates by column label.
    void getObject_withMap_byLabel_delegatesWithFromSqlMap() throws SQLException {
        Map<String, Class<?>> inputMap = Map.of("col", String.class);
        Map<String, Class<?>> convertedMap = Map.of("col", String.class);
        when(registry.fromSqlMap(any())).thenReturn(convertedMap);
        when(mockResultSet.getObject(anyString(), any(Map.class))).thenReturn("raw-value");

        retyperResultSet.getObject("col", inputMap);

        verify(registry).fromSqlMap(inputMap);
        verify(mockResultSet).getObject(anyString(), any(Map.class));
    }

    @Test
    // Check that plain getObject(int) routes the raw driver value
    // through fromSqlDefaultType() for untyped reads.
    void getObject_defaultType_usesFromSqlDefaultType() throws SQLException {
        when(mockResultSet.getObject(1)).thenReturn("raw-value");

        retyperResultSet.getObject(1);

        verify(registry).fromSqlDefaultType("raw-value");
    }

    @Test
    // Check that updateObject(int, Object) converts the value via
    // toSql() before passing it to the delegate.
    void updateObject_usesToSql() throws SQLException {
        var obj = new Object();
        when(registry.toSql(obj)).thenReturn(obj);

        retyperResultSet.updateObject(1, obj);

        verify(registry).toSql(obj);
    }

    @Test
    // Check that updateObject(int, Object, int) with a scale parameter
    // also converts the value via toSql() first.
    void updateObject_withScale_usesToSql() throws SQLException {
        var obj = new Object();
        when(registry.toSql(obj)).thenReturn(obj);

        retyperResultSet.updateObject(1, obj, 2);

        verify(registry).toSql(obj);
    }

    // --- Unwrap / isWrapperFor ---

    @Test
    // Check that unwrap(ResultSet.class) returns the underlying driver
    // ResultSet when the requested type matches the delegate.
    void unwrap_returnsWrappedIfInstance() throws SQLException {
        ResultSet resultSet = retyperResultSet.unwrap(ResultSet.class);

        assertSame(mockResultSet, resultSet);
    }

    @Test
    // Check that isWrapperFor(ResultSet.class) returns true because
    // the wrapped ResultSet implements that interface.
    void isWrapperFor_trueForWrappedInstance() throws SQLException {
        assertTrue(retyperResultSet.isWrapperFor(ResultSet.class));
    }

    @Test
    // Check that next() delegates to the underlying ResultSet with
    // no registry involvement.
    void next_delegatesToWrapped() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);

        assertTrue(retyperResultSet.next());
    }

    @Test
    // Check that wasNull() delegates directly to the driver's ResultSet
    // without additional transformation.
    void wasNull_delegatesToWrapped() throws SQLException {
        when(mockResultSet.wasNull()).thenReturn(true);

        assertTrue(retyperResultSet.wasNull());
    }

    @Test
    // Check that plain getObject(String) routes the raw driver value
    // through fromSqlDefaultType() for untyped reads by label.
    void getObject_defaultType_byLabel_usesFromSqlDefaultType() throws SQLException {
        when(mockResultSet.getObject("col")).thenReturn("raw-value");

        retyperResultSet.getObject("col");

        verify(registry).fromSqlDefaultType("raw-value");
    }

    @Test
    // Check that updateObject(String, Object) converts the value via
    // toSql() before passing it to the delegate.
    void updateObject_byLabel_usesToSql() throws SQLException {
        var obj = new Object();
        when(registry.toSql(obj)).thenReturn(obj);

        retyperResultSet.updateObject("col", obj);

        verify(registry).toSql(obj);
    }

    @Test
    // Check that updateObject(int, Object, SQLType) converts the value
    // via toSql() before passing it to the delegate.
    void updateObject_withSqlType_usesToSql() throws SQLException {
        var obj = new Object();
        when(registry.toSql(obj)).thenReturn(obj);
        var sqlType = mock(SQLType.class);

        retyperResultSet.updateObject(1, obj, sqlType);

        verify(registry).toSql(obj);
    }

    @Test
    // Check that updateObject(String, Object, SQLType, int) converts
    // the value via toSql() before passing it to the delegate.
    void updateObject_byLabel_withSqlTypeAndScale_usesToSql() throws SQLException {
        var obj = new Object();
        when(registry.toSql(obj)).thenReturn(obj);
        var sqlType = mock(SQLType.class);

        retyperResultSet.updateObject("col", obj, sqlType, 2);

        verify(registry).toSql(obj);
    }

    @Test
    // Check that updateObject(String, Object, int) converts the value
    // via toSql() before passing it to the delegate.
    void updateObject_byLabel_withScale_usesToSql() throws SQLException {
        var obj = new Object();
        when(registry.toSql(obj)).thenReturn(obj);

        retyperResultSet.updateObject("col", obj, 2);

        verify(registry).toSql(obj);
    }

    @Test
    // Check that updateObject(String, Object, SQLType) converts the
    // value via toSql() before passing it to the delegate.
    void updateObject_byLabel_withSqlType_usesToSql() throws SQLException {
        var obj = new Object();
        when(registry.toSql(obj)).thenReturn(obj);
        var sqlType = mock(SQLType.class);

        retyperResultSet.updateObject("col", obj, sqlType);

        verify(registry).toSql(obj);
    }

    @Test
    // Check that getStatement() returns the wrapping Statement passed
    // to the constructor, not the underlying driver's Statement.
    void getStatement_returnsWrapper() throws SQLException {
        var mockStatement = mock(Statement.class);
        try (var rsWithStatement = new RetyperResultSet(mockResultSet, registry, mockStatement)) {
            assertSame(mockStatement, rsWithStatement.getStatement());
        }
    }

    @Test
    // Check that unwrap(Class) delegates to the underlying ResultSet
    // when it doesn't implement the requested type.
    void unwrap_delegatesWhenNotInstance() throws SQLException {
        var dbMetaData = mock(DatabaseMetaData.class);
        when(mockResultSet.unwrap(DatabaseMetaData.class)).thenReturn(dbMetaData);
        assertSame(dbMetaData, retyperResultSet.unwrap(DatabaseMetaData.class));
        verify(mockResultSet).unwrap(DatabaseMetaData.class);
    }

    @Test
    // Check that isWrapperFor(Class) delegates to the underlying
    // ResultSet when it doesn't implement the requested type.
    void isWrapperFor_delegatesWhenNotInstance() throws SQLException {
        when(mockResultSet.isWrapperFor(DatabaseMetaData.class)).thenReturn(true);
        assertTrue(retyperResultSet.isWrapperFor(DatabaseMetaData.class));
        verify(mockResultSet).isWrapperFor(DatabaseMetaData.class);
    }

    @Test
    // Check that isWrapperFor(Class) returns false when neither the
    // ResultSet nor the underlying delegate implements the type.
    void isWrapperFor_returnsFalseWhenNeitherImplements() throws SQLException {
        when(mockResultSet.isWrapperFor(DatabaseMetaData.class)).thenReturn(false);
        assertFalse(retyperResultSet.isWrapperFor(DatabaseMetaData.class));
        verify(mockResultSet).isWrapperFor(DatabaseMetaData.class);
    }
}
