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
import java.sql.JDBCType;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.emcastro.jdbcretyper.jdbc.RetyperResultSet;
import fr.emcastro.jdbcretyper.transform.TypeTransformerRegistry;

@ExtendWith(MockitoExtension.class)
class RetyperResultSetDelegationTest {

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private TypeTransformerRegistry registry;

    private RetyperResultSet rs;

    @BeforeEach
    void setUp() {
        rs = new RetyperResultSet(mockResultSet, registry, null);
    }

    // --- Primitive getters ---

    @Test
    // Check that getBoolean(int) delegates to the underlying ResultSet
    // without additional transformation.
    void getBoolean_delegates() throws SQLException {
        when(mockResultSet.getBoolean(1)).thenReturn(true);
        assertTrue(rs.getBoolean(1));
        verify(mockResultSet).getBoolean(1);
    }

    @Test
    // Check that getByte(int) delegates to the underlying ResultSet
    // without additional transformation.
    void getByte_delegates() throws SQLException {
        when(mockResultSet.getByte(1)).thenReturn((byte) 42);
        assertEquals((byte) 42, rs.getByte(1));
        verify(mockResultSet).getByte(1);
    }

    @Test
    // Check that getShort(int) delegates to the underlying ResultSet
    // without additional transformation.
    void getShort_delegates() throws SQLException {
        when(mockResultSet.getShort(1)).thenReturn((short) 42);
        assertEquals((short) 42, rs.getShort(1));
        verify(mockResultSet).getShort(1);
    }

    @Test
    // Check that getInt(int) delegates to the underlying ResultSet
    // without additional transformation.
    void getInt_delegates() throws SQLException {
        when(mockResultSet.getInt(1)).thenReturn(42);
        assertEquals(42, rs.getInt(1));
        verify(mockResultSet).getInt(1);
    }

    @Test
    // Check that getLong(int) delegates to the underlying ResultSet
    // without additional transformation.
    void getLong_delegates() throws SQLException {
        when(mockResultSet.getLong(1)).thenReturn(42L);
        assertEquals(42L, rs.getLong(1));
        verify(mockResultSet).getLong(1);
    }

    @Test
    // Check that getFloat(int) delegates to the underlying ResultSet
    // without additional transformation.
    void getFloat_delegates() throws SQLException {
        when(mockResultSet.getFloat(1)).thenReturn(1.5f);
        assertEquals(1.5f, rs.getFloat(1), 0.001f);
        verify(mockResultSet).getFloat(1);
    }

    @Test
    // Check that getDouble(int) delegates to the underlying ResultSet
    // without additional transformation.
    void getDouble_delegates() throws SQLException {
        when(mockResultSet.getDouble(1)).thenReturn(1.5);
        assertEquals(1.5, rs.getDouble(1), 0.001);
        verify(mockResultSet).getDouble(1);
    }

    @Test
    // Check that getBigDecimal(int) delegates to the underlying
    // ResultSet without additional transformation.
    void getBigDecimal_delegates() throws SQLException {
        BigDecimal expected = new BigDecimal("123.45");
        when(mockResultSet.getBigDecimal(1)).thenReturn(expected);
        assertEquals(expected, rs.getBigDecimal(1));
        verify(mockResultSet).getBigDecimal(1);
    }

    @Test
    // Check that getBigDecimal(int, int) delegates to the underlying
    // ResultSet without additional transformation.
    void getBigDecimal_withScale_delegates() throws SQLException {
        BigDecimal expected = new BigDecimal("123.45");
        when(mockResultSet.getBigDecimal(1, 2)).thenReturn(expected);
        assertEquals(expected, rs.getBigDecimal(1, 2));
        verify(mockResultSet).getBigDecimal(1, 2);
    }

    @Test
    // Check that getString(int) delegates to the underlying ResultSet
    // without additional transformation.
    void getString_delegates() throws SQLException {
        when(mockResultSet.getString(1)).thenReturn("hello");
        assertEquals("hello", rs.getString(1));
        verify(mockResultSet).getString(1);
    }

    @Test
    // Check that getBytes(int) delegates to the underlying ResultSet
    // without additional transformation.
    void getBytes_delegates() throws SQLException {
        byte[] expected = {1, 2, 3};
        when(mockResultSet.getBytes(1)).thenReturn(expected);
        assertArrayEquals(expected, rs.getBytes(1));
        verify(mockResultSet).getBytes(1);
    }

    // --- Date/Time getters ---

    @Test
    // Check that getDate(int) delegates to the underlying ResultSet
    // without additional transformation.
    void getDate_delegates() throws SQLException {
        Date expected = new Date(System.currentTimeMillis());
        when(mockResultSet.getDate(1)).thenReturn(expected);
        assertEquals(expected, rs.getDate(1));
        verify(mockResultSet).getDate(1);
    }

    @Test
    // Check that getTime(int) delegates to the underlying ResultSet
    // without additional transformation.
    void getTime_delegates() throws SQLException {
        Time expected = new Time(System.currentTimeMillis());
        when(mockResultSet.getTime(1)).thenReturn(expected);
        assertEquals(expected, rs.getTime(1));
        verify(mockResultSet).getTime(1);
    }

    @Test
    // Check that getTimestamp(int) delegates to the underlying ResultSet
    // without additional transformation.
    void getTimestamp_delegates() throws SQLException {
        Timestamp expected = new Timestamp(System.currentTimeMillis());
        when(mockResultSet.getTimestamp(1)).thenReturn(expected);
        assertEquals(expected, rs.getTimestamp(1));
        verify(mockResultSet).getTimestamp(1);
    }

    @Test
    // Check that getDate(int, Calendar) delegates to the underlying
    // ResultSet without additional transformation.
    void getDate_withCalendar_delegates() throws SQLException {
        Date expected = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        when(mockResultSet.getDate(1, cal)).thenReturn(expected);
        assertEquals(expected, rs.getDate(1, cal));
        verify(mockResultSet).getDate(1, cal);
    }

    @Test
    // Check that getTime(int, Calendar) delegates to the underlying
    // ResultSet without additional transformation.
    void getTime_withCalendar_delegates() throws SQLException {
        Time expected = new Time(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        when(mockResultSet.getTime(1, cal)).thenReturn(expected);
        assertEquals(expected, rs.getTime(1, cal));
        verify(mockResultSet).getTime(1, cal);
    }

    @Test
    // Check that getTimestamp(int, Calendar) delegates to the underlying
    // ResultSet without additional transformation.
    void getTimestamp_withCalendar_delegates() throws SQLException {
        Timestamp expected = new Timestamp(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        when(mockResultSet.getTimestamp(1, cal)).thenReturn(expected);
        assertEquals(expected, rs.getTimestamp(1, cal));
        verify(mockResultSet).getTimestamp(1, cal);
    }

    // --- Stream getters ---

    @Test
    // Check that getAsciiStream(int) delegates to the underlying
    // ResultSet without additional transformation.
    void getAsciiStream_delegates() throws SQLException {
        InputStream expected = mock(InputStream.class);
        when(mockResultSet.getAsciiStream(1)).thenReturn(expected);
        assertSame(expected, rs.getAsciiStream(1));
        verify(mockResultSet).getAsciiStream(1);
    }

    @Test
    // Check that getBinaryStream(int) delegates to the underlying
    // ResultSet without additional transformation.
    void getBinaryStream_delegates() throws SQLException {
        InputStream expected = mock(InputStream.class);
        when(mockResultSet.getBinaryStream(1)).thenReturn(expected);
        assertSame(expected, rs.getBinaryStream(1));
        verify(mockResultSet).getBinaryStream(1);
    }

    @Test
    // Check that getCharacterStream(int) delegates to the underlying
    // ResultSet without additional transformation.
    void getCharacterStream_delegates() throws SQLException {
        Reader expected = mock(Reader.class);
        when(mockResultSet.getCharacterStream(1)).thenReturn(expected);
        assertSame(expected, rs.getCharacterStream(1));
        verify(mockResultSet).getCharacterStream(1);
    }

    // --- By-label getters ---

    @Test
    // Check that getString(String) delegates to the underlying ResultSet
    // without additional transformation.
    void getString_byLabel_delegates() throws SQLException {
        when(mockResultSet.getString("col")).thenReturn("hello");
        assertEquals("hello", rs.getString("col"));
        verify(mockResultSet).getString("col");
    }

    @Test
    // Check that getInt(String) delegates to the underlying ResultSet
    // without additional transformation.
    void getInt_byLabel_delegates() throws SQLException {
        when(mockResultSet.getInt("col")).thenReturn(42);
        assertEquals(42, rs.getInt("col"));
        verify(mockResultSet).getInt("col");
    }

    @Test
    // Check that getBoolean(String) delegates to the underlying
    // ResultSet without additional transformation.
    void getBoolean_byLabel_delegates() throws SQLException {
        when(mockResultSet.getBoolean("col")).thenReturn(true);
        assertTrue(rs.getBoolean("col"));
        verify(mockResultSet).getBoolean("col");
    }

    // --- Navigation ---

    @Test
    // Check that next() delegates to the underlying ResultSet without
    // additional transformation.
    void next_delegates() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        assertTrue(rs.next());
        verify(mockResultSet).next();
    }

    @Test
    // Check that previous() delegates to the underlying ResultSet
    // without additional transformation.
    void previous_delegates() throws SQLException {
        when(mockResultSet.previous()).thenReturn(true);
        assertTrue(rs.previous());
        verify(mockResultSet).previous();
    }

    @Test
    // Check that first() delegates to the underlying ResultSet without
    // additional transformation.
    void first_delegates() throws SQLException {
        when(mockResultSet.first()).thenReturn(true);
        assertTrue(rs.first());
        verify(mockResultSet).first();
    }

    @Test
    // Check that last() delegates to the underlying ResultSet without
    // additional transformation.
    void last_delegates() throws SQLException {
        when(mockResultSet.last()).thenReturn(true);
        assertTrue(rs.last());
        verify(mockResultSet).last();
    }

    @Test
    // Check that beforeFirst() delegates to the underlying ResultSet
    // without additional transformation.
    void beforeFirst_delegates() throws SQLException {
        rs.beforeFirst();
        verify(mockResultSet).beforeFirst();
    }

    @Test
    // Check that afterLast() delegates to the underlying ResultSet
    // without additional transformation.
    void afterLast_delegates() throws SQLException {
        rs.afterLast();
        verify(mockResultSet).afterLast();
    }

    @Test
    // Check that absolute(int) delegates to the underlying ResultSet
    // without additional transformation.
    void absolute_delegates() throws SQLException {
        when(mockResultSet.absolute(5)).thenReturn(true);
        assertTrue(rs.absolute(5));
        verify(mockResultSet).absolute(5);
    }

    @Test
    // Check that relative(int) delegates to the underlying ResultSet
    // without additional transformation.
    void relative_delegates() throws SQLException {
        when(mockResultSet.relative(2)).thenReturn(true);
        assertTrue(rs.relative(2));
        verify(mockResultSet).relative(2);
    }

    @Test
    // Check that moveToInsertRow() delegates to the underlying ResultSet
    // without additional transformation.
    void moveToInsertRow_delegates() throws SQLException {
        rs.moveToInsertRow();
        verify(mockResultSet).moveToInsertRow();
    }

    @Test
    // Check that moveToCurrentRow() delegates to the underlying
    // ResultSet without additional transformation.
    void moveToCurrentRow_delegates() throws SQLException {
        rs.moveToCurrentRow();
        verify(mockResultSet).moveToCurrentRow();
    }

    // --- Position queries ---

    @Test
    // Check that isBeforeFirst() delegates to the underlying ResultSet
    // without additional transformation.
    void isBeforeFirst_delegates() throws SQLException {
        when(mockResultSet.isBeforeFirst()).thenReturn(true);
        assertTrue(rs.isBeforeFirst());
        verify(mockResultSet).isBeforeFirst();
    }

    @Test
    // Check that isAfterLast() delegates to the underlying ResultSet
    // without additional transformation.
    void isAfterLast_delegates() throws SQLException {
        when(mockResultSet.isAfterLast()).thenReturn(true);
        assertTrue(rs.isAfterLast());
        verify(mockResultSet).isAfterLast();
    }

    @Test
    // Check that isFirst() delegates to the underlying ResultSet without
    // additional transformation.
    void isFirst_delegates() throws SQLException {
        when(mockResultSet.isFirst()).thenReturn(true);
        assertTrue(rs.isFirst());
        verify(mockResultSet).isFirst();
    }

    @Test
    // Check that isLast() delegates to the underlying ResultSet without
    // additional transformation.
    void isLast_delegates() throws SQLException {
        when(mockResultSet.isLast()).thenReturn(true);
        assertTrue(rs.isLast());
        verify(mockResultSet).isLast();
    }

    @Test
    // Check that getRow() delegates to the underlying ResultSet without
    // additional transformation.
    void getRow_delegates() throws SQLException {
        when(mockResultSet.getRow()).thenReturn(5);
        assertEquals(5, rs.getRow());
        verify(mockResultSet).getRow();
    }

    @Test
    // Check that findColumn(String) delegates to the underlying
    // ResultSet without additional transformation.
    void findColumn_delegates() throws SQLException {
        when(mockResultSet.findColumn("col")).thenReturn(1);
        assertEquals(1, rs.findColumn("col"));
        verify(mockResultSet).findColumn("col");
    }

    // --- Metadata ---

    @Test
    // Check that getMetaData() delegates to the underlying ResultSet
    // without additional transformation.
    void getMetaData_delegates() throws SQLException {
        ResultSetMetaData expected = mock(ResultSetMetaData.class);
        when(mockResultSet.getMetaData()).thenReturn(expected);
        assertSame(expected, rs.getMetaData());
        verify(mockResultSet).getMetaData();
    }

    // --- Type-specific getters ---

    @Test
    // Check that getArray(int) delegates to the underlying ResultSet
    // without additional transformation.
    void getArray_delegates() throws SQLException {
        Array expected = mock(Array.class);
        when(mockResultSet.getArray(1)).thenReturn(expected);
        assertSame(expected, rs.getArray(1));
        verify(mockResultSet).getArray(1);
    }

    @Test
    // Check that getBlob(int) delegates to the underlying ResultSet
    // without additional transformation.
    void getBlob_delegates() throws SQLException {
        Blob expected = mock(Blob.class);
        when(mockResultSet.getBlob(1)).thenReturn(expected);
        assertSame(expected, rs.getBlob(1));
        verify(mockResultSet).getBlob(1);
    }

    @Test
    // Check that getClob(int) delegates to the underlying ResultSet
    // without additional transformation.
    void getClob_delegates() throws SQLException {
        Clob expected = mock(Clob.class);
        when(mockResultSet.getClob(1)).thenReturn(expected);
        assertSame(expected, rs.getClob(1));
        verify(mockResultSet).getClob(1);
    }

    @Test
    // Check that getRef(int) delegates to the underlying ResultSet
    // without additional transformation.
    void getRef_delegates() throws SQLException {
        Ref expected = mock(Ref.class);
        when(mockResultSet.getRef(1)).thenReturn(expected);
        assertSame(expected, rs.getRef(1));
        verify(mockResultSet).getRef(1);
    }

    @Test
    // Check that getRowId(int) delegates to the underlying ResultSet
    // without additional transformation.
    void getRowId_delegates() throws SQLException {
        RowId expected = mock(RowId.class);
        when(mockResultSet.getRowId(1)).thenReturn(expected);
        assertSame(expected, rs.getRowId(1));
        verify(mockResultSet).getRowId(1);
    }

    @Test
    // Check that getSQLXML(int) delegates to the underlying ResultSet
    // without additional transformation.
    void getSQLXML_delegates() throws SQLException {
        SQLXML expected = mock(SQLXML.class);
        when(mockResultSet.getSQLXML(1)).thenReturn(expected);
        assertSame(expected, rs.getSQLXML(1));
        verify(mockResultSet).getSQLXML(1);
    }

    @Test
    // Check that getURL(int) delegates to the underlying ResultSet
    // without additional transformation.
    void getURL_delegates() throws Exception {
        URL expected = new URL("http://example.com");
        when(mockResultSet.getURL(1)).thenReturn(expected);
        assertEquals(expected, rs.getURL(1));
        verify(mockResultSet).getURL(1);
    }

    @Test
    // Check that getNClob(int) delegates to the underlying ResultSet
    // without additional transformation.
    void getNClob_delegates() throws SQLException {
        NClob expected = mock(NClob.class);
        when(mockResultSet.getNClob(1)).thenReturn(expected);
        assertSame(expected, rs.getNClob(1));
        verify(mockResultSet).getNClob(1);
    }

    @Test
    // Check that getNString(int) delegates to the underlying ResultSet
    // without additional transformation.
    void getNString_delegates() throws SQLException {
        when(mockResultSet.getNString(1)).thenReturn("hello");
        assertEquals("hello", rs.getNString(1));
        verify(mockResultSet).getNString(1);
    }

    @Test
    // Check that getNCharacterStream(int) delegates to the underlying
    // ResultSet without additional transformation.
    void getNCharacterStream_delegates() throws SQLException {
        Reader expected = mock(Reader.class);
        when(mockResultSet.getNCharacterStream(1)).thenReturn(expected);
        assertSame(expected, rs.getNCharacterStream(1));
        verify(mockResultSet).getNCharacterStream(1);
    }

    // --- Update methods (non-registry) ---

    @Test
    // Check that updateNull(int) delegates to the underlying ResultSet
    // without additional transformation.
    void updateNull_delegates() throws SQLException {
        rs.updateNull(1);
        verify(mockResultSet).updateNull(1);
    }

    @Test
    // Check that updateBoolean(int, boolean) delegates to the
    // underlying ResultSet without additional transformation.
    void updateBoolean_delegates() throws SQLException {
        rs.updateBoolean(1, true);
        verify(mockResultSet).updateBoolean(1, true);
    }

    @Test
    // Check that updateByte(int, byte) delegates to the underlying
    // ResultSet without additional transformation.
    void updateByte_delegates() throws SQLException {
        rs.updateByte(1, (byte) 42);
        verify(mockResultSet).updateByte(1, (byte) 42);
    }

    @Test
    // Check that updateShort(int, short) delegates to the underlying
    // ResultSet without additional transformation.
    void updateShort_delegates() throws SQLException {
        rs.updateShort(1, (short) 42);
        verify(mockResultSet).updateShort(1, (short) 42);
    }

    @Test
    // Check that updateInt(int, int) delegates to the underlying
    // ResultSet without additional transformation.
    void updateInt_delegates() throws SQLException {
        rs.updateInt(1, 42);
        verify(mockResultSet).updateInt(1, 42);
    }

    @Test
    // Check that updateLong(int, long) delegates to the underlying
    // ResultSet without additional transformation.
    void updateLong_delegates() throws SQLException {
        rs.updateLong(1, 42L);
        verify(mockResultSet).updateLong(1, 42L);
    }

    @Test
    // Check that updateFloat(int, float) delegates to the underlying
    // ResultSet without additional transformation.
    void updateFloat_delegates() throws SQLException {
        rs.updateFloat(1, 1.5f);
        verify(mockResultSet).updateFloat(1, 1.5f);
    }

    @Test
    // Check that updateDouble(int, double) delegates to the underlying
    // ResultSet without additional transformation.
    void updateDouble_delegates() throws SQLException {
        rs.updateDouble(1, 1.5);
        verify(mockResultSet).updateDouble(1, 1.5);
    }

    @Test
    // Check that updateBigDecimal(int, BigDecimal) delegates to the
    // underlying ResultSet without additional transformation.
    void updateBigDecimal_delegates() throws SQLException {
        BigDecimal value = new BigDecimal("123.45");
        rs.updateBigDecimal(1, value);
        verify(mockResultSet).updateBigDecimal(1, value);
    }

    @Test
    // Check that updateString(int, String) delegates to the underlying
    // ResultSet without additional transformation.
    void updateString_delegates() throws SQLException {
        rs.updateString(1, "hello");
        verify(mockResultSet).updateString(1, "hello");
    }

    @Test
    // Check that updateBytes(int, byte[]) delegates to the underlying
    // ResultSet without additional transformation.
    void updateBytes_delegates() throws SQLException {
        rs.updateBytes(1, new byte[] {1, 2, 3});
        verify(mockResultSet).updateBytes(1, new byte[] {1, 2, 3});
    }

    @Test
    // Check that updateDate(int, Date) delegates to the underlying
    // ResultSet without additional transformation.
    void updateDate_delegates() throws SQLException {
        Date value = new Date(System.currentTimeMillis());
        rs.updateDate(1, value);
        verify(mockResultSet).updateDate(1, value);
    }

    @Test
    // Check that updateTime(int, Time) delegates to the underlying
    // ResultSet without additional transformation.
    void updateTime_delegates() throws SQLException {
        Time value = new Time(System.currentTimeMillis());
        rs.updateTime(1, value);
        verify(mockResultSet).updateTime(1, value);
    }

    @Test
    // Check that updateTimestamp(int, Timestamp) delegates to the
    // underlying ResultSet without additional transformation.
    void updateTimestamp_delegates() throws SQLException {
        Timestamp value = new Timestamp(System.currentTimeMillis());
        rs.updateTimestamp(1, value);
        verify(mockResultSet).updateTimestamp(1, value);
    }

    @Test
    // Check that updateAsciiStream(int, InputStream) delegates to the
    // underlying ResultSet without additional transformation.
    void updateAsciiStream_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        rs.updateAsciiStream(1, stream);
        verify(mockResultSet).updateAsciiStream(1, stream);
    }

    @Test
    // Check that updateAsciiStream(int, InputStream, int) delegates to
    // the underlying ResultSet without additional transformation.
    void updateAsciiStream_withIntLength_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        rs.updateAsciiStream(1, stream, 100);
        verify(mockResultSet).updateAsciiStream(1, stream, 100);
    }

    @Test
    // Check that updateBinaryStream(int, InputStream) delegates to the
    // underlying ResultSet without additional transformation.
    void updateBinaryStream_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        rs.updateBinaryStream(1, stream);
        verify(mockResultSet).updateBinaryStream(1, stream);
    }

    @Test
    // Check that updateBinaryStream(int, InputStream, int) delegates to
    // the underlying ResultSet without additional transformation.
    void updateBinaryStream_withIntLength_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        rs.updateBinaryStream(1, stream, 100);
        verify(mockResultSet).updateBinaryStream(1, stream, 100);
    }

    @Test
    // Check that updateCharacterStream(int, Reader) delegates to the
    // underlying ResultSet without additional transformation.
    void updateCharacterStream_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        rs.updateCharacterStream(1, reader);
        verify(mockResultSet).updateCharacterStream(1, reader);
    }

    @Test
    // Check that updateCharacterStream(int, Reader, int) delegates to
    // the underlying ResultSet without additional transformation.
    void updateCharacterStream_withIntLength_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        rs.updateCharacterStream(1, reader, 100);
        verify(mockResultSet).updateCharacterStream(1, reader, 100);
    }

    @Test
    // Check that updateBlob(int, Blob) delegates to the underlying
    // ResultSet without additional transformation.
    void updateBlob_delegates() throws SQLException {
        Blob blob = mock(Blob.class);
        rs.updateBlob(1, blob);
        verify(mockResultSet).updateBlob(1, blob);
    }

    @Test
    // Check that updateClob(int, Clob) delegates to the underlying
    // ResultSet without additional transformation.
    void updateClob_delegates() throws SQLException {
        Clob clob = mock(Clob.class);
        rs.updateClob(1, clob);
        verify(mockResultSet).updateClob(1, clob);
    }

    @Test
    // Check that updateArray(int, Array) delegates to the underlying
    // ResultSet without additional transformation.
    void updateArray_delegates() throws SQLException {
        Array array = mock(Array.class);
        rs.updateArray(1, array);
        verify(mockResultSet).updateArray(1, array);
    }

    @Test
    // Check that updateRowId(int, RowId) delegates to the underlying
    // ResultSet without additional transformation.
    void updateRowId_delegates() throws SQLException {
        RowId rowId = mock(RowId.class);
        rs.updateRowId(1, rowId);
        verify(mockResultSet).updateRowId(1, rowId);
    }

    @Test
    // Check that updateSQLXML(int, SQLXML) delegates to the underlying
    // ResultSet without additional transformation.
    void updateSQLXML_delegates() throws SQLException {
        SQLXML xml = mock(SQLXML.class);
        rs.updateSQLXML(1, xml);
        verify(mockResultSet).updateSQLXML(1, xml);
    }

    @Test
    // Check that updateNString(int, String) delegates to the underlying
    // ResultSet without additional transformation.
    void updateNString_delegates() throws SQLException {
        rs.updateNString(1, "hello");
        verify(mockResultSet).updateNString(1, "hello");
    }

    @Test
    // Check that updateNClob(int, NClob) delegates to the underlying
    // ResultSet without additional transformation.
    void updateNClob_delegates() throws SQLException {
        NClob nclob = mock(NClob.class);
        rs.updateNClob(1, nclob);
        verify(mockResultSet).updateNClob(1, nclob);
    }

    // --- Row operations ---

    @Test
    // Check that insertRow() delegates to the underlying ResultSet
    // without additional transformation.
    void insertRow_delegates() throws SQLException {
        rs.insertRow();
        verify(mockResultSet).insertRow();
    }

    @Test
    // Check that updateRow() delegates to the underlying ResultSet
    // without additional transformation.
    void updateRow_delegates() throws SQLException {
        rs.updateRow();
        verify(mockResultSet).updateRow();
    }

    @Test
    // Check that deleteRow() delegates to the underlying ResultSet
    // without additional transformation.
    void deleteRow_delegates() throws SQLException {
        rs.deleteRow();
        verify(mockResultSet).deleteRow();
    }

    @Test
    // Check that refreshRow() delegates to the underlying ResultSet
    // without additional transformation.
    void refreshRow_delegates() throws SQLException {
        rs.refreshRow();
        verify(mockResultSet).refreshRow();
    }

    @Test
    // Check that cancelRowUpdates() delegates to the underlying
    // ResultSet without additional transformation.
    void cancelRowUpdates_delegates() throws SQLException {
        rs.cancelRowUpdates();
        verify(mockResultSet).cancelRowUpdates();
    }

    // --- Warnings and cursor ---

    @Test
    // Check that getWarnings() delegates to the underlying ResultSet
    // without additional transformation.
    void getWarnings_delegates() throws SQLException {
        SQLWarning warning = new SQLWarning("test");
        when(mockResultSet.getWarnings()).thenReturn(warning);
        assertSame(warning, rs.getWarnings());
        verify(mockResultSet).getWarnings();
    }

    @Test
    // Check that clearWarnings() delegates to the underlying ResultSet
    // without additional transformation.
    void clearWarnings_delegates() throws SQLException {
        rs.clearWarnings();
        verify(mockResultSet).clearWarnings();
    }

    @Test
    // Check that getCursorName() delegates to the underlying ResultSet
    // without additional transformation.
    void getCursorName_delegates() throws SQLException {
        when(mockResultSet.getCursorName()).thenReturn("myCursor");
        assertEquals("myCursor", rs.getCursorName());
        verify(mockResultSet).getCursorName();
    }

    // --- Concurrency and type ---

    @Test
    // Check that getConcurrency() delegates to the underlying ResultSet
    // without additional transformation.
    void getConcurrency_delegates() throws SQLException {
        when(mockResultSet.getConcurrency()).thenReturn(ResultSet.CONCUR_READ_ONLY);
        assertEquals(ResultSet.CONCUR_READ_ONLY, rs.getConcurrency());
        verify(mockResultSet).getConcurrency();
    }

    @Test
    // Check that getType() delegates to the underlying ResultSet without
    // additional transformation.
    void getType_delegates() throws SQLException {
        when(mockResultSet.getType()).thenReturn(ResultSet.TYPE_FORWARD_ONLY);
        assertEquals(ResultSet.TYPE_FORWARD_ONLY, rs.getType());
        verify(mockResultSet).getType();
    }

    @Test
    // Check that getHoldability() delegates to the underlying ResultSet
    // without additional transformation.
    void getHoldability_delegates() throws SQLException {
        when(mockResultSet.getHoldability()).thenReturn(ResultSet.CLOSE_CURSORS_AT_COMMIT);
        assertEquals(ResultSet.CLOSE_CURSORS_AT_COMMIT, rs.getHoldability());
        verify(mockResultSet).getHoldability();
    }

    // --- Statement reference ---

    @Test
    // Check that getStatement() returns the wrapper Statement passed to
    // the constructor, not the underlying driver's Statement.
    void getStatement_returnsWrapper() throws SQLException {
        Statement mockStmt = mock(Statement.class);
        RetyperResultSet rsWithStatement = new RetyperResultSet(mockResultSet, registry, mockStmt);
        assertSame(mockStmt, rsWithStatement.getStatement());
    }

    @Test
    // Check that getStatement() returns null when no Statement was
    // passed to the constructor.
    void getStatement_returnsNullWhenNoStatement() throws SQLException {
        RetyperResultSet rsWithNoStatement = new RetyperResultSet(mockResultSet, registry, null);
        assertNull(rsWithNoStatement.getStatement());
    }

    // --- Fetch ---

    @Test
    // Check that getFetchDirection() delegates to the underlying
    // ResultSet without additional transformation.
    void getFetchDirection_delegates() throws SQLException {
        when(mockResultSet.getFetchDirection()).thenReturn(ResultSet.FETCH_FORWARD);
        assertEquals(ResultSet.FETCH_FORWARD, rs.getFetchDirection());
        verify(mockResultSet).getFetchDirection();
    }

    @Test
    // Check that getFetchSize() delegates to the underlying ResultSet
    // without additional transformation.
    void getFetchSize_delegates() throws SQLException {
        when(mockResultSet.getFetchSize()).thenReturn(100);
        assertEquals(100, rs.getFetchSize());
        verify(mockResultSet).getFetchSize();
    }

    @Test
    // Check that setFetchDirection(int) delegates to the underlying
    // ResultSet without additional transformation.
    void setFetchDirection_delegates() throws SQLException {
        rs.setFetchDirection(ResultSet.FETCH_FORWARD);
        verify(mockResultSet).setFetchDirection(ResultSet.FETCH_FORWARD);
    }

    @Test
    // Check that setFetchSize(int) delegates to the underlying ResultSet
    // without additional transformation.
    void setFetchSize_delegates() throws SQLException {
        rs.setFetchSize(100);
        verify(mockResultSet).setFetchSize(100);
    }

    // --- wasNull ---

    @Test
    // Check that wasNull() delegates to the underlying ResultSet without
    // additional transformation.
    void wasNull_delegates() throws SQLException {
        when(mockResultSet.wasNull()).thenReturn(true);
        assertTrue(rs.wasNull());
        verify(mockResultSet).wasNull();
    }

    // --- Deprecated methods ---

    @Test
    // Check that getUnicodeStream(int) delegates to the underlying
    // ResultSet without additional transformation.
    @SuppressWarnings("deprecation")
    void getUnicodeStream_delegates() throws SQLException {
        InputStream expected = mock(InputStream.class);
        when(mockResultSet.getUnicodeStream(1)).thenReturn(expected);
        assertSame(expected, rs.getUnicodeStream(1));
        verify(mockResultSet).getUnicodeStream(1);
    }

    @Test
    // Check that getUnicodeStream(String) delegates to the underlying
    // ResultSet without additional transformation.
    @SuppressWarnings("deprecation")
    void getUnicodeStream_byLabel_delegates() throws SQLException {
        InputStream expected = mock(InputStream.class);
        when(mockResultSet.getUnicodeStream("col")).thenReturn(expected);
        assertSame(expected, rs.getUnicodeStream("col"));
        verify(mockResultSet).getUnicodeStream("col");
    }

    @Test
    // Check that getBigDecimal(String, int) delegates to the underlying
    // ResultSet without additional transformation.
    @SuppressWarnings("deprecation")
    void getBigDecimal_byLabelWithScale_delegates() throws SQLException {
        BigDecimal expected = new BigDecimal("123.45");
        when(mockResultSet.getBigDecimal("col", 2)).thenReturn(expected);
        assertEquals(expected, rs.getBigDecimal("col", 2));
        verify(mockResultSet).getBigDecimal("col", 2);
    }

    // --- Row change detection ---

    @Test
    // Check that rowUpdated() delegates to the underlying ResultSet
    // without additional transformation.
    void rowUpdated_delegates() throws SQLException {
        when(mockResultSet.rowUpdated()).thenReturn(true);
        assertTrue(rs.rowUpdated());
        verify(mockResultSet).rowUpdated();
    }

    @Test
    // Check that rowInserted() delegates to the underlying ResultSet
    // without additional transformation.
    void rowInserted_delegates() throws SQLException {
        when(mockResultSet.rowInserted()).thenReturn(true);
        assertTrue(rs.rowInserted());
        verify(mockResultSet).rowInserted();
    }

    @Test
    // Check that rowDeleted() delegates to the underlying ResultSet
    // without additional transformation.
    void rowDeleted_delegates() throws SQLException {
        when(mockResultSet.rowDeleted()).thenReturn(true);
        assertTrue(rs.rowDeleted());
        verify(mockResultSet).rowDeleted();
    }

    // --- Update Ref ---

    @Test
    // Check that updateRef(int, Ref) delegates to the underlying
    // ResultSet without additional transformation.
    void updateRef_delegates() throws SQLException {
        Ref ref = mock(Ref.class);
        rs.updateRef(1, ref);
        verify(mockResultSet).updateRef(1, ref);
    }

    @Test
    // Check that updateRef(String, Ref) delegates to the underlying
    // ResultSet without additional transformation.
    void updateRef_byLabel_delegates() throws SQLException {
        Ref ref = mock(Ref.class);
        rs.updateRef("col", ref);
        verify(mockResultSet).updateRef("col", ref);
    }

    // --- isClosed ---

    @Test
    // Check that isClosed() delegates to the underlying ResultSet
    // without additional transformation.
    void isClosed_delegates() throws SQLException {
        when(mockResultSet.isClosed()).thenReturn(true);
        assertTrue(rs.isClosed());
        verify(mockResultSet).isClosed();
    }

    // --- Close ---

    @Test
    // Check that close() delegates to the underlying ResultSet
    // without additional transformation.
    void close_delegates() throws SQLException {
        rs.close();
        verify(mockResultSet).close();
    }

    // --- Primitive getters by label ---

    @Test
    // Check that getByte(String) delegates to the underlying ResultSet
    // without additional transformation.
    void getByte_byLabel_delegates() throws SQLException {
        when(mockResultSet.getByte("col")).thenReturn((byte) 42);
        assertEquals((byte) 42, rs.getByte("col"));
        verify(mockResultSet).getByte("col");
    }

    @Test
    // Check that getShort(String) delegates to the underlying ResultSet
    // without additional transformation.
    void getShort_byLabel_delegates() throws SQLException {
        when(mockResultSet.getShort("col")).thenReturn((short) 42);
        assertEquals((short) 42, rs.getShort("col"));
        verify(mockResultSet).getShort("col");
    }

    @Test
    // Check that getLong(String) delegates to the underlying ResultSet
    // without additional transformation.
    void getLong_byLabel_delegates() throws SQLException {
        when(mockResultSet.getLong("col")).thenReturn(42L);
        assertEquals(42L, rs.getLong("col"));
        verify(mockResultSet).getLong("col");
    }

    @Test
    // Check that getFloat(String) delegates to the underlying ResultSet
    // without additional transformation.
    void getFloat_byLabel_delegates() throws SQLException {
        when(mockResultSet.getFloat("col")).thenReturn(1.5f);
        assertEquals(1.5f, rs.getFloat("col"), 0.001f);
        verify(mockResultSet).getFloat("col");
    }

    @Test
    // Check that getDouble(String) delegates to the underlying ResultSet
    // without additional transformation.
    void getDouble_byLabel_delegates() throws SQLException {
        when(mockResultSet.getDouble("col")).thenReturn(1.5);
        assertEquals(1.5, rs.getDouble("col"), 0.001);
        verify(mockResultSet).getDouble("col");
    }

    // --- Bytes and BigDecimal by label ---

    @Test
    // Check that getBytes(String) delegates to the underlying ResultSet
    // without additional transformation.
    void getBytes_byLabel_delegates() throws SQLException {
        byte[] expected = {1, 2, 3};
        when(mockResultSet.getBytes("col")).thenReturn(expected);
        assertArrayEquals(expected, rs.getBytes("col"));
        verify(mockResultSet).getBytes("col");
    }

    @Test
    // Check that getBigDecimal(String) delegates to the underlying
    // ResultSet without additional transformation.
    void getBigDecimal_byLabel_delegates() throws SQLException {
        BigDecimal expected = new BigDecimal("123.45");
        when(mockResultSet.getBigDecimal("col")).thenReturn(expected);
        assertEquals(expected, rs.getBigDecimal("col"));
        verify(mockResultSet).getBigDecimal("col");
    }

    // --- Date/Time getters by label ---

    @Test
    // Check that getDate(String) delegates to the underlying ResultSet
    // without additional transformation.
    void getDate_byLabel_delegates() throws SQLException {
        Date expected = new Date(System.currentTimeMillis());
        when(mockResultSet.getDate("col")).thenReturn(expected);
        assertEquals(expected, rs.getDate("col"));
        verify(mockResultSet).getDate("col");
    }

    @Test
    // Check that getTime(String) delegates to the underlying ResultSet
    // without additional transformation.
    void getTime_byLabel_delegates() throws SQLException {
        Time expected = new Time(System.currentTimeMillis());
        when(mockResultSet.getTime("col")).thenReturn(expected);
        assertEquals(expected, rs.getTime("col"));
        verify(mockResultSet).getTime("col");
    }

    @Test
    // Check that getTimestamp(String) delegates to the underlying
    // ResultSet without additional transformation.
    void getTimestamp_byLabel_delegates() throws SQLException {
        Timestamp expected = new Timestamp(System.currentTimeMillis());
        when(mockResultSet.getTimestamp("col")).thenReturn(expected);
        assertEquals(expected, rs.getTimestamp("col"));
        verify(mockResultSet).getTimestamp("col");
    }

    @Test
    // Check that getDate(String, Calendar) delegates to the underlying
    // ResultSet without additional transformation.
    void getDate_byLabelWithCalendar_delegates() throws SQLException {
        Date expected = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        when(mockResultSet.getDate("col", cal)).thenReturn(expected);
        assertEquals(expected, rs.getDate("col", cal));
        verify(mockResultSet).getDate("col", cal);
    }

    @Test
    // Check that getTime(String, Calendar) delegates to the underlying
    // ResultSet without additional transformation.
    void getTime_byLabelWithCalendar_delegates() throws SQLException {
        Time expected = new Time(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        when(mockResultSet.getTime("col", cal)).thenReturn(expected);
        assertEquals(expected, rs.getTime("col", cal));
        verify(mockResultSet).getTime("col", cal);
    }

    @Test
    // Check that getTimestamp(String, Calendar) delegates to the
    // underlying ResultSet without additional transformation.
    void getTimestamp_byLabelWithCalendar_delegates() throws SQLException {
        Timestamp expected = new Timestamp(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        when(mockResultSet.getTimestamp("col", cal)).thenReturn(expected);
        assertEquals(expected, rs.getTimestamp("col", cal));
        verify(mockResultSet).getTimestamp("col", cal);
    }

    // --- Stream getters by label ---

    @Test
    // Check that getAsciiStream(String) delegates to the underlying
    // ResultSet without additional transformation.
    void getAsciiStream_byLabel_delegates() throws SQLException {
        InputStream expected = mock(InputStream.class);
        when(mockResultSet.getAsciiStream("col")).thenReturn(expected);
        assertSame(expected, rs.getAsciiStream("col"));
        verify(mockResultSet).getAsciiStream("col");
    }

    @Test
    // Check that getBinaryStream(String) delegates to the underlying
    // ResultSet without additional transformation.
    void getBinaryStream_byLabel_delegates() throws SQLException {
        InputStream expected = mock(InputStream.class);
        when(mockResultSet.getBinaryStream("col")).thenReturn(expected);
        assertSame(expected, rs.getBinaryStream("col"));
        verify(mockResultSet).getBinaryStream("col");
    }

    @Test
    // Check that getCharacterStream(String) delegates to the underlying
    // ResultSet without additional transformation.
    void getCharacterStream_byLabel_delegates() throws SQLException {
        Reader expected = mock(Reader.class);
        when(mockResultSet.getCharacterStream("col")).thenReturn(expected);
        assertSame(expected, rs.getCharacterStream("col"));
        verify(mockResultSet).getCharacterStream("col");
    }

    // --- Type-specific getters by label ---

    @Test
    // Check that getArray(String) delegates to the underlying ResultSet
    // without additional transformation.
    void getArray_byLabel_delegates() throws SQLException {
        Array expected = mock(Array.class);
        when(mockResultSet.getArray("col")).thenReturn(expected);
        assertSame(expected, rs.getArray("col"));
        verify(mockResultSet).getArray("col");
    }

    @Test
    // Check that getBlob(String) delegates to the underlying ResultSet
    // without additional transformation.
    void getBlob_byLabel_delegates() throws SQLException {
        Blob expected = mock(Blob.class);
        when(mockResultSet.getBlob("col")).thenReturn(expected);
        assertSame(expected, rs.getBlob("col"));
        verify(mockResultSet).getBlob("col");
    }

    @Test
    // Check that getClob(String) delegates to the underlying ResultSet
    // without additional transformation.
    void getClob_byLabel_delegates() throws SQLException {
        Clob expected = mock(Clob.class);
        when(mockResultSet.getClob("col")).thenReturn(expected);
        assertSame(expected, rs.getClob("col"));
        verify(mockResultSet).getClob("col");
    }

    @Test
    // Check that getRef(String) delegates to the underlying ResultSet
    // without additional transformation.
    void getRef_byLabel_delegates() throws SQLException {
        Ref expected = mock(Ref.class);
        when(mockResultSet.getRef("col")).thenReturn(expected);
        assertSame(expected, rs.getRef("col"));
        verify(mockResultSet).getRef("col");
    }

    @Test
    // Check that getRowId(String) delegates to the underlying ResultSet
    // without additional transformation.
    void getRowId_byLabel_delegates() throws SQLException {
        RowId expected = mock(RowId.class);
        when(mockResultSet.getRowId("col")).thenReturn(expected);
        assertSame(expected, rs.getRowId("col"));
        verify(mockResultSet).getRowId("col");
    }

    @Test
    // Check that getSQLXML(String) delegates to the underlying ResultSet
    // without additional transformation.
    void getSQLXML_byLabel_delegates() throws SQLException {
        SQLXML expected = mock(SQLXML.class);
        when(mockResultSet.getSQLXML("col")).thenReturn(expected);
        assertSame(expected, rs.getSQLXML("col"));
        verify(mockResultSet).getSQLXML("col");
    }

    @Test
    // Check that getURL(String) delegates to the underlying ResultSet
    // without additional transformation.
    void getURL_byLabel_delegates() throws Exception {
        URL expected = new URL("http://example.com");
        when(mockResultSet.getURL("col")).thenReturn(expected);
        assertEquals(expected, rs.getURL("col"));
        verify(mockResultSet).getURL("col");
    }

    @Test
    // Check that getNClob(String) delegates to the underlying ResultSet
    // without additional transformation.
    void getNClob_byLabel_delegates() throws SQLException {
        NClob expected = mock(NClob.class);
        when(mockResultSet.getNClob("col")).thenReturn(expected);
        assertSame(expected, rs.getNClob("col"));
        verify(mockResultSet).getNClob("col");
    }

    @Test
    // Check that getNString(String) delegates to the underlying ResultSet
    // without additional transformation.
    void getNString_byLabel_delegates() throws SQLException {
        when(mockResultSet.getNString("col")).thenReturn("hello");
        assertEquals("hello", rs.getNString("col"));
        verify(mockResultSet).getNString("col");
    }

    @Test
    // Check that getNCharacterStream(String) delegates to the underlying
    // ResultSet without additional transformation.
    void getNCharacterStream_byLabel_delegates() throws SQLException {
        Reader expected = mock(Reader.class);
        when(mockResultSet.getNCharacterStream("col")).thenReturn(expected);
        assertSame(expected, rs.getNCharacterStream("col"));
        verify(mockResultSet).getNCharacterStream("col");
    }

    // --- Update methods by label ---

    @Test
    // Check that updateNull(String) delegates to the underlying ResultSet
    // without additional transformation.
    void updateNull_byLabel_delegates() throws SQLException {
        rs.updateNull("col");
        verify(mockResultSet).updateNull("col");
    }

    @Test
    // Check that updateBoolean(String, boolean) delegates to the
    // underlying ResultSet without additional transformation.
    void updateBoolean_byLabel_delegates() throws SQLException {
        rs.updateBoolean("col", true);
        verify(mockResultSet).updateBoolean("col", true);
    }

    @Test
    // Check that updateByte(String, byte) delegates to the underlying
    // ResultSet without additional transformation.
    void updateByte_byLabel_delegates() throws SQLException {
        rs.updateByte("col", (byte) 42);
        verify(mockResultSet).updateByte("col", (byte) 42);
    }

    @Test
    // Check that updateShort(String, short) delegates to the underlying
    // ResultSet without additional transformation.
    void updateShort_byLabel_delegates() throws SQLException {
        rs.updateShort("col", (short) 42);
        verify(mockResultSet).updateShort("col", (short) 42);
    }

    @Test
    // Check that updateInt(String, int) delegates to the underlying
    // ResultSet without additional transformation.
    void updateInt_byLabel_delegates() throws SQLException {
        rs.updateInt("col", 42);
        verify(mockResultSet).updateInt("col", 42);
    }

    @Test
    // Check that updateLong(String, long) delegates to the underlying
    // ResultSet without additional transformation.
    void updateLong_byLabel_delegates() throws SQLException {
        rs.updateLong("col", 42L);
        verify(mockResultSet).updateLong("col", 42L);
    }

    @Test
    // Check that updateFloat(String, float) delegates to the underlying
    // ResultSet without additional transformation.
    void updateFloat_byLabel_delegates() throws SQLException {
        rs.updateFloat("col", 1.5f);
        verify(mockResultSet).updateFloat("col", 1.5f);
    }

    @Test
    // Check that updateDouble(String, double) delegates to the underlying
    // ResultSet without additional transformation.
    void updateDouble_byLabel_delegates() throws SQLException {
        rs.updateDouble("col", 1.5);
        verify(mockResultSet).updateDouble("col", 1.5);
    }

    @Test
    // Check that updateBigDecimal(String, BigDecimal) delegates to the
    // underlying ResultSet without additional transformation.
    void updateBigDecimal_byLabel_delegates() throws SQLException {
        BigDecimal value = new BigDecimal("123.45");
        rs.updateBigDecimal("col", value);
        verify(mockResultSet).updateBigDecimal("col", value);
    }

    @Test
    // Check that updateString(String, String) delegates to the underlying
    // ResultSet without additional transformation.
    void updateString_byLabel_delegates() throws SQLException {
        rs.updateString("col", "hello");
        verify(mockResultSet).updateString("col", "hello");
    }

    @Test
    // Check that updateBytes(String, byte[]) delegates to the underlying
    // ResultSet without additional transformation.
    void updateBytes_byLabel_delegates() throws SQLException {
        rs.updateBytes("col", new byte[] {1, 2, 3});
        verify(mockResultSet).updateBytes("col", new byte[] {1, 2, 3});
    }

    @Test
    // Check that updateDate(String, Date) delegates to the underlying
    // ResultSet without additional transformation.
    void updateDate_byLabel_delegates() throws SQLException {
        Date value = new Date(System.currentTimeMillis());
        rs.updateDate("col", value);
        verify(mockResultSet).updateDate("col", value);
    }

    @Test
    // Check that updateTime(String, Time) delegates to the underlying
    // ResultSet without additional transformation.
    void updateTime_byLabel_delegates() throws SQLException {
        Time value = new Time(System.currentTimeMillis());
        rs.updateTime("col", value);
        verify(mockResultSet).updateTime("col", value);
    }

    @Test
    // Check that updateTimestamp(String, Timestamp) delegates to the
    // underlying ResultSet without additional transformation.
    void updateTimestamp_byLabel_delegates() throws SQLException {
        Timestamp value = new Timestamp(System.currentTimeMillis());
        rs.updateTimestamp("col", value);
        verify(mockResultSet).updateTimestamp("col", value);
    }

    @Test
    // Check that updateBlob(String, Blob) delegates to the underlying
    // ResultSet without additional transformation.
    void updateBlob_byLabel_delegates() throws SQLException {
        Blob blob = mock(Blob.class);
        rs.updateBlob("col", blob);
        verify(mockResultSet).updateBlob("col", blob);
    }

    @Test
    // Check that updateClob(String, Clob) delegates to the underlying
    // ResultSet without additional transformation.
    void updateClob_byLabel_delegates() throws SQLException {
        Clob clob = mock(Clob.class);
        rs.updateClob("col", clob);
        verify(mockResultSet).updateClob("col", clob);
    }

    @Test
    // Check that updateArray(String, Array) delegates to the underlying
    // ResultSet without additional transformation.
    void updateArray_byLabel_delegates() throws SQLException {
        Array array = mock(Array.class);
        rs.updateArray("col", array);
        verify(mockResultSet).updateArray("col", array);
    }

    @Test
    // Check that updateRowId(String, RowId) delegates to the underlying
    // ResultSet without additional transformation.
    void updateRowId_byLabel_delegates() throws SQLException {
        RowId rowId = mock(RowId.class);
        rs.updateRowId("col", rowId);
        verify(mockResultSet).updateRowId("col", rowId);
    }

    @Test
    // Check that updateSQLXML(String, SQLXML) delegates to the underlying
    // ResultSet without additional transformation.
    void updateSQLXML_byLabel_delegates() throws SQLException {
        SQLXML xml = mock(SQLXML.class);
        rs.updateSQLXML("col", xml);
        verify(mockResultSet).updateSQLXML("col", xml);
    }

    @Test
    // Check that updateNString(String, String) delegates to the
    // underlying ResultSet without additional transformation.
    void updateNString_byLabel_delegates() throws SQLException {
        rs.updateNString("col", "hello");
        verify(mockResultSet).updateNString("col", "hello");
    }

    @Test
    // Check that updateNClob(String, NClob) delegates to the underlying
    // ResultSet without additional transformation.
    void updateNClob_byLabel_delegates() throws SQLException {
        NClob nclob = mock(NClob.class);
        rs.updateNClob("col", nclob);
        verify(mockResultSet).updateNClob("col", nclob);
    }

    // --- Update stream methods with long length ---

    @Test
    // Check that updateAsciiStream(int, InputStream, long) delegates to
    // the underlying ResultSet without additional transformation.
    void updateAsciiStream_withLongLength_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        rs.updateAsciiStream(1, stream, 100L);
        verify(mockResultSet).updateAsciiStream(1, stream, 100L);
    }

    @Test
    // Check that updateBinaryStream(int, InputStream, long) delegates to
    // the underlying ResultSet without additional transformation.
    void updateBinaryStream_withLongLength_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        rs.updateBinaryStream(1, stream, 100L);
        verify(mockResultSet).updateBinaryStream(1, stream, 100L);
    }

    @Test
    // Check that updateCharacterStream(int, Reader, long) delegates to
    // the underlying ResultSet without additional transformation.
    void updateCharacterStream_withLongLength_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        rs.updateCharacterStream(1, reader, 100L);
        verify(mockResultSet).updateCharacterStream(1, reader, 100L);
    }

    @Test
    // Check that updateAsciiStream(String, InputStream, long) delegates
    // to the underlying ResultSet without additional transformation.
    void updateAsciiStream_byLabelWithLongLength_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        rs.updateAsciiStream("col", stream, 100L);
        verify(mockResultSet).updateAsciiStream("col", stream, 100L);
    }

    @Test
    // Check that updateBinaryStream(String, InputStream, long) delegates
    // to the underlying ResultSet without additional transformation.
    void updateBinaryStream_byLabelWithLongLength_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        rs.updateBinaryStream("col", stream, 100L);
        verify(mockResultSet).updateBinaryStream("col", stream, 100L);
    }

    @Test
    // Check that updateCharacterStream(String, Reader, long) delegates
    // to the underlying ResultSet without additional transformation.
    void updateCharacterStream_byLabelWithLongLength_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        rs.updateCharacterStream("col", reader, 100L);
        verify(mockResultSet).updateCharacterStream("col", reader, 100L);
    }

    // --- Update NCharacterStream ---

    @Test
    // Check that updateNCharacterStream(int, Reader, long) delegates to
    // the underlying ResultSet without additional transformation.
    void updateNCharacterStream_withLongLength_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        rs.updateNCharacterStream(1, reader, 100L);
        verify(mockResultSet).updateNCharacterStream(1, reader, 100L);
    }

    @Test
    // Check that updateNCharacterStream(String, Reader, long) delegates
    // to the underlying ResultSet without additional transformation.
    void updateNCharacterStream_byLabelWithLongLength_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        rs.updateNCharacterStream("col", reader, 100L);
        verify(mockResultSet).updateNCharacterStream("col", reader, 100L);
    }

    @Test
    // Check that updateNCharacterStream(int, Reader) delegates to the
    // underlying ResultSet without additional transformation.
    void updateNCharacterStream_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        rs.updateNCharacterStream(1, reader);
        verify(mockResultSet).updateNCharacterStream(1, reader);
    }

    @Test
    // Check that updateNCharacterStream(String, Reader) delegates to the
    // underlying ResultSet without additional transformation.
    void updateNCharacterStream_byLabel_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        rs.updateNCharacterStream("col", reader);
        verify(mockResultSet).updateNCharacterStream("col", reader);
    }

    // --- Update stream methods without length ---

    @Test
    // Check that updateAsciiStream(int, InputStream) delegates to the
    // underlying ResultSet without additional transformation.
    void updateAsciiStream_withoutLength_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        rs.updateAsciiStream(1, stream);
        verify(mockResultSet).updateAsciiStream(1, stream);
    }

    @Test
    // Check that updateBinaryStream(int, InputStream) delegates to the
    // underlying ResultSet without additional transformation.
    void updateBinaryStream_withoutLength_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        rs.updateBinaryStream(1, stream);
        verify(mockResultSet).updateBinaryStream(1, stream);
    }

    @Test
    // Check that updateCharacterStream(int, Reader) delegates to the
    // underlying ResultSet without additional transformation.
    void updateCharacterStream_withoutLength_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        rs.updateCharacterStream(1, reader);
        verify(mockResultSet).updateCharacterStream(1, reader);
    }

    @Test
    // Check that updateAsciiStream(String, InputStream) delegates to the
    // underlying ResultSet without additional transformation.
    void updateAsciiStream_byLabelWithoutLength_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        rs.updateAsciiStream("col", stream);
        verify(mockResultSet).updateAsciiStream("col", stream);
    }

    @Test
    // Check that updateAsciiStream(String, InputStream, int) delegates to
    // the underlying ResultSet without additional transformation.
    void updateAsciiStream_byLabelWithIntLength_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        rs.updateAsciiStream("col", stream, 100);
        verify(mockResultSet).updateAsciiStream("col", stream, 100);
    }

    @Test
    // Check that updateBinaryStream(String, InputStream) delegates to the
    // underlying ResultSet without additional transformation.
    void updateBinaryStream_byLabelWithoutLength_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        rs.updateBinaryStream("col", stream);
        verify(mockResultSet).updateBinaryStream("col", stream);
    }

    @Test
    // Check that updateBinaryStream(String, InputStream, int) delegates to
    // the underlying ResultSet without additional transformation.
    void updateBinaryStream_byLabelWithIntLength_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        rs.updateBinaryStream("col", stream, 100);
        verify(mockResultSet).updateBinaryStream("col", stream, 100);
    }

    @Test
    // Check that updateCharacterStream(String, Reader) delegates to the
    // underlying ResultSet without additional transformation.
    void updateCharacterStream_byLabelWithoutLength_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        rs.updateCharacterStream("col", reader);
        verify(mockResultSet).updateCharacterStream("col", reader);
    }

    @Test
    // Check that updateCharacterStream(String, Reader, int) delegates to
    // the underlying ResultSet without additional transformation.
    void updateCharacterStream_byLabelWithIntLength_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        rs.updateCharacterStream("col", reader, 100);
        verify(mockResultSet).updateCharacterStream("col", reader, 100);
    }

    // --- Update Blob/Clob/NClob with InputStream/Reader ---

    @Test
    // Check that updateBlob(int, InputStream, long) delegates to the
    // underlying ResultSet without additional transformation.
    void updateBlob_withInputStreamAndLong_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        rs.updateBlob(1, stream, 100L);
        verify(mockResultSet).updateBlob(1, stream, 100L);
    }

    @Test
    // Check that updateBlob(String, InputStream, long) delegates to the
    // underlying ResultSet without additional transformation.
    void updateBlob_byLabelWithInputStreamAndLong_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        rs.updateBlob("col", stream, 100L);
        verify(mockResultSet).updateBlob("col", stream, 100L);
    }

    @Test
    // Check that updateClob(int, Reader, long) delegates to the
    // underlying ResultSet without additional transformation.
    void updateClob_withReaderAndLong_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        rs.updateClob(1, reader, 100L);
        verify(mockResultSet).updateClob(1, reader, 100L);
    }

    @Test
    // Check that updateClob(String, Reader, long) delegates to the
    // underlying ResultSet without additional transformation.
    void updateClob_byLabelWithReaderAndLong_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        rs.updateClob("col", reader, 100L);
        verify(mockResultSet).updateClob("col", reader, 100L);
    }

    @Test
    // Check that updateNClob(int, Reader, long) delegates to the
    // underlying ResultSet without additional transformation.
    void updateNClob_withReaderAndLong_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        rs.updateNClob(1, reader, 100L);
        verify(mockResultSet).updateNClob(1, reader, 100L);
    }

    @Test
    // Check that updateNClob(String, Reader, long) delegates to the
    // underlying ResultSet without additional transformation.
    void updateNClob_byLabelWithReaderAndLong_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        rs.updateNClob("col", reader, 100L);
        verify(mockResultSet).updateNClob("col", reader, 100L);
    }

    @Test
    // Check that updateBlob(int, InputStream) delegates to the
    // underlying ResultSet without additional transformation.
    void updateBlob_withInputStream_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        rs.updateBlob(1, stream);
        verify(mockResultSet).updateBlob(1, stream);
    }

    @Test
    // Check that updateBlob(String, InputStream) delegates to the
    // underlying ResultSet without additional transformation.
    void updateBlob_byLabelWithInputStream_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        rs.updateBlob("col", stream);
        verify(mockResultSet).updateBlob("col", stream);
    }

    @Test
    // Check that updateClob(int, Reader) delegates to the underlying
    // ResultSet without additional transformation.
    void updateClob_withReader_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        rs.updateClob(1, reader);
        verify(mockResultSet).updateClob(1, reader);
    }

    @Test
    // Check that updateClob(String, Reader) delegates to the underlying
    // ResultSet without additional transformation.
    void updateClob_byLabelWithReader_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        rs.updateClob("col", reader);
        verify(mockResultSet).updateClob("col", reader);
    }

    @Test
    // Check that updateNClob(int, Reader) delegates to the underlying
    // ResultSet without additional transformation.
    void updateNClob_withReader_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        rs.updateNClob(1, reader);
        verify(mockResultSet).updateNClob(1, reader);
    }

    @Test
    // Check that updateNClob(String, Reader) delegates to the underlying
    // ResultSet without additional transformation.
    void updateNClob_byLabelWithReader_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        rs.updateNClob("col", reader);
        verify(mockResultSet).updateNClob("col", reader);
    }

    // --- Update Object with SQLType ---

    @Test
    // Check that updateObject(int, Object, SQLType, int) delegates to
    // the underlying ResultSet after calling registry.toSql().
    void updateObject_withSqlTypeAndScale_delegates() throws SQLException {
        SQLType sqlType = JDBCType.VARCHAR;
        rs.updateObject(1, "value", sqlType, 10);
        verify(mockResultSet).updateObject(eq(1), any(), eq(sqlType), eq(10));
    }

    @Test
    // Check that updateObject(String, Object, SQLType, int) delegates to
    // the underlying ResultSet after calling registry.toSql().
    void updateObject_byLabelWithSqlTypeAndScale_delegates() throws SQLException {
        SQLType sqlType = JDBCType.VARCHAR;
        rs.updateObject("col", "value", sqlType, 10);
        verify(mockResultSet).updateObject(eq("col"), any(), eq(sqlType), eq(10));
    }

    @Test
    // Check that updateObject(int, Object, SQLType) delegates to the
    // underlying ResultSet after calling registry.toSql().
    void updateObject_withSqlType_delegates() throws SQLException {
        SQLType sqlType = JDBCType.VARCHAR;
        rs.updateObject(1, "value", sqlType);
        verify(mockResultSet).updateObject(eq(1), any(), eq(sqlType));
    }

    @Test
    // Check that updateObject(String, Object, SQLType) delegates to the
    // underlying ResultSet after calling registry.toSql().
    void updateObject_byLabelWithSqlType_delegates() throws SQLException {
        SQLType sqlType = JDBCType.VARCHAR;
        rs.updateObject("col", "value", sqlType);
        verify(mockResultSet).updateObject(eq("col"), any(), eq(sqlType));
    }

    // --- Unwrap ---

    @Test
    // Check that unwrap(Class) returns the underlying ResultSet when
    // it implements the requested type.
    void unwrap_returnsUnderlyingWhenInstance() throws SQLException {
        assertSame(mockResultSet, rs.unwrap(ResultSet.class));
    }

    @Test
    // Check that isWrapperFor(Class) returns true when the underlying
    // ResultSet implements the requested type.
    void isWrapperFor_returnsTrueWhenInstance() throws SQLException {
        assertTrue(rs.isWrapperFor(ResultSet.class));
    }
}
