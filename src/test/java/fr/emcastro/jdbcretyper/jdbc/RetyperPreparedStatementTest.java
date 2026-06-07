package fr.emcastro.jdbcretyper.jdbc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.emcastro.jdbcretyper.transform.TypeTransformerRegistry;

@ExtendWith(MockitoExtension.class)
class RetyperPreparedStatementTest {

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private TypeTransformerRegistry registry;

    @Mock
    private RetyperConnection mockConnection;

    private RetyperPreparedStatement statement;

    @BeforeEach
    void setUp() {
        statement = new RetyperPreparedStatement(mockPreparedStatement, registry, mockConnection);
    }

    @Test
    // Check that setObject(int, Object) converts the value via toSql()
    // before passing it to the delegate.
    void setObject_usesToSql() throws SQLException {
        var obj = new Object();
        when(registry.toSql(obj)).thenReturn(obj);

        statement.setObject(1, obj);

        verify(registry).toSql(obj);
        verify(mockPreparedStatement).setObject(1, obj);
    }

    @Test
    // Check that setObject(int, Object, int) converts the value via
    // toSql() before passing it to the delegate.
    void setObject_withSqlType_usesToSql() throws SQLException {
        var obj = new Object();
        when(registry.toSql(obj)).thenReturn(obj);

        statement.setObject(1, obj, 12);

        verify(registry).toSql(obj);
        verify(mockPreparedStatement).setObject(1, obj, 12);
    }

    @Test
    // Check that setObject(int, Object, int, int) converts the value
    // via toSql() before passing it to the delegate.
    void setObject_withSqlTypeAndScale_usesToSql() throws SQLException {
        var obj = new Object();
        when(registry.toSql(obj)).thenReturn(obj);

        statement.setObject(1, obj, 12, 3);

        verify(registry).toSql(obj);
        verify(mockPreparedStatement).setObject(1, obj, 12, 3);
    }

    @Test
    // Check that executeQuery() returns a RetyperResultSet carrying
    // this statement as the wrapping Statement reference.
    void executeQuery_returnsRetyperResultSetWithThisStatement() throws SQLException {
        var mockRs = mock(ResultSet.class);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockRs);

        var resultSet = statement.executeQuery();

        assertInstanceOf(RetyperResultSet.class, resultSet);
        assertSame(statement, resultSet.getStatement());
    }
}
