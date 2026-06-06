package fr.emcastro.jdbcretyper.demo.postgis;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.ParseException;

import fr.emcastro.jdbcretyper.jdbc.RetyperConnection;
import fr.emcastro.jdbcretyper.transform.TypeTransformerRegistry;
import fr.emcastro.jdbcretyper.demo.postgis.transform.GeometryReadTransformer;
import fr.emcastro.jdbcretyper.demo.postgis.transform.GeometryPostgisWriteTransformer;
import fr.emcastro.jdbcretyper.demo.postgis.transform.JsonBoxReadTransformer;
import fr.emcastro.jdbcretyper.demo.postgis.transform.JsonBoxPostgresWriteTransformer;

public class PostgisApplication {

    public static void main(String[] args) {
        var url = System.getenv("POSTGRES_URL");
        var user = System.getenv("POSTGRES_USER");
        var password = System.getenv("POSTGRES_PASSWORD");

        System.out.println("PostGIS demo application started");
        System.out.println("Connecting to: " + url);
        System.out.println();

        var registry = new TypeTransformerRegistry();
        registry.registerRead(new GeometryReadTransformer());
        registry.registerRead(new JsonBoxReadTransformer());
        registry.registerWrite(new GeometryPostgisWriteTransformer());
        registry.registerWrite(new JsonBoxPostgresWriteTransformer());

        try (var rawConnection = DriverManager.getConnection(url, user, password);
                var connection = new RetyperConnection(rawConnection, registry)) {
            var geometry = new WKTReader().read("POINT (1 2)");

            System.out.println("Input: " + geometry);
            System.out.println();

            System.out.println("Geometry round-trip through ST_Buffer...");
            try (var preparedStatement = connection.prepareStatement("""
                SELECT ST_SnapToGrid(ST_Buffer(?, 1, 1), 1e-10)
            """)) {
                preparedStatement.setObject(1, geometry);

                try (var resultSet = preparedStatement.executeQuery()) {
                    resultSet.next();
                    Geometry geomResult = (Geometry) resultSet.getObject(1);

                    System.out.println("Result: " + geomResult + "  - awaiting POLYGON((2 1, 2 3, 0 3, 0 1, 2 1))");
                    System.out.println("Type: " + geomResult.getClass());
                }
            }
            System.out.println();

            var json = new JsonBox("""
                    {"a": {"name":"a"}, "b": 42}
                    """);
            System.out.println("Input: " + json);
            System.out.println();

            System.out.println("JSONB extraction using -> and ::numeric...");
            try (var preparedStatement = connection.prepareStatement("""
                    SELECT 
                        ?-> 'a' || '{"type": "letter"}', 
                        (?-> 'b') :: numeric
                    """)) {
                preparedStatement.setObject(1, json);
                preparedStatement.setObject(2, json);
                try (var resultSet = preparedStatement.executeQuery()) {
                    resultSet.next();
                    JsonBox box = resultSet.getObject(1, JsonBox.class);
                    int num = resultSet.getInt(2);

                    System.out.println("Result: " + box + "  - awaiting JsonBox[value={\"name\": \"a\", \"type\": \"letter\"}]");
                    System.out.println("Type: " + box.getClass());
                    System.out.println("Number: " + num + " - awaiting 42");
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Unable to run PostGIS JDBC demo", e);
        } catch (ParseException e) {
            throw new IllegalStateException("Unable to parse initial WKT geometry", e);
        }
    }
}
