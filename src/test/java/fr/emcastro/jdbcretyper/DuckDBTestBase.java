package fr.emcastro.jdbcretyper;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import fr.emcastro.jdbcretyper.jdbc.RetyperConnection;
import fr.emcastro.jdbcretyper.transform.GeometryReadTransformer;
import fr.emcastro.jdbcretyper.transform.GeometryWriteTransformer;
import fr.emcastro.jdbcretyper.transform.JsonBoxReadTransformer;
import fr.emcastro.jdbcretyper.transform.JsonBoxWriteTransformer;
import fr.emcastro.jdbcretyper.transform.TypeTransformerRegistry;

/**
 * Base class for DuckDB integration tests. Creates an in-memory DuckDB connection
 * wrapped with {@link RetyperConnection} and a pre-configured {@link TypeTransformerRegistry}.
 */
public abstract class DuckDBTestBase {

    protected TypeTransformerRegistry registry;
    protected RetyperConnection connection;

    protected void setUp() throws SQLException {
        registry = new TypeTransformerRegistry();
        registry.registerRead(new JsonBoxReadTransformer());
        registry.registerWrite(new JsonBoxWriteTransformer());
        registry.registerRead(new GeometryReadTransformer());
        registry.registerWrite(new GeometryWriteTransformer());

        var raw = DriverManager.getConnection("jdbc:duckdb:");
        connection = new RetyperConnection(raw, registry);

        // Load spatial extension for geometry tests
        try (Statement stmt = raw.createStatement()) {
            stmt.execute("INSTALL spatial");
            stmt.execute("LOAD spatial");
        }
    }

    protected void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
