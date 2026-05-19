package fr.emcastro.jdbcretyper.jdbc.delegation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
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

import fr.emcastro.jdbcretyper.jdbc.RetyperCallableStatement;
import fr.emcastro.jdbcretyper.jdbc.RetyperConnection;
import fr.emcastro.jdbcretyper.transform.TypeTransformerRegistry;

@ExtendWith(MockitoExtension.class)
class RetyperCallableStatementDelegationTest {

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

    // --- registerOutParameter ---

    @Test
    // Check that registerOutParameter(int, int) delegates to the
    // underlying CallableStatement without additional transformation.
    void registerOutParameter_int_delegates() throws SQLException {
        statement.registerOutParameter(1, 12);
        verify(mockCallableStatement).registerOutParameter(1, 12);
    }

    @Test
    // Check that registerOutParameter(int, int, int) delegates to the
    // underlying CallableStatement without additional transformation.
    void registerOutParameter_intWithScale_delegates() throws SQLException {
        statement.registerOutParameter(1, 12, 2);
        verify(mockCallableStatement).registerOutParameter(1, 12, 2);
    }

    @Test
    // Check that registerOutParameter(int, int, String) delegates to
    // the underlying CallableStatement without additional transformation.
    void registerOutParameter_intWithTypeName_delegates() throws SQLException {
        statement.registerOutParameter(1, 12, "VARCHAR");
        verify(mockCallableStatement).registerOutParameter(1, 12, "VARCHAR");
    }

    @Test
    // Check that registerOutParameter(String, int) delegates to the
    // underlying CallableStatement without additional transformation.
    void registerOutParameter_string_delegates() throws SQLException {
        statement.registerOutParameter("param", 12);
        verify(mockCallableStatement).registerOutParameter("param", 12);
    }

    @Test
    // Check that registerOutParameter(String, int, int) delegates to
    // the underlying CallableStatement without additional transformation.
    void registerOutParameter_stringWithScale_delegates() throws SQLException {
        statement.registerOutParameter("param", 12, 2);
        verify(mockCallableStatement).registerOutParameter("param", 12, 2);
    }

    @Test
    // Check that registerOutParameter(String, int, String) delegates to
    // the underlying CallableStatement without additional transformation.
    void registerOutParameter_stringWithTypeName_delegates() throws SQLException {
        statement.registerOutParameter("param", 12, "VARCHAR");
        verify(mockCallableStatement).registerOutParameter("param", 12, "VARCHAR");
    }

    // --- wasNull ---

    @Test
    // Check that wasNull() delegates to the underlying CallableStatement
    // without additional transformation.
    void wasNull_delegates() throws SQLException {
        when(mockCallableStatement.wasNull()).thenReturn(true);
        assertTrue(statement.wasNull());
        verify(mockCallableStatement).wasNull();
    }

    // --- Primitive getters ---

    @Test
    // Check that getString(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getString_int_delegates() throws SQLException {
        when(mockCallableStatement.getString(1)).thenReturn("hello");
        assertEquals("hello", statement.getString(1));
        verify(mockCallableStatement).getString(1);
    }

    @Test
    // Check that getString(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getString_string_delegates() throws SQLException {
        when(mockCallableStatement.getString("param")).thenReturn("hello");
        assertEquals("hello", statement.getString("param"));
        verify(mockCallableStatement).getString("param");
    }

    @Test
    // Check that getBoolean(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getBoolean_int_delegates() throws SQLException {
        when(mockCallableStatement.getBoolean(1)).thenReturn(true);
        assertTrue(statement.getBoolean(1));
        verify(mockCallableStatement).getBoolean(1);
    }

    @Test
    // Check that getBoolean(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getBoolean_string_delegates() throws SQLException {
        when(mockCallableStatement.getBoolean("param")).thenReturn(true);
        assertTrue(statement.getBoolean("param"));
        verify(mockCallableStatement).getBoolean("param");
    }

    @Test
    // Check that getByte(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getByte_int_delegates() throws SQLException {
        when(mockCallableStatement.getByte(1)).thenReturn((byte) 42);
        assertEquals((byte) 42, statement.getByte(1));
        verify(mockCallableStatement).getByte(1);
    }

    @Test
    // Check that getByte(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getByte_string_delegates() throws SQLException {
        when(mockCallableStatement.getByte("param")).thenReturn((byte) 42);
        assertEquals((byte) 42, statement.getByte("param"));
        verify(mockCallableStatement).getByte("param");
    }

    @Test
    // Check that getShort(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getShort_int_delegates() throws SQLException {
        when(mockCallableStatement.getShort(1)).thenReturn((short) 42);
        assertEquals((short) 42, statement.getShort(1));
        verify(mockCallableStatement).getShort(1);
    }

    @Test
    // Check that getShort(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getShort_string_delegates() throws SQLException {
        when(mockCallableStatement.getShort("param")).thenReturn((short) 42);
        assertEquals((short) 42, statement.getShort("param"));
        verify(mockCallableStatement).getShort("param");
    }

    @Test
    // Check that getInt(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getInt_int_delegates() throws SQLException {
        when(mockCallableStatement.getInt(1)).thenReturn(42);
        assertEquals(42, statement.getInt(1));
        verify(mockCallableStatement).getInt(1);
    }

    @Test
    // Check that getInt(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getInt_string_delegates() throws SQLException {
        when(mockCallableStatement.getInt("param")).thenReturn(42);
        assertEquals(42, statement.getInt("param"));
        verify(mockCallableStatement).getInt("param");
    }

    @Test
    // Check that getLong(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getLong_int_delegates() throws SQLException {
        when(mockCallableStatement.getLong(1)).thenReturn(42L);
        assertEquals(42L, statement.getLong(1));
        verify(mockCallableStatement).getLong(1);
    }

    @Test
    // Check that getLong(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getLong_string_delegates() throws SQLException {
        when(mockCallableStatement.getLong("param")).thenReturn(42L);
        assertEquals(42L, statement.getLong("param"));
        verify(mockCallableStatement).getLong("param");
    }

    @Test
    // Check that getFloat(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getFloat_int_delegates() throws SQLException {
        when(mockCallableStatement.getFloat(1)).thenReturn(1.5f);
        assertEquals(1.5f, statement.getFloat(1), 0.001f);
        verify(mockCallableStatement).getFloat(1);
    }

    @Test
    // Check that getFloat(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getFloat_string_delegates() throws SQLException {
        when(mockCallableStatement.getFloat("param")).thenReturn(1.5f);
        assertEquals(1.5f, statement.getFloat("param"), 0.001f);
        verify(mockCallableStatement).getFloat("param");
    }

    @Test
    // Check that getDouble(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getDouble_int_delegates() throws SQLException {
        when(mockCallableStatement.getDouble(1)).thenReturn(1.5);
        assertEquals(1.5, statement.getDouble(1), 0.001);
        verify(mockCallableStatement).getDouble(1);
    }

    @Test
    // Check that getDouble(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getDouble_string_delegates() throws SQLException {
        when(mockCallableStatement.getDouble("param")).thenReturn(1.5);
        assertEquals(1.5, statement.getDouble("param"), 0.001);
        verify(mockCallableStatement).getDouble("param");
    }

    @Test
    // Check that getBigDecimal(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getBigDecimal_int_delegates() throws SQLException {
        BigDecimal expected = new BigDecimal("123.45");
        when(mockCallableStatement.getBigDecimal(1)).thenReturn(expected);
        assertEquals(expected, statement.getBigDecimal(1));
        verify(mockCallableStatement).getBigDecimal(1);
    }

    @Test
    // Check that getBigDecimal(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getBigDecimal_string_delegates() throws SQLException {
        BigDecimal expected = new BigDecimal("123.45");
        when(mockCallableStatement.getBigDecimal("param")).thenReturn(expected);
        assertEquals(expected, statement.getBigDecimal("param"));
        verify(mockCallableStatement).getBigDecimal("param");
    }

    // --- Date/Time getters ---

    @Test
    // Check that getDate(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getDate_int_delegates() throws SQLException {
        Date expected = new Date(System.currentTimeMillis());
        when(mockCallableStatement.getDate(1)).thenReturn(expected);
        assertEquals(expected, statement.getDate(1));
        verify(mockCallableStatement).getDate(1);
    }

    @Test
    // Check that getDate(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getDate_string_delegates() throws SQLException {
        Date expected = new Date(System.currentTimeMillis());
        when(mockCallableStatement.getDate("param")).thenReturn(expected);
        assertEquals(expected, statement.getDate("param"));
        verify(mockCallableStatement).getDate("param");
    }

    @Test
    // Check that getTime(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getTime_int_delegates() throws SQLException {
        Time expected = new Time(System.currentTimeMillis());
        when(mockCallableStatement.getTime(1)).thenReturn(expected);
        assertEquals(expected, statement.getTime(1));
        verify(mockCallableStatement).getTime(1);
    }

    @Test
    // Check that getTime(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getTime_string_delegates() throws SQLException {
        Time expected = new Time(System.currentTimeMillis());
        when(mockCallableStatement.getTime("param")).thenReturn(expected);
        assertEquals(expected, statement.getTime("param"));
        verify(mockCallableStatement).getTime("param");
    }

    @Test
    // Check that getTimestamp(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getTimestamp_int_delegates() throws SQLException {
        Timestamp expected = new Timestamp(System.currentTimeMillis());
        when(mockCallableStatement.getTimestamp(1)).thenReturn(expected);
        assertEquals(expected, statement.getTimestamp(1));
        verify(mockCallableStatement).getTimestamp(1);
    }

    @Test
    // Check that getTimestamp(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getTimestamp_string_delegates() throws SQLException {
        Timestamp expected = new Timestamp(System.currentTimeMillis());
        when(mockCallableStatement.getTimestamp("param")).thenReturn(expected);
        assertEquals(expected, statement.getTimestamp("param"));
        verify(mockCallableStatement).getTimestamp("param");
    }

    // --- Type-specific getters ---

    @Test
    // Check that getArray(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getArray_int_delegates() throws SQLException {
        Array expected = mock(Array.class);
        when(mockCallableStatement.getArray(1)).thenReturn(expected);
        assertSame(expected, statement.getArray(1));
        verify(mockCallableStatement).getArray(1);
    }

    @Test
    // Check that getArray(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getArray_string_delegates() throws SQLException {
        Array expected = mock(Array.class);
        when(mockCallableStatement.getArray("param")).thenReturn(expected);
        assertSame(expected, statement.getArray("param"));
        verify(mockCallableStatement).getArray("param");
    }

    @Test
    // Check that getBlob(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getBlob_int_delegates() throws SQLException {
        Blob expected = mock(Blob.class);
        when(mockCallableStatement.getBlob(1)).thenReturn(expected);
        assertSame(expected, statement.getBlob(1));
        verify(mockCallableStatement).getBlob(1);
    }

    @Test
    // Check that getBlob(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getBlob_string_delegates() throws SQLException {
        Blob expected = mock(Blob.class);
        when(mockCallableStatement.getBlob("param")).thenReturn(expected);
        assertSame(expected, statement.getBlob("param"));
        verify(mockCallableStatement).getBlob("param");
    }

    @Test
    // Check that getClob(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getClob_int_delegates() throws SQLException {
        Clob expected = mock(Clob.class);
        when(mockCallableStatement.getClob(1)).thenReturn(expected);
        assertSame(expected, statement.getClob(1));
        verify(mockCallableStatement).getClob(1);
    }

    @Test
    // Check that getClob(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getClob_string_delegates() throws SQLException {
        Clob expected = mock(Clob.class);
        when(mockCallableStatement.getClob("param")).thenReturn(expected);
        assertSame(expected, statement.getClob("param"));
        verify(mockCallableStatement).getClob("param");
    }

    @Test
    // Check that getRef(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getRef_int_delegates() throws SQLException {
        Ref expected = mock(Ref.class);
        when(mockCallableStatement.getRef(1)).thenReturn(expected);
        assertSame(expected, statement.getRef(1));
        verify(mockCallableStatement).getRef(1);
    }

    @Test
    // Check that getRef(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getRef_string_delegates() throws SQLException {
        Ref expected = mock(Ref.class);
        when(mockCallableStatement.getRef("param")).thenReturn(expected);
        assertSame(expected, statement.getRef("param"));
        verify(mockCallableStatement).getRef("param");
    }

    @Test
    // Check that getRowId(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getRowId_int_delegates() throws SQLException {
        RowId expected = mock(RowId.class);
        when(mockCallableStatement.getRowId(1)).thenReturn(expected);
        assertSame(expected, statement.getRowId(1));
        verify(mockCallableStatement).getRowId(1);
    }

    @Test
    // Check that getRowId(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getRowId_string_delegates() throws SQLException {
        RowId expected = mock(RowId.class);
        when(mockCallableStatement.getRowId("param")).thenReturn(expected);
        assertSame(expected, statement.getRowId("param"));
        verify(mockCallableStatement).getRowId("param");
    }

    @Test
    // Check that getSQLXML(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getSQLXML_int_delegates() throws SQLException {
        SQLXML expected = mock(SQLXML.class);
        when(mockCallableStatement.getSQLXML(1)).thenReturn(expected);
        assertSame(expected, statement.getSQLXML(1));
        verify(mockCallableStatement).getSQLXML(1);
    }

    @Test
    // Check that getSQLXML(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getSQLXML_string_delegates() throws SQLException {
        SQLXML expected = mock(SQLXML.class);
        when(mockCallableStatement.getSQLXML("param")).thenReturn(expected);
        assertSame(expected, statement.getSQLXML("param"));
        verify(mockCallableStatement).getSQLXML("param");
    }

    @Test
    // Check that getURL(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getURL_int_delegates() throws Exception {
        URL expected = new URL("http://example.com");
        when(mockCallableStatement.getURL(1)).thenReturn(expected);
        assertEquals(expected, statement.getURL(1));
        verify(mockCallableStatement).getURL(1);
    }

    @Test
    // Check that getURL(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getURL_string_delegates() throws Exception {
        URL expected = new URL("http://example.com");
        when(mockCallableStatement.getURL("param")).thenReturn(expected);
        assertEquals(expected, statement.getURL("param"));
        verify(mockCallableStatement).getURL("param");
    }

    // --- NString/NClob getters ---

    @Test
    // Check that getNString(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getNString_int_delegates() throws SQLException {
        when(mockCallableStatement.getNString(1)).thenReturn("hello");
        assertEquals("hello", statement.getNString(1));
        verify(mockCallableStatement).getNString(1);
    }

    @Test
    // Check that getNString(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getNString_string_delegates() throws SQLException {
        when(mockCallableStatement.getNString("param")).thenReturn("hello");
        assertEquals("hello", statement.getNString("param"));
        verify(mockCallableStatement).getNString("param");
    }

    @Test
    // Check that getNClob(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getNClob_int_delegates() throws SQLException {
        NClob expected = mock(NClob.class);
        when(mockCallableStatement.getNClob(1)).thenReturn(expected);
        assertSame(expected, statement.getNClob(1));
        verify(mockCallableStatement).getNClob(1);
    }

    @Test
    // Check that getNClob(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getNClob_string_delegates() throws SQLException {
        NClob expected = mock(NClob.class);
        when(mockCallableStatement.getNClob("param")).thenReturn(expected);
        assertSame(expected, statement.getNClob("param"));
        verify(mockCallableStatement).getNClob("param");
    }

    @Test
    // Check that getNCharacterStream(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getNCharacterStream_int_delegates() throws SQLException {
        Reader expected = mock(Reader.class);
        when(mockCallableStatement.getNCharacterStream(1)).thenReturn(expected);
        assertSame(expected, statement.getNCharacterStream(1));
        verify(mockCallableStatement).getNCharacterStream(1);
    }

    @Test
    // Check that getNCharacterStream(String) delegates to the
    // underlying CallableStatement without additional transformation.
    void getNCharacterStream_string_delegates() throws SQLException {
        Reader expected = mock(Reader.class);
        when(mockCallableStatement.getNCharacterStream("param")).thenReturn(expected);
        assertSame(expected, statement.getNCharacterStream("param"));
        verify(mockCallableStatement).getNCharacterStream("param");
    }

    // --- Bytes getter ---

    @Test
    // Check that getBytes(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getBytes_int_delegates() throws SQLException {
        byte[] expected = {1, 2, 3};
        when(mockCallableStatement.getBytes(1)).thenReturn(expected);
        assertArrayEquals(expected, statement.getBytes(1));
        verify(mockCallableStatement).getBytes(1);
    }

    @Test
    // Check that getBytes(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getBytes_string_delegates() throws SQLException {
        byte[] expected = {1, 2, 3};
        when(mockCallableStatement.getBytes("param")).thenReturn(expected);
        assertArrayEquals(expected, statement.getBytes("param"));
        verify(mockCallableStatement).getBytes("param");
    }

    // --- Calendar-based getters ---

    @Test
    // Check that getDate(int, Calendar) delegates to the underlying
    // CallableStatement without additional transformation.
    void getDate_intWithCalendar_delegates() throws SQLException {
        Date expected = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        when(mockCallableStatement.getDate(1, cal)).thenReturn(expected);
        assertEquals(expected, statement.getDate(1, cal));
        verify(mockCallableStatement).getDate(1, cal);
    }

    @Test
    // Check that getDate(String, Calendar) delegates to the underlying
    // CallableStatement without additional transformation.
    void getDate_stringWithCalendar_delegates() throws SQLException {
        Date expected = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        when(mockCallableStatement.getDate("param", cal)).thenReturn(expected);
        assertEquals(expected, statement.getDate("param", cal));
        verify(mockCallableStatement).getDate("param", cal);
    }

    @Test
    // Check that getTime(int, Calendar) delegates to the underlying
    // CallableStatement without additional transformation.
    void getTime_intWithCalendar_delegates() throws SQLException {
        Time expected = new Time(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        when(mockCallableStatement.getTime(1, cal)).thenReturn(expected);
        assertEquals(expected, statement.getTime(1, cal));
        verify(mockCallableStatement).getTime(1, cal);
    }

    @Test
    // Check that getTime(String, Calendar) delegates to the underlying
    // CallableStatement without additional transformation.
    void getTime_stringWithCalendar_delegates() throws SQLException {
        Time expected = new Time(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        when(mockCallableStatement.getTime("param", cal)).thenReturn(expected);
        assertEquals(expected, statement.getTime("param", cal));
        verify(mockCallableStatement).getTime("param", cal);
    }

    @Test
    // Check that getTimestamp(int, Calendar) delegates to the
    // underlying CallableStatement without additional transformation.
    void getTimestamp_intWithCalendar_delegates() throws SQLException {
        Timestamp expected = new Timestamp(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        when(mockCallableStatement.getTimestamp(1, cal)).thenReturn(expected);
        assertEquals(expected, statement.getTimestamp(1, cal));
        verify(mockCallableStatement).getTimestamp(1, cal);
    }

    @Test
    // Check that getTimestamp(String, Calendar) delegates to the
    // underlying CallableStatement without additional transformation.
    void getTimestamp_stringWithCalendar_delegates() throws SQLException {
        Timestamp expected = new Timestamp(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        when(mockCallableStatement.getTimestamp("param", cal)).thenReturn(expected);
        assertEquals(expected, statement.getTimestamp("param", cal));
        verify(mockCallableStatement).getTimestamp("param", cal);
    }

    // --- Deprecated methods ---

    @Test
    // Check that getBigDecimal(int, int) delegates to the underlying
    // CallableStatement without additional transformation.
    @SuppressWarnings("deprecation")
    void getBigDecimal_withScale_delegates() throws SQLException {
        BigDecimal expected = new BigDecimal("123.45");
        when(mockCallableStatement.getBigDecimal(1, 2)).thenReturn(expected);
        assertEquals(expected, statement.getBigDecimal(1, 2));
        verify(mockCallableStatement).getBigDecimal(1, 2);
    }

    // --- CharacterStream getters ---

    @Test
    // Check that getCharacterStream(int) delegates to the underlying
    // CallableStatement without additional transformation.
    void getCharacterStream_int_delegates() throws SQLException {
        Reader expected = mock(Reader.class);
        when(mockCallableStatement.getCharacterStream(1)).thenReturn(expected);
        assertSame(expected, statement.getCharacterStream(1));
        verify(mockCallableStatement).getCharacterStream(1);
    }

    @Test
    // Check that getCharacterStream(String) delegates to the underlying
    // CallableStatement without additional transformation.
    void getCharacterStream_string_delegates() throws SQLException {
        Reader expected = mock(Reader.class);
        when(mockCallableStatement.getCharacterStream("param")).thenReturn(expected);
        assertSame(expected, statement.getCharacterStream("param"));
        verify(mockCallableStatement).getCharacterStream("param");
    }

    // --- By-name setters: bytes and streams ---

    @Test
    // Check that setBytes(String, byte[]) delegates to the underlying
    // CallableStatement without additional transformation.
    void setBytes_string_delegates() throws SQLException {
        statement.setBytes("param", new byte[] {1, 2, 3});
        verify(mockCallableStatement).setBytes("param", new byte[] {1, 2, 3});
    }

    @Test
    // Check that setAsciiStream(String, InputStream) delegates to the
    // underlying CallableStatement without additional transformation.
    void setAsciiStream_string_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        statement.setAsciiStream("param", stream);
        verify(mockCallableStatement).setAsciiStream("param", stream);
    }

    @Test
    // Check that setAsciiStream(String, InputStream, int) delegates to
    // the underlying CallableStatement without additional transformation.
    void setAsciiStream_stringWithLength_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        statement.setAsciiStream("param", stream, 100);
        verify(mockCallableStatement).setAsciiStream("param", stream, 100);
    }

    @Test
    // Check that setBinaryStream(String, InputStream) delegates to the
    // underlying CallableStatement without additional transformation.
    void setBinaryStream_string_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        statement.setBinaryStream("param", stream);
        verify(mockCallableStatement).setBinaryStream("param", stream);
    }

    @Test
    // Check that setBinaryStream(String, InputStream, int) delegates to
    // the underlying CallableStatement without additional transformation.
    void setBinaryStream_stringWithLength_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        statement.setBinaryStream("param", stream, 100);
        verify(mockCallableStatement).setBinaryStream("param", stream, 100);
    }

    @Test
    // Check that setCharacterStream(String, Reader) delegates to the
    // underlying CallableStatement without additional transformation.
    void setCharacterStream_string_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        statement.setCharacterStream("param", reader);
        verify(mockCallableStatement).setCharacterStream("param", reader);
    }

    @Test
    // Check that setCharacterStream(String, Reader, int) delegates to
    // the underlying CallableStatement without additional transformation.
    void setCharacterStream_stringWithIntLength_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        statement.setCharacterStream("param", reader, 100);
        verify(mockCallableStatement).setCharacterStream("param", reader, 100);
    }

    @Test
    // Check that setCharacterStream(String, Reader, long) delegates to
    // the underlying CallableStatement without additional transformation.
    void setCharacterStream_stringWithLongLength_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        statement.setCharacterStream("param", reader, 100L);
        verify(mockCallableStatement).setCharacterStream("param", reader, 100L);
    }

    @Test
    // Check that setBlob(String, Blob) delegates to the underlying
    // CallableStatement without additional transformation.
    void setBlob_string_delegates() throws SQLException {
        Blob blob = mock(Blob.class);
        statement.setBlob("param", blob);
        verify(mockCallableStatement).setBlob("param", blob);
    }

    @Test
    // Check that setBlob(String, InputStream) delegates to the
    // underlying CallableStatement without additional transformation.
    void setBlob_stringWithInputStream_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        statement.setBlob("param", stream);
        verify(mockCallableStatement).setBlob("param", stream);
    }

    @Test
    // Check that setBlob(String, InputStream, long) delegates to the
    // underlying CallableStatement without additional transformation.
    void setBlob_stringWithInputStreamAndLength_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        statement.setBlob("param", stream, 100L);
        verify(mockCallableStatement).setBlob("param", stream, 100L);
    }

    @Test
    // Check that setClob(String, Clob) delegates to the underlying
    // CallableStatement without additional transformation.
    void setClob_string_delegates() throws SQLException {
        Clob clob = mock(Clob.class);
        statement.setClob("param", clob);
        verify(mockCallableStatement).setClob("param", clob);
    }

    @Test
    // Check that setClob(String, Reader) delegates to the underlying
    // CallableStatement without additional transformation.
    void setClob_stringWithReader_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        statement.setClob("param", reader);
        verify(mockCallableStatement).setClob("param", reader);
    }

    @Test
    // Check that setClob(String, Reader, long) delegates to the
    // underlying CallableStatement without additional transformation.
    void setClob_stringWithReaderAndLength_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        statement.setClob("param", reader, 100L);
        verify(mockCallableStatement).setClob("param", reader, 100L);
    }

    @Test
    // Check that setDate(String, Date) delegates to the underlying
    // CallableStatement without additional transformation.
    void setDate_string_delegates() throws SQLException {
        Date date = new Date(System.currentTimeMillis());
        statement.setDate("param", date);
        verify(mockCallableStatement).setDate("param", date);
    }

    @Test
    // Check that setDate(String, Date, Calendar) delegates to the
    // underlying CallableStatement without additional transformation.
    void setDate_stringWithCalendar_delegates() throws SQLException {
        Date date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        statement.setDate("param", date, cal);
        verify(mockCallableStatement).setDate("param", date, cal);
    }

    @Test
    // Check that setTime(String, Time) delegates to the underlying
    // CallableStatement without additional transformation.
    void setTime_string_delegates() throws SQLException {
        Time time = new Time(System.currentTimeMillis());
        statement.setTime("param", time);
        verify(mockCallableStatement).setTime("param", time);
    }

    @Test
    // Check that setTime(String, Time, Calendar) delegates to the
    // underlying CallableStatement without additional transformation.
    void setTime_stringWithCalendar_delegates() throws SQLException {
        Time time = new Time(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        statement.setTime("param", time, cal);
        verify(mockCallableStatement).setTime("param", time, cal);
    }

    @Test
    // Check that setTimestamp(String, Timestamp) delegates to the
    // underlying CallableStatement without additional transformation.
    void setTimestamp_string_delegates() throws SQLException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        statement.setTimestamp("param", timestamp);
        verify(mockCallableStatement).setTimestamp("param", timestamp);
    }

    @Test
    // Check that setTimestamp(String, Timestamp, Calendar) delegates to
    // the underlying CallableStatement without additional transformation.
    void setTimestamp_stringWithCalendar_delegates() throws SQLException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        statement.setTimestamp("param", timestamp, cal);
        verify(mockCallableStatement).setTimestamp("param", timestamp, cal);
    }

    @Test
    // Check that setNull(String, int) delegates to the underlying
    // CallableStatement without additional transformation.
    void setNull_string_delegates() throws SQLException {
        statement.setNull("param", 12);
        verify(mockCallableStatement).setNull("param", 12);
    }

    @Test
    // Check that setNull(String, int, String) delegates to the
    // underlying CallableStatement without additional transformation.
    void setNull_stringWithTypeName_delegates() throws SQLException {
        statement.setNull("param", 12, "VARCHAR");
        verify(mockCallableStatement).setNull("param", 12, "VARCHAR");
    }

    // --- Primitive by-name setters ---

    @Test
    // Check that setBoolean(String, boolean) delegates to the underlying
    // CallableStatement without additional transformation.
    void setBoolean_string_delegates() throws SQLException {
        statement.setBoolean("param", true);
        verify(mockCallableStatement).setBoolean("param", true);
    }

    @Test
    // Check that setByte(String, byte) delegates to the underlying
    // CallableStatement without additional transformation.
    void setByte_string_delegates() throws SQLException {
        statement.setByte("param", (byte) 42);
        verify(mockCallableStatement).setByte("param", (byte) 42);
    }

    @Test
    // Check that setShort(String, short) delegates to the underlying
    // CallableStatement without additional transformation.
    void setShort_string_delegates() throws SQLException {
        statement.setShort("param", (short) 42);
        verify(mockCallableStatement).setShort("param", (short) 42);
    }

    @Test
    // Check that setInt(String, int) delegates to the underlying
    // CallableStatement without additional transformation.
    void setInt_string_delegates() throws SQLException {
        statement.setInt("param", 42);
        verify(mockCallableStatement).setInt("param", 42);
    }

    @Test
    // Check that setLong(String, long) delegates to the underlying
    // CallableStatement without additional transformation.
    void setLong_string_delegates() throws SQLException {
        statement.setLong("param", 42L);
        verify(mockCallableStatement).setLong("param", 42L);
    }

    @Test
    // Check that setFloat(String, float) delegates to the underlying
    // CallableStatement without additional transformation.
    void setFloat_string_delegates() throws SQLException {
        statement.setFloat("param", 1.5f);
        verify(mockCallableStatement).setFloat("param", 1.5f);
    }

    @Test
    // Check that setDouble(String, double) delegates to the underlying
    // CallableStatement without additional transformation.
    void setDouble_string_delegates() throws SQLException {
        statement.setDouble("param", 1.5);
        verify(mockCallableStatement).setDouble("param", 1.5);
    }

    @Test
    // Check that setBigDecimal(String, BigDecimal) delegates to the
    // underlying CallableStatement without additional transformation.
    void setBigDecimal_string_delegates() throws SQLException {
        BigDecimal value = new BigDecimal("123.45");
        statement.setBigDecimal("param", value);
        verify(mockCallableStatement).setBigDecimal("param", value);
    }

    @Test
    // Check that setString(String, String) delegates to the underlying
    // CallableStatement without additional transformation.
    void setString_string_delegates() throws SQLException {
        statement.setString("param", "hello");
        verify(mockCallableStatement).setString("param", "hello");
    }

    @Test
    // Check that setURL(String, URL) delegates to the underlying
    // CallableStatement without additional transformation.
    void setURL_string_delegates() throws Exception {
        URL url = new URL("http://example.com");
        statement.setURL("param", url);
        verify(mockCallableStatement).setURL("param", url);
    }

    @Test
    // Check that setNString(String, String) delegates to the underlying
    // CallableStatement without additional transformation.
    void setNString_string_delegates() throws SQLException {
        statement.setNString("param", "hello");
        verify(mockCallableStatement).setNString("param", "hello");
    }

    @Test
    // Check that setNCharacterStream(String, Reader) delegates to the
    // underlying CallableStatement without additional transformation.
    void setNCharacterStream_string_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        statement.setNCharacterStream("param", reader);
        verify(mockCallableStatement).setNCharacterStream("param", reader);
    }

    @Test
    // Check that setNCharacterStream(String, Reader, long) delegates to
    // the underlying CallableStatement without additional transformation.
    void setNCharacterStream_stringWithLength_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        statement.setNCharacterStream("param", reader, 100L);
        verify(mockCallableStatement).setNCharacterStream("param", reader, 100L);
    }

    @Test
    // Check that setNClob(String, NClob) delegates to the underlying
    // CallableStatement without additional transformation.
    void setNClob_string_delegates() throws SQLException {
        NClob nclob = mock(NClob.class);
        statement.setNClob("param", nclob);
        verify(mockCallableStatement).setNClob("param", nclob);
    }

    @Test
    // Check that setNClob(String, Reader) delegates to the underlying
    // CallableStatement without additional transformation.
    void setNClob_stringWithReader_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        statement.setNClob("param", reader);
        verify(mockCallableStatement).setNClob("param", reader);
    }

    @Test
    // Check that setNClob(String, Reader, long) delegates to the
    // underlying CallableStatement without additional transformation.
    void setNClob_stringWithReaderAndLength_delegates() throws SQLException {
        Reader reader = mock(Reader.class);
        statement.setNClob("param", reader, 100L);
        verify(mockCallableStatement).setNClob("param", reader, 100L);
    }

    @Test
    // Check that setRowId(String, RowId) delegates to the underlying
    // CallableStatement without additional transformation.
    void setRowId_string_delegates() throws SQLException {
        RowId rowId = mock(RowId.class);
        statement.setRowId("param", rowId);
        verify(mockCallableStatement).setRowId("param", rowId);
    }

    @Test
    // Check that setSQLXML(String, SQLXML) delegates to the underlying
    // CallableStatement without additional transformation.
    void setSQLXML_string_delegates() throws SQLException {
        SQLXML xml = mock(SQLXML.class);
        statement.setSQLXML("param", xml);
        verify(mockCallableStatement).setSQLXML("param", xml);
    }

    @Test
    // Check that setBinaryStream(String, InputStream, long) delegates to
    // the underlying CallableStatement without additional transformation.
    void setBinaryStream_stringWithLongLength_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        statement.setBinaryStream("param", stream, 100L);
        verify(mockCallableStatement).setBinaryStream("param", stream, 100L);
    }

    @Test
    // Check that setAsciiStream(String, InputStream, long) delegates to
    // the underlying CallableStatement without additional transformation.
    void setAsciiStream_stringWithLongLength_delegates() throws SQLException {
        InputStream stream = mock(InputStream.class);
        statement.setAsciiStream("param", stream, 100L);
        verify(mockCallableStatement).setAsciiStream("param", stream, 100L);
    }
}
