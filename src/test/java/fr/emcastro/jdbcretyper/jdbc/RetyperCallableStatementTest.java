package fr.emcastro.jdbcretyper.jdbc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.emcastro.jdbcretyper.transform.TypeTransformerRegistry;

@ExtendWith(MockitoExtension.class)
class RetyperCallableStatementTest {

    @Mock
    private CallableStatement mockCallableStatement;

    @Mock
    private TypeTransformerRegistry registry;

    @Mock
    private RetyperConnection mockConnection;

    private RetyperCallableStatement statement;

    @BeforeEach
    void setUp() {
        statement = new RetyperCallableStatement(mockCallableStatement, registry, mockConnection);
    }

    @Test
    // Check that getObject(int) routes the raw driver value through
    // fromSqlDefaultType() for untyped reads.
    void getObject_int_usesFromSqlDefaultType() throws SQLException {
        when(mockCallableStatement.getObject(1)).thenReturn("raw-value");

        statement.getObject(1);

        verify(registry).fromSqlDefaultType("raw-value");
    }

    @Test
    // Check that getObject(String) routes the raw driver value through
    // fromSqlDefaultType() for untyped reads by parameter name.
    void getObject_string_usesFromSqlDefaultType() throws SQLException {
        when(mockCallableStatement.getObject("param")).thenReturn("raw-value");

        statement.getObject("param");

        verify(registry).fromSqlDefaultType("raw-value");
    }

    @Test
    // Check that getObject(int, Class) calls mapType() for the type hint
    // and fromSql() to convert the result.
    void getObject_intWithClass_delegatesWithMapType() throws SQLException {
        when(registry.mapType(String.class)).thenReturn(String.class);
        when(mockCallableStatement.getObject(1, String.class)).thenReturn("raw-value");
        when(registry.fromSql("raw-value", String.class)).thenReturn("raw-value");

        statement.getObject(1, String.class);

        verify(registry).mapType(String.class);
        verify(mockCallableStatement).getObject(1, String.class);
        verify(registry).fromSql("raw-value", String.class);
    }

    @Test
    // Check that getObject(int, Class) falls back to plain getObject(int)
    // when mapType() returns null for the requested type.
    void getObject_intWithClass_nullMapType_fallsBack() throws SQLException {
        when(registry.mapType(Integer.class)).thenReturn(null);
        when(mockCallableStatement.getObject(1)).thenReturn(42);
        when(registry.fromSql(42, Integer.class)).thenReturn(42);

        statement.getObject(1, Integer.class);

        verify(mockCallableStatement).getObject(1);
        verify(registry).fromSql(42, Integer.class);
    }

    @Test
    // Check that getObject(String, Class) follows the same mapType-then-
    // fromSql flow as the parameter-index overload.
    void getObject_stringWithClass_delegatesWithMapType() throws SQLException {
        when(registry.mapType(String.class)).thenReturn(String.class);
        when(mockCallableStatement.getObject("param", String.class)).thenReturn("raw-value");
        when(registry.fromSql("raw-value", String.class)).thenReturn("raw-value");

        statement.getObject("param", String.class);

        verify(registry).mapType(String.class);
        verify(mockCallableStatement).getObject("param", String.class);
        verify(registry).fromSql("raw-value", String.class);
    }

    @Test
    // Check that getObject(String, Class) falls back to plain getObject(String)
    // when mapType() returns null for the requested type.
    void getObject_stringWithClass_nullMapType_fallsBack() throws SQLException {
        when(registry.mapType(Integer.class)).thenReturn(null);
        when(mockCallableStatement.getObject("param")).thenReturn(42);
        when(registry.fromSql(42, Integer.class)).thenReturn(42);

        statement.getObject("param", Integer.class);

        verify(mockCallableStatement).getObject("param");
        verify(registry).fromSql(42, Integer.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    // Check that getObject(int, Map) transforms the type map through
    // fromSqlMap() before forwarding to the delegate.
    void getObject_intWithMap_usesFromSqlMap() throws SQLException {
        Map<String, Class<?>> inputMap = Map.of("col", String.class);
        Map<String, Class<?>> convertedMap = Map.of("col", String.class);
        when(registry.fromSqlMap(any())).thenReturn(convertedMap);
        when(mockCallableStatement.getObject(anyInt(), any(Map.class))).thenReturn("raw-value");

        statement.getObject(1, inputMap);

        verify(registry).fromSqlMap(inputMap);
        verify(mockCallableStatement).getObject(anyInt(), any(Map.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    // Check that getObject(String, Map) applies fromSqlMap() to the
    // type map, then delegates by parameter name.
    void getObject_stringWithMap_usesFromSqlMap() throws SQLException {
        Map<String, Class<?>> inputMap = Map.of("col", String.class);
        Map<String, Class<?>> convertedMap = Map.of("col", String.class);
        when(registry.fromSqlMap(any())).thenReturn(convertedMap);
        when(mockCallableStatement.getObject(anyString(), any(Map.class))).thenReturn("raw-value");

        statement.getObject("param", inputMap);

        verify(registry).fromSqlMap(inputMap);
        verify(mockCallableStatement).getObject(anyString(), any(Map.class));
    }

    @Test
    // Check that setObject(String, Object) converts the value via toSql()
    // before passing it to the delegate.
    void setObject_string_usesToSql() throws SQLException {
        Object value = new Object();
        when(registry.toSql(value)).thenReturn(value);

        statement.setObject("param", value);

        verify(registry).toSql(value);
        verify(mockCallableStatement).setObject("param", value);
    }

    @Test
    // Check that setObject(String, Object, int) converts the value via
    // toSql() before passing it to the delegate.
    void setObject_stringWithSqlType_usesToSql() throws SQLException {
        Object value = new Object();
        when(registry.toSql(value)).thenReturn(value);

        statement.setObject("param", value, 12);

        verify(registry).toSql(value);
        verify(mockCallableStatement).setObject("param", value, 12);
    }

    @Test
    // Check that setObject(String, Object, int, int) converts the value
    // via toSql() before passing it to the delegate.
    void setObject_stringWithSqlTypeAndScale_usesToSql() throws SQLException {
        Object value = new Object();
        when(registry.toSql(value)).thenReturn(value);

        statement.setObject("param", value, 12, 3);

        verify(registry).toSql(value);
        verify(mockCallableStatement).setObject("param", value, 12, 3);
    }
}
