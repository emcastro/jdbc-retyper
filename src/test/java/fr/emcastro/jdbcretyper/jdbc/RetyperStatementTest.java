package fr.emcastro.jdbcretyper.jdbc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.emcastro.jdbcretyper.transform.TypeTransformerRegistry;

@ExtendWith(MockitoExtension.class)
class RetyperStatementTest {

    @Mock
    private Statement mockStatement;

    @Mock
    private TypeTransformerRegistry registry;

    @Mock
    private RetyperConnection mockConnection;

    private RetyperStatement statement;

    @BeforeEach
    void setUp() {
        statement = new RetyperStatement(mockStatement, registry, mockConnection);
    }

    @Test
    // Check that executeQuery(String) returns a RetyperResultSet carrying
    // this statement as the wrapping Statement reference.
    void executeQuery_returnsRetyperResultSetWithThisStatement() throws SQLException {
        ResultSet mockRs = mock(ResultSet.class);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockRs);

        ResultSet result = statement.executeQuery("SELECT 1");

        assertInstanceOf(RetyperResultSet.class, result);
        assertSame(statement, result.getStatement());
    }

    @Test
    // Check that getGeneratedKeys() returns a RetyperResultSet carrying
    // this statement as the wrapping Statement reference.
    void getGeneratedKeys_returnsRetyperResultSetWithThisStatement() throws SQLException {
        ResultSet mockRs = mock(ResultSet.class);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockRs);

        ResultSet result = statement.getGeneratedKeys();

        assertInstanceOf(RetyperResultSet.class, result);
        assertSame(statement, result.getStatement());
    }

    @Test
    // Check that getResultSet() returns a RetyperResultSet carrying
    // this statement as the wrapping Statement reference.
    void getResultSet_returnsRetyperResultSetWithThisStatement() throws SQLException {
        ResultSet mockRs = mock(ResultSet.class);
        when(mockStatement.getResultSet()).thenReturn(mockRs);

        ResultSet result = statement.getResultSet();

        assertInstanceOf(RetyperResultSet.class, result);
        assertSame(statement, result.getStatement());
    }

    @Test
    // Check that getConnection() returns the wrapping RetyperConnection
    // passed to the constructor, not the underlying driver's Connection.
    void getConnection_returnsWrapper() throws SQLException {
        assertSame(mockConnection, statement.getConnection());
    }
}
