package fr.emcastro.jdbcretyper.jdbc.delegation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.emcastro.jdbcretyper.jdbc.RetyperConnection;
import fr.emcastro.jdbcretyper.jdbc.RetyperPreparedStatement;
import fr.emcastro.jdbcretyper.transform.TypeTransformerRegistry;

@ExtendWith(MockitoExtension.class)
class RetyperPreparedStatementDelegationTest {

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
    // Check that addBatch() delegates to the underlying PreparedStatement
    // without additional transformation.
    void addBatch_delegates() throws SQLException {
        statement.addBatch();
        verify(mockPreparedStatement).addBatch();
    }

    @Test
    // Check that clearParameters() delegates to the underlying
    // PreparedStatement without additional transformation.
    void clearParameters_delegates() throws SQLException {
        statement.clearParameters();
        verify(mockPreparedStatement).clearParameters();
    }

    @Test
    // Check that execute() delegates to the underlying PreparedStatement
    // without additional transformation.
    void execute_delegates() throws SQLException {
        when(mockPreparedStatement.execute()).thenReturn(true);
        assertTrue(statement.execute());
        verify(mockPreparedStatement).execute();
    }

    @Test
    // Check that executeUpdate() delegates to the underlying
    // PreparedStatement without additional transformation.
    void executeUpdate_delegates() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        assertEquals(1, statement.executeUpdate());
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    // Check that getMetaData() delegates to the underlying
    // PreparedStatement without additional transformation.
    void getMetaData_delegates() throws SQLException {
        var metaData = mock(ResultSetMetaData.class);
        when(mockPreparedStatement.getMetaData()).thenReturn(metaData);
        assertSame(metaData, statement.getMetaData());
        verify(mockPreparedStatement).getMetaData();
    }

    @Test
    // Check that getParameterMetaData() delegates to the underlying
    // PreparedStatement without additional transformation.
    void getParameterMetaData_delegates() throws SQLException {
        var paramMetaData = mock(ParameterMetaData.class);
        when(mockPreparedStatement.getParameterMetaData()).thenReturn(paramMetaData);
        assertSame(paramMetaData, statement.getParameterMetaData());
        verify(mockPreparedStatement).getParameterMetaData();
    }

    @Test
    // Check that setNull(int, int) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setNull_delegates() throws SQLException {
        statement.setNull(1, 12);
        verify(mockPreparedStatement).setNull(1, 12);
    }

    @Test
    // Check that setNull(int, int, String) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setNull_withTypeName_delegates() throws SQLException {
        statement.setNull(1, 12, "VARCHAR");
        verify(mockPreparedStatement).setNull(1, 12, "VARCHAR");
    }

    @Test
    // Check that setBoolean(int, boolean) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setBoolean_delegates() throws SQLException {
        statement.setBoolean(1, true);
        verify(mockPreparedStatement).setBoolean(1, true);
    }

    @Test
    // Check that setByte(int, byte) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setByte_delegates() throws SQLException {
        statement.setByte(1, (byte) 42);
        verify(mockPreparedStatement).setByte(1, (byte) 42);
    }

    @Test
    // Check that setShort(int, short) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setShort_delegates() throws SQLException {
        statement.setShort(1, (short) 42);
        verify(mockPreparedStatement).setShort(1, (short) 42);
    }

    @Test
    // Check that setInt(int, int) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setInt_delegates() throws SQLException {
        statement.setInt(1, 42);
        verify(mockPreparedStatement).setInt(1, 42);
    }

    @Test
    // Check that setLong(int, long) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setLong_delegates() throws SQLException {
        statement.setLong(1, 42L);
        verify(mockPreparedStatement).setLong(1, 42L);
    }

    @Test
    // Check that setFloat(int, float) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setFloat_delegates() throws SQLException {
        statement.setFloat(1, 1.5f);
        verify(mockPreparedStatement).setFloat(1, 1.5f);
    }

    @Test
    // Check that setDouble(int, double) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setDouble_delegates() throws SQLException {
        statement.setDouble(1, 1.5);
        verify(mockPreparedStatement).setDouble(1, 1.5);
    }

    @Test
    // Check that setBigDecimal(int, BigDecimal) delegates to the
    // underlying PreparedStatement without additional transformation.
    void setBigDecimal_delegates() throws SQLException {
        var bigDecimal = new BigDecimal("123.45");
        statement.setBigDecimal(1, bigDecimal);
        verify(mockPreparedStatement).setBigDecimal(1, bigDecimal);
    }

    @Test
    // Check that setString(int, String) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setString_delegates() throws SQLException {
        statement.setString(1, "hello");
        verify(mockPreparedStatement).setString(1, "hello");
    }

    @Test
    // Check that setBytes(int, byte[]) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setBytes_delegates() throws SQLException {
        statement.setBytes(1, new byte[] {1, 2, 3});
        verify(mockPreparedStatement).setBytes(1, new byte[] {1, 2, 3});
    }

    @Test
    // Check that setDate(int, Date) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setDate_delegates() throws SQLException {
        var date = new Date(System.currentTimeMillis());
        statement.setDate(1, date);
        verify(mockPreparedStatement).setDate(1, date);
    }

    @Test
    // Check that setDate(int, Date, Calendar) delegates to the
    // underlying PreparedStatement without additional transformation.
    void setDate_withCalendar_delegates() throws SQLException {
        var date = new Date(System.currentTimeMillis());
        var cal = Calendar.getInstance();
        statement.setDate(1, date, cal);
        verify(mockPreparedStatement).setDate(1, date, cal);
    }

    @Test
    // Check that setTime(int, Time) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setTime_delegates() throws SQLException {
        var time = new Time(System.currentTimeMillis());
        statement.setTime(1, time);
        verify(mockPreparedStatement).setTime(1, time);
    }

    @Test
    // Check that setTime(int, Time, Calendar) delegates to the
    // underlying PreparedStatement without additional transformation.
    void setTime_withCalendar_delegates() throws SQLException {
        var time = new Time(System.currentTimeMillis());
        var cal = Calendar.getInstance();
        statement.setTime(1, time, cal);
        verify(mockPreparedStatement).setTime(1, time, cal);
    }

    @Test
    // Check that setTimestamp(int, Timestamp) delegates to the
    // underlying PreparedStatement without additional transformation.
    void setTimestamp_delegates() throws SQLException {
        var timestamp = new Timestamp(System.currentTimeMillis());
        statement.setTimestamp(1, timestamp);
        verify(mockPreparedStatement).setTimestamp(1, timestamp);
    }

    @Test
    // Check that setTimestamp(int, Timestamp, Calendar) delegates to
    // the underlying PreparedStatement without additional transformation.
    void setTimestamp_withCalendar_delegates() throws SQLException {
        var timestamp = new Timestamp(System.currentTimeMillis());
        var cal = Calendar.getInstance();
        statement.setTimestamp(1, timestamp, cal);
        verify(mockPreparedStatement).setTimestamp(1, timestamp, cal);
    }

    @Test
    // Check that setAsciiStream(int, InputStream) delegates to the
    // underlying PreparedStatement without additional transformation.
    void setAsciiStream_delegates() throws SQLException {
        var stream = mock(InputStream.class);
        statement.setAsciiStream(1, stream);
        verify(mockPreparedStatement).setAsciiStream(1, stream);
    }

    @Test
    // Check that setBinaryStream(int, InputStream) delegates to the
    // underlying PreparedStatement without additional transformation.
    void setBinaryStream_delegates() throws SQLException {
        var stream = mock(InputStream.class);
        statement.setBinaryStream(1, stream);
        verify(mockPreparedStatement).setBinaryStream(1, stream);
    }

    @Test
    // Check that setCharacterStream(int, Reader) delegates to the
    // underlying PreparedStatement without additional transformation.
    void setCharacterStream_delegates() throws SQLException {
        var reader = mock(Reader.class);
        statement.setCharacterStream(1, reader);
        verify(mockPreparedStatement).setCharacterStream(1, reader);
    }

    @Test
    // Check that setBlob(int, Blob) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setBlob_delegates() throws SQLException {
        var blob = mock(Blob.class);
        statement.setBlob(1, blob);
        verify(mockPreparedStatement).setBlob(1, blob);
    }

    @Test
    // Check that setClob(int, Clob) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setClob_delegates() throws SQLException {
        var clob = mock(Clob.class);
        statement.setClob(1, clob);
        verify(mockPreparedStatement).setClob(1, clob);
    }

    @Test
    // Check that setArray(int, Array) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setArray_delegates() throws SQLException {
        var array = mock(Array.class);
        statement.setArray(1, array);
        verify(mockPreparedStatement).setArray(1, array);
    }

    @Test
    // Check that setRef(int, Ref) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setRef_delegates() throws SQLException {
        var ref = mock(Ref.class);
        statement.setRef(1, ref);
        verify(mockPreparedStatement).setRef(1, ref);
    }

    @Test
    // Check that setRowId(int, RowId) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setRowId_delegates() throws SQLException {
        var rowId = mock(RowId.class);
        statement.setRowId(1, rowId);
        verify(mockPreparedStatement).setRowId(1, rowId);
    }

    @Test
    // Check that setNString(int, String) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setNString_delegates() throws SQLException {
        statement.setNString(1, "hello");
        verify(mockPreparedStatement).setNString(1, "hello");
    }

    @Test
    // Check that setNClob(int, NClob) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setNClob_delegates() throws SQLException {
        var nclob = mock(NClob.class);
        statement.setNClob(1, nclob);
        verify(mockPreparedStatement).setNClob(1, nclob);
    }

    @Test
    // Check that setSQLXML(int, SQLXML) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setSQLXML_delegates() throws SQLException {
        var xml = mock(SQLXML.class);
        statement.setSQLXML(1, xml);
        verify(mockPreparedStatement).setSQLXML(1, xml);
    }

    @Test
    // Check that setURL(int, URL) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setURL_delegates() throws Exception {
        var url = new URL("http://example.com");
        statement.setURL(1, url);
        verify(mockPreparedStatement).setURL(1, url);
    }

    @Test
    // Check that setAsciiStream(int, InputStream, int) delegates to
    // the underlying PreparedStatement without additional transformation.
    void setAsciiStream_withIntLength_delegates() throws SQLException {
        var stream = mock(InputStream.class);
        statement.setAsciiStream(1, stream, 100);
        verify(mockPreparedStatement).setAsciiStream(1, stream, 100);
    }

    @Test
    // Check that setAsciiStream(int, InputStream, long) delegates to
    // the underlying PreparedStatement without additional transformation.
    void setAsciiStream_withLongLength_delegates() throws SQLException {
        var stream = mock(InputStream.class);
        statement.setAsciiStream(1, stream, 100L);
        verify(mockPreparedStatement).setAsciiStream(1, stream, 100L);
    }

    @Test
    // Check that setBinaryStream(int, InputStream, int) delegates to
    // the underlying PreparedStatement without additional transformation.
    void setBinaryStream_withIntLength_delegates() throws SQLException {
        var stream = mock(InputStream.class);
        statement.setBinaryStream(1, stream, 100);
        verify(mockPreparedStatement).setBinaryStream(1, stream, 100);
    }

    @Test
    // Check that setBinaryStream(int, InputStream, long) delegates to
    // the underlying PreparedStatement without additional transformation.
    void setBinaryStream_withLongLength_delegates() throws SQLException {
        var stream = mock(InputStream.class);
        statement.setBinaryStream(1, stream, 100L);
        verify(mockPreparedStatement).setBinaryStream(1, stream, 100L);
    }

    @Test
    // Check that setCharacterStream(int, Reader, int) delegates to
    // the underlying PreparedStatement without additional transformation.
    void setCharacterStream_withIntLength_delegates() throws SQLException {
        var reader = mock(Reader.class);
        statement.setCharacterStream(1, reader, 100);
        verify(mockPreparedStatement).setCharacterStream(1, reader, 100);
    }

    @Test
    // Check that setCharacterStream(int, Reader, long) delegates to
    // the underlying PreparedStatement without additional transformation.
    void setCharacterStream_withLongLength_delegates() throws SQLException {
        var reader = mock(Reader.class);
        statement.setCharacterStream(1, reader, 100L);
        verify(mockPreparedStatement).setCharacterStream(1, reader, 100L);
    }

    @Test
    // Check that setBlob(int, InputStream) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setBlob_withInputStream_delegates() throws SQLException {
        var stream = mock(InputStream.class);
        statement.setBlob(1, stream);
        verify(mockPreparedStatement).setBlob(1, stream);
    }

    @Test
    // Check that setBlob(int, InputStream, long) delegates to the
    // underlying PreparedStatement without additional transformation.
    void setBlob_withInputStreamAndLength_delegates() throws SQLException {
        var stream = mock(InputStream.class);
        statement.setBlob(1, stream, 100L);
        verify(mockPreparedStatement).setBlob(1, stream, 100L);
    }

    @Test
    // Check that setClob(int, Reader) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setClob_withReader_delegates() throws SQLException {
        var reader = mock(Reader.class);
        statement.setClob(1, reader);
        verify(mockPreparedStatement).setClob(1, reader);
    }

    @Test
    // Check that setClob(int, Reader, long) delegates to the
    // underlying PreparedStatement without additional transformation.
    void setClob_withReaderAndLength_delegates() throws SQLException {
        var reader = mock(Reader.class);
        statement.setClob(1, reader, 100L);
        verify(mockPreparedStatement).setClob(1, reader, 100L);
    }

    @Test
    // Check that setNClob(int, Reader) delegates to the underlying
    // PreparedStatement without additional transformation.
    void setNClob_withReader_delegates() throws SQLException {
        var reader = mock(Reader.class);
        statement.setNClob(1, reader);
        verify(mockPreparedStatement).setNClob(1, reader);
    }

    @Test
    // Check that setNClob(int, Reader, long) delegates to the
    // underlying PreparedStatement without additional transformation.
    void setNClob_withReaderAndLength_delegates() throws SQLException {
        var reader = mock(Reader.class);
        statement.setNClob(1, reader, 100L);
        verify(mockPreparedStatement).setNClob(1, reader, 100L);
    }

    @Test
    // Check that setNCharacterStream(int, Reader) delegates to the
    // underlying PreparedStatement without additional transformation.
    void setNCharacterStream_delegates() throws SQLException {
        var reader = mock(Reader.class);
        statement.setNCharacterStream(1, reader);
        verify(mockPreparedStatement).setNCharacterStream(1, reader);
    }

    @Test
    // Check that setNCharacterStream(int, Reader, long) delegates to
    // the underlying PreparedStatement without additional transformation.
    void setNCharacterStream_withLength_delegates() throws SQLException {
        var reader = mock(Reader.class);
        statement.setNCharacterStream(1, reader, 100L);
        verify(mockPreparedStatement).setNCharacterStream(1, reader, 100L);
    }

    @Test
    // Check that setUnicodeStream(int, InputStream, int) delegates to
    // the underlying PreparedStatement without additional transformation.
    @SuppressWarnings("deprecation")
    void setUnicodeStream_delegates() throws SQLException {
        var stream = mock(InputStream.class);
        statement.setUnicodeStream(1, stream, 100);
        verify(mockPreparedStatement).setUnicodeStream(1, stream, 100);
    }
}
