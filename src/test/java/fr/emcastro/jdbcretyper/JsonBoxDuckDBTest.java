package fr.emcastro.jdbcretyper;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonBoxDuckDBTest extends DuckDBTestBase {

    @BeforeEach
    @Override
    protected void setUp() throws SQLException {
        super.setUp();
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE json_test (id INTEGER, data JSON)");
        }
    }

    @AfterEach
    @Override
    protected void tearDown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS json_test");
        }
        super.tearDown();
    }

    @Test
    // Check that a JsonBox inserted via PreparedStatement.setObject() can be
    // read back via ResultSet.getObject(columnIndex, JsonBox.class).
    void insertAndReadJsonBox() throws SQLException {
        JsonBox box = new JsonBox("""
                {"name":"test","value":42}
                """);

        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO json_test VALUES (1, ?)")) {
            ps.setObject(1, box);
            ps.execute();
        }

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT data FROM json_test WHERE id = 1")) {
            assertTrue(rs.next());
            var jsonbox = rs.getObject(1, JsonBox.class);
            assertEquals(box, jsonbox);
        }
    }

    @Test
    // Check that getObject(columnIndex) without a type hint still resolves
    // JsonBox through the default-type read transformer path.
    void insertAndReadJsonBoxDefaultType() throws SQLException {
        JsonBox box = new JsonBox("""
                {"nested":{"a":1,"b":2}}
                """);

        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO json_test VALUES (2, ?)")) {
            ps.setObject(1, box);
            ps.execute();
        }

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT data FROM json_test WHERE id = 2")) {
            assertTrue(rs.next());
            var obj = rs.getObject(1);
            assertInstanceOf(JsonBox.class, obj);
            assertEquals(box, obj);
        }
    }

    @Test
    // Check that SQL NULL in a JSON column correctly reports wasNull() for
    // both getObject(int) and getObject(int, Class).
    void nullJsonBoxRoundTrip() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO json_test VALUES (3, ?)")) {
            ps.setNull(1, java.sql.Types.VARCHAR);
            ps.execute();
        }

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT data FROM json_test WHERE id = 3")) {
            assertTrue(rs.next());
            rs.getObject(1);
            assertTrue(rs.wasNull());
            rs.getObject(1, JsonBox.class);
            assertTrue(rs.wasNull());
        }
    }
}
