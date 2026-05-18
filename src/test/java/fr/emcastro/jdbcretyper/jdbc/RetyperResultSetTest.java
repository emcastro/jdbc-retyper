package fr.emcastro.jdbcretyper.jdbc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;
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
        when(mockResultSet.getObject(1, String.class)).thenReturn("raw-value");

        retyperResultSet.getObject(1, String.class);

        verify(registry).mapType(String.class);
        verify(registry).fromSql("raw-value", String.class);
    }

    @Test
    // Check that getObject(String, Class) follows the same
    // mapType-then-fromSql flow as the column-index overload.
    void getObject_withClass_byLabel_delegatesWithMapType() throws SQLException {
        when(mockResultSet.getObject("col", String.class)).thenReturn("raw-value");

        retyperResultSet.getObject("col", String.class);

        verify(registry).mapType(String.class);
        verify(registry).fromSql("raw-value", String.class);
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
        Object value = new Object();
        when(registry.toSql(value)).thenReturn(value);

        retyperResultSet.updateObject(1, value);

        verify(registry).toSql(value);
    }

    @Test
    // Check that updateObject(int, Object, int) with a scale parameter
    // also converts the value via toSql() first.
    void updateObject_withScale_usesToSql() throws SQLException {
        Object value = new Object();
        when(registry.toSql(value)).thenReturn(value);

        retyperResultSet.updateObject(1, value, 2);

        verify(registry).toSql(value);
    }

    @Test
    // Check that unwrap(ResultSet.class) returns the underlying driver
    // ResultSet when the requested type matches the delegate.
    void unwrap_returnsWrappedIfInstance() throws SQLException {
        ResultSet result = retyperResultSet.unwrap(ResultSet.class);

        assertSame(mockResultSet, result);
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
}
