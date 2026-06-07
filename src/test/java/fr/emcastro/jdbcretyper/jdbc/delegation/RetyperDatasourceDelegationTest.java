package fr.emcastro.jdbcretyper.jdbc.delegation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.emcastro.jdbcretyper.jdbc.RetyperDatasource;
import fr.emcastro.jdbcretyper.transform.TypeTransformerRegistry;

@ExtendWith(MockitoExtension.class)
class RetyperDatasourceDelegationTest {

    @Mock
    private DataSource mockDataSource;

    @Mock
    private TypeTransformerRegistry registry;

    private RetyperDatasource datasource;

    @BeforeEach
    void setUp() {
        datasource = new RetyperDatasource(mockDataSource, registry);
    }

    @Test
    // Check that getLogWriter() delegates to the underlying DataSource
    // without additional transformation.
    void getLogWriter_delegates() throws SQLException {
        PrintWriter logWriter = new PrintWriter(System.out);
        when(mockDataSource.getLogWriter()).thenReturn(logWriter);

        assertSame(logWriter, datasource.getLogWriter());
        verify(mockDataSource).getLogWriter();
    }

    @Test
    // Check that setLogWriter() delegates to the underlying DataSource
    // without additional transformation.
    void setLogWriter_delegates() throws SQLException {
        PrintWriter pw = new PrintWriter(System.out);
        datasource.setLogWriter(pw);

        verify(mockDataSource).setLogWriter(pw);
    }

    @Test
    // Check that getLoginTimeout() delegates to the underlying DataSource
    // without additional transformation.
    void getLoginTimeout_delegates() throws SQLException {
        when(mockDataSource.getLoginTimeout()).thenReturn(30);

        assertEquals(30, datasource.getLoginTimeout());
        verify(mockDataSource).getLoginTimeout();
    }

    @Test
    // Check that setLoginTimeout() delegates to the underlying DataSource
    // without additional transformation.
    void setLoginTimeout_delegates() throws SQLException {
        datasource.setLoginTimeout(45);

        verify(mockDataSource).setLoginTimeout(45);
    }

    @Test
    // Check that getParentLogger() delegates to the underlying DataSource
    // without additional transformation.
    void getParentLogger_delegates() throws SQLException {
        Logger parentLogger = Logger.getGlobal();
        when(mockDataSource.getParentLogger()).thenReturn(parentLogger);

        assertSame(parentLogger, datasource.getParentLogger());
        verify(mockDataSource).getParentLogger();
    }
}
