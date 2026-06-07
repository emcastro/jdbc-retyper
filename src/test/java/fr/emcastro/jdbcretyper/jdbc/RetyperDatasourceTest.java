package fr.emcastro.jdbcretyper.jdbc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.emcastro.jdbcretyper.transform.TypeTransformerRegistry;

@ExtendWith(MockitoExtension.class)
class RetyperDatasourceTest {

    @Mock
    private DataSource mockDataSource;

    @Mock
    private TypeTransformerRegistry registry;

    @Mock
    private Connection mockConnection;

    private RetyperDatasource datasource;

    @BeforeEach
    void setUp() {
        datasource = new RetyperDatasource(mockDataSource, registry);
    }

    @Test
    // Check that getConnection() wraps the driver's Connection in a
    // RetyperConnection sharing the TypeTransformerRegistry.
    void getConnection_returnsRetyperConnection() throws SQLException {
        when(mockDataSource.getConnection()).thenReturn(mockConnection);

        assertInstanceOf(RetyperConnection.class, datasource.getConnection());
    }

    @Test
    // Check that getConnection(user, pass) also wraps with
    // RetyperConnection, preserving credential-based authentication.
    void getConnection_withCredentials_returnsRetyperConnection() throws SQLException {
        when(mockDataSource.getConnection(anyString(), anyString())).thenReturn(mockConnection);

        assertInstanceOf(RetyperConnection.class, datasource.getConnection("user", "pass"));
    }

    // --- Unwrap / isWrapperFor ---

    @Test
    // Check that unwrap(DataSource.class) returns the underlying driver
    // DataSource when the requested type matches the delegate.
    void unwrap_returnsWrappedIfInstance() throws SQLException {
        assertSame(mockDataSource, datasource.unwrap(DataSource.class));
    }

    @Test
    // Check that isWrapperFor(DataSource.class) is true because the
    // wrapped DataSource implements that interface.
    void isWrapperFor_trueForWrappedInstance() throws SQLException {
        assertTrue(datasource.isWrapperFor(DataSource.class));
    }

    @Test
    // Check that unwrap(Class) delegates to the underlying DataSource
    // when it doesn't implement the requested type.
    void unwrap_delegatesWhenNotInstance() throws SQLException {
        DatabaseMetaData dbMetaData = mock(DatabaseMetaData.class);
        when(mockDataSource.unwrap(DatabaseMetaData.class)).thenReturn(dbMetaData);
        assertSame(dbMetaData, datasource.unwrap(DatabaseMetaData.class));
        verify(mockDataSource).unwrap(DatabaseMetaData.class);
    }

    @Test
    // Check that isWrapperFor(Class) delegates to the underlying
    // DataSource when it doesn't implement the requested type.
    void isWrapperFor_delegatesWhenNotInstance() throws SQLException {
        when(mockDataSource.isWrapperFor(DatabaseMetaData.class)).thenReturn(true);
        assertTrue(datasource.isWrapperFor(DatabaseMetaData.class));
        verify(mockDataSource).isWrapperFor(DatabaseMetaData.class);
    }

    @Test
    // Check that isWrapperFor(Class) returns false when neither the
    // DataSource nor the underlying delegate implements the type.
    void isWrapperFor_returnsFalseWhenNeitherImplements() throws SQLException {
        when(mockDataSource.isWrapperFor(DatabaseMetaData.class)).thenReturn(false);
        assertFalse(datasource.isWrapperFor(DatabaseMetaData.class));
        verify(mockDataSource).isWrapperFor(DatabaseMetaData.class);
    }
}
