package fr.emcastro.jdbcretyper.jdbc.delegation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.emcastro.jdbcretyper.jdbc.RetyperConnection;
import fr.emcastro.jdbcretyper.transform.TypeTransformerRegistry;

@ExtendWith(MockitoExtension.class)
class RetyperConnectionDelegationTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private TypeTransformerRegistry registry;

    private RetyperConnection connection;

    @BeforeEach
    void setUp() {
        connection = new RetyperConnection(mockConnection, registry);
    }

    @Test
    // Check that getAutoCommit() delegates to the underlying Connection
    // without additional transformation.
    void getAutoCommit_delegates() throws SQLException {
        when(mockConnection.getAutoCommit()).thenReturn(true);
        assertTrue(connection.getAutoCommit());
        verify(mockConnection).getAutoCommit();
    }

    @Test
    // Check that setAutoCommit(boolean) delegates to the underlying
    // Connection without additional transformation.
    void setAutoCommit_delegates() throws SQLException {
        connection.setAutoCommit(false);
        verify(mockConnection).setAutoCommit(false);
    }

    @Test
    // Check that getCatalog() delegates to the underlying Connection
    // without additional transformation.
    void getCatalog_delegates() throws SQLException {
        when(mockConnection.getCatalog()).thenReturn("mydb");
        assertEquals("mydb", connection.getCatalog());
        verify(mockConnection).getCatalog();
    }

    @Test
    // Check that setCatalog(String) delegates to the underlying Connection
    // without additional transformation.
    void setCatalog_delegates() throws SQLException {
        connection.setCatalog("otherdb");
        verify(mockConnection).setCatalog("otherdb");
    }

    @Test
    // Check that getSchema() delegates to the underlying Connection
    // without additional transformation.
    void getSchema_delegates() throws SQLException {
        when(mockConnection.getSchema()).thenReturn("public");
        assertEquals("public", connection.getSchema());
        verify(mockConnection).getSchema();
    }

    @Test
    // Check that setSchema(String) delegates to the underlying Connection
    // without additional transformation.
    void setSchema_delegates() throws SQLException {
        connection.setSchema("other");
        verify(mockConnection).setSchema("other");
    }

    @Test
    // Check that getHoldability() delegates to the underlying Connection
    // without additional transformation.
    void getHoldability_delegates() throws SQLException {
        when(mockConnection.getHoldability()).thenReturn(1001);
        assertEquals(1001, connection.getHoldability());
        verify(mockConnection).getHoldability();
    }

    @Test
    // Check that setHoldability(int) delegates to the underlying
    // Connection without additional transformation.
    void setHoldability_delegates() throws SQLException {
        connection.setHoldability(1002);
        verify(mockConnection).setHoldability(1002);
    }

    @Test
    // Check that getTransactionIsolation() delegates to the underlying
    // Connection without additional transformation.
    void getTransactionIsolation_delegates() throws SQLException {
        when(mockConnection.getTransactionIsolation()).thenReturn(2);
        assertEquals(2, connection.getTransactionIsolation());
        verify(mockConnection).getTransactionIsolation();
    }

    @Test
    // Check that setTransactionIsolation(int) delegates to the underlying
    // Connection without additional transformation.
    void setTransactionIsolation_delegates() throws SQLException {
        connection.setTransactionIsolation(8);
        verify(mockConnection).setTransactionIsolation(8);
    }

    @Test
    // Check that isReadOnly() delegates to the underlying Connection
    // without additional transformation.
    void isReadOnly_delegates() throws SQLException {
        when(mockConnection.isReadOnly()).thenReturn(true);
        assertTrue(connection.isReadOnly());
        verify(mockConnection).isReadOnly();
    }

    @Test
    // Check that setReadOnly(boolean) delegates to the underlying
    // Connection without additional transformation.
    void setReadOnly_delegates() throws SQLException {
        connection.setReadOnly(true);
        verify(mockConnection).setReadOnly(true);
    }

    @Test
    // Check that isClosed() delegates to the underlying Connection
    // without additional transformation.
    void isClosed_delegates() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(true);
        assertTrue(connection.isClosed());
        verify(mockConnection).isClosed();
    }

    @Test
    // Check that isValid(int) delegates to the underlying Connection
    // without additional transformation.
    void isValid_delegates() throws SQLException {
        when(mockConnection.isValid(5)).thenReturn(true);
        assertTrue(connection.isValid(5));
        verify(mockConnection).isValid(5);
    }

    @Test
    // Check that getWarnings() delegates to the underlying Connection
    // without additional transformation.
    void getWarnings_delegates() throws SQLException {
        SQLWarning warning = new SQLWarning("test");
        when(mockConnection.getWarnings()).thenReturn(warning);
        assertSame(warning, connection.getWarnings());
        verify(mockConnection).getWarnings();
    }

    @Test
    // Check that clearWarnings() delegates to the underlying Connection
    // without additional transformation.
    void clearWarnings_delegates() throws SQLException {
        connection.clearWarnings();
        verify(mockConnection).clearWarnings();
    }

    @Test
    // Check that commit() delegates to the underlying Connection without
    // additional transformation.
    void commit_delegates() throws SQLException {
        connection.commit();
        verify(mockConnection).commit();
    }

    @Test
    // Check that rollback() delegates to the underlying Connection without
    // additional transformation.
    void rollback_delegates() throws SQLException {
        connection.rollback();
        verify(mockConnection).rollback();
    }

    @Test
    // Check that nativeSQL(String) delegates to the underlying Connection
    // without additional transformation.
    void nativeSQL_delegates() throws SQLException {
        when(mockConnection.nativeSQL("SELECT 1")).thenReturn("SELECT 1");
        assertEquals("SELECT 1", connection.nativeSQL("SELECT 1"));
        verify(mockConnection).nativeSQL("SELECT 1");
    }

    @Test
    // Check that getMetaData() delegates to the underlying Connection
    // without additional transformation.
    void getMetaData_delegates() throws SQLException {
        DatabaseMetaData dbMetaData = mock(DatabaseMetaData.class);
        when(mockConnection.getMetaData()).thenReturn(dbMetaData);
        assertSame(dbMetaData, connection.getMetaData());
        verify(mockConnection).getMetaData();
    }

    @Test
    // Check that getClientInfo() delegates to the underlying Connection
    // without additional transformation.
    void getClientInfo_delegates() throws SQLException {
        Properties clientInfo = new Properties();
        when(mockConnection.getClientInfo()).thenReturn(clientInfo);
        assertEquals(clientInfo, connection.getClientInfo());
        verify(mockConnection).getClientInfo();
    }

    @Test
    // Check that getNetworkTimeout() delegates to the underlying Connection
    // without additional transformation.
    void getNetworkTimeout_delegates() throws SQLException {
        when(mockConnection.getNetworkTimeout()).thenReturn(30000);
        assertEquals(30000, connection.getNetworkTimeout());
        verify(mockConnection).getNetworkTimeout();
    }

    @Test
    // Check that abort(Executor) delegates to the underlying Connection
    // without additional transformation.
    void abort_delegates() throws SQLException {
        Executor executor = mock(Executor.class);
        connection.abort(executor);
        verify(mockConnection).abort(executor);
    }

    @Test
    // Check that createArrayOf(String, Object[]) delegates to the
    // underlying Connection without additional transformation.
    void createArrayOf_delegates() throws SQLException {
        Array array = mock(Array.class);
        when(mockConnection.createArrayOf("INTEGER", new Object[] {1, 2})).thenReturn(array);
        assertSame(array, connection.createArrayOf("INTEGER", new Object[] {1, 2}));
        verify(mockConnection).createArrayOf("INTEGER", new Object[] {1, 2});
    }

    @Test
    // Check that createBlob() delegates to the underlying Connection
    // without additional transformation.
    void createBlob_delegates() throws SQLException {
        Blob blob = mock(Blob.class);
        when(mockConnection.createBlob()).thenReturn(blob);
        assertSame(blob, connection.createBlob());
        verify(mockConnection).createBlob();
    }

    @Test
    // Check that createClob() delegates to the underlying Connection
    // without additional transformation.
    void createClob_delegates() throws SQLException {
        Clob clob = mock(Clob.class);
        when(mockConnection.createClob()).thenReturn(clob);
        assertSame(clob, connection.createClob());
        verify(mockConnection).createClob();
    }

    @Test
    // Check that createNClob() delegates to the underlying Connection
    // without additional transformation.
    void createNClob_delegates() throws SQLException {
        NClob nClob = mock(NClob.class);
        when(mockConnection.createNClob()).thenReturn(nClob);
        assertSame(nClob, connection.createNClob());
        verify(mockConnection).createNClob();
    }

    @Test
    // Check that createSQLXML() delegates to the underlying Connection
    // without additional transformation.
    void createSQLXML_delegates() throws SQLException {
        SQLXML sqlXml = mock(SQLXML.class);
        when(mockConnection.createSQLXML()).thenReturn(sqlXml);
        assertSame(sqlXml, connection.createSQLXML());
        verify(mockConnection).createSQLXML();
    }

    @Test
    // Check that createStruct(String, Object[]) delegates to the
    // underlying Connection without additional transformation.
    void createStruct_delegates() throws SQLException {
        Struct struct = mock(Struct.class);
        when(mockConnection.createStruct("MY_TYPE", new Object[] {"a", "b"})).thenReturn(struct);
        assertSame(struct, connection.createStruct("MY_TYPE", new Object[] {"a", "b"}));
        verify(mockConnection).createStruct("MY_TYPE", new Object[] {"a", "b"});
    }

    @Test
    // Check that getClientInfo(String) delegates to the underlying
    // Connection without additional transformation.
    void getClientInfo_byName_delegates() throws SQLException {
        when(mockConnection.getClientInfo("appname")).thenReturn("myapp");
        assertEquals("myapp", connection.getClientInfo("appname"));
        verify(mockConnection).getClientInfo("appname");
    }

    @Test
    // Check that getTypeMap() delegates to the underlying Connection
    // without additional transformation.
    void getTypeMap_delegates() throws SQLException {
        Map<String, Class<?>> typeMap = Map.of();
        when(mockConnection.getTypeMap()).thenReturn(typeMap);
        assertEquals(typeMap, connection.getTypeMap());
        verify(mockConnection).getTypeMap();
    }

    @Test
    // Check that releaseSavepoint(Savepoint) delegates to the
    // underlying Connection without additional transformation.
    void releaseSavepoint_delegates() throws SQLException {
        Savepoint savepoint = mock(Savepoint.class);
        connection.releaseSavepoint(savepoint);
        verify(mockConnection).releaseSavepoint(savepoint);
    }

    @Test
    // Check that rollback(Savepoint) delegates to the underlying
    // Connection without additional transformation.
    void rollback_withSavepoint_delegates() throws SQLException {
        Savepoint savepoint = mock(Savepoint.class);
        connection.rollback(savepoint);
        verify(mockConnection).rollback(savepoint);
    }

    @Test
    // Check that setClientInfo(Properties) delegates to the underlying
    // Connection without additional transformation.
    void setClientInfo_withProperties_delegates() throws SQLException {
        Properties props = new Properties();
        props.setProperty("appname", "myapp");
        connection.setClientInfo(props);
        verify(mockConnection).setClientInfo(props);
    }

    @Test
    // Check that setClientInfo(String, String) delegates to the
    // underlying Connection without additional transformation.
    void setClientInfo_withNameValue_delegates() throws SQLException {
        connection.setClientInfo("appname", "myapp");
        verify(mockConnection).setClientInfo("appname", "myapp");
    }

    @Test
    // Check that setNetworkTimeout(Executor, int) delegates to the
    // underlying Connection without additional transformation.
    void setNetworkTimeout_delegates() throws SQLException {
        Executor executor = mock(Executor.class);
        connection.setNetworkTimeout(executor, 30000);
        verify(mockConnection).setNetworkTimeout(executor, 30000);
    }

    @Test
    // Check that setSavepoint() delegates to the underlying Connection
    // without additional transformation.
    void setSavepoint_delegates() throws SQLException {
        Savepoint savepoint = mock(Savepoint.class);
        when(mockConnection.setSavepoint()).thenReturn(savepoint);
        assertSame(savepoint, connection.setSavepoint());
        verify(mockConnection).setSavepoint();
    }

    @Test
    // Check that setSavepoint(String) delegates to the underlying
    // Connection without additional transformation.
    void setSavepoint_withName_delegates() throws SQLException {
        Savepoint savepoint = mock(Savepoint.class);
        when(mockConnection.setSavepoint("sp1")).thenReturn(savepoint);
        assertSame(savepoint, connection.setSavepoint("sp1"));
        verify(mockConnection).setSavepoint("sp1");
    }

    @Test
    // Check that setTypeMap(Map) delegates to the underlying Connection
    // without additional transformation.
    void setTypeMap_delegates() throws SQLException {
        Map<String, Class<?>> map = Map.of();
        connection.setTypeMap(map);
        verify(mockConnection).setTypeMap(map);
    }
}
