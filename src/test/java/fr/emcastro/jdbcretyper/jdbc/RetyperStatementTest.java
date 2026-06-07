package fr.emcastro.jdbcretyper.jdbc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.DatabaseMetaData;
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
        var mockRs = mock(ResultSet.class);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockRs);

        var resultSet = statement.executeQuery("SELECT 1");

        assertInstanceOf(RetyperResultSet.class, resultSet);
        assertSame(statement, resultSet.getStatement());
    }

    @Test
    // Check that getGeneratedKeys() returns a RetyperResultSet carrying
    // this statement as the wrapping Statement reference.
    void getGeneratedKeys_returnsRetyperResultSetWithThisStatement() throws SQLException {
        var mockRs = mock(ResultSet.class);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockRs);

        var resultSet = statement.getGeneratedKeys();

        assertInstanceOf(RetyperResultSet.class, resultSet);
        assertSame(statement, resultSet.getStatement());
    }

    @Test
    // Check that getResultSet() returns a RetyperResultSet carrying
    // this statement as the wrapping Statement reference.
    void getResultSet_returnsRetyperResultSetWithThisStatement() throws SQLException {
        var mockRs = mock(ResultSet.class);
        when(mockStatement.getResultSet()).thenReturn(mockRs);

        var resultSet = statement.getResultSet();

        assertInstanceOf(RetyperResultSet.class, resultSet);
        assertSame(statement, resultSet.getStatement());
    }

    @Test
    // Check that getConnection() returns the wrapping RetyperConnection
    // passed to the constructor, not the underlying driver's Connection.
    void getConnection_returnsWrapper() throws SQLException {
        assertSame(mockConnection, statement.getConnection());
    }

    // --- Unwrap / isWrapperFor ---

    @Test
    // Check that unwrap(Class) returns the underlying Statement when
    // it implements the requested type.
    void unwrap_returnsWrappedIfInstance() throws SQLException {
        assertSame(mockStatement, statement.unwrap(Statement.class));
    }

    @Test
    // Check that unwrap(Class) delegates to the underlying Statement
    // when it doesn't implement the requested type.
    void unwrap_delegatesWhenNotInstance() throws SQLException {
        var dbMetaData = mock(DatabaseMetaData.class);
        when(mockStatement.unwrap(DatabaseMetaData.class)).thenReturn(dbMetaData);
        assertSame(dbMetaData, statement.unwrap(DatabaseMetaData.class));
        verify(mockStatement).unwrap(DatabaseMetaData.class);
    }

    @Test
    // Check that isWrapperFor(Class) returns true when the underlying
    // Statement implements the requested type.
    void isWrapperFor_trueForWrappedInstance() throws SQLException {
        assertTrue(statement.isWrapperFor(Statement.class));
    }

    @Test
    // Check that isWrapperFor(Class) delegates to the underlying
    // Statement when it doesn't implement the requested type.
    void isWrapperFor_delegatesWhenNotInstance() throws SQLException {
        when(mockStatement.isWrapperFor(DatabaseMetaData.class)).thenReturn(true);
        assertTrue(statement.isWrapperFor(DatabaseMetaData.class));
        verify(mockStatement).isWrapperFor(DatabaseMetaData.class);
    }

    @Test
    // Check that isWrapperFor(Class) returns false when neither the
    // Statement nor the underlying delegate implements the type.
    void isWrapperFor_returnsFalseWhenNeitherImplements() throws SQLException {
        when(mockStatement.isWrapperFor(DatabaseMetaData.class)).thenReturn(false);
        assertFalse(statement.isWrapperFor(DatabaseMetaData.class));
        verify(mockStatement).isWrapperFor(DatabaseMetaData.class);
    }
}
