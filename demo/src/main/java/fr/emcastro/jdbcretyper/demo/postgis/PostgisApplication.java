package fr.emcastro.jdbcretyper.demo.postgis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.ParseException;

import fr.emcastro.jdbcretyper.jdbc.RetyperConnection;
import fr.emcastro.jdbcretyper.transform.TypeTransformerRegistry;
import fr.emcastro.jdbcretyper.demo.postgis.transform.BofBofReadTransformer;
import fr.emcastro.jdbcretyper.demo.postgis.transform.GeometryPostgisWriteTransformer;
import fr.emcastro.jdbcretyper.demo.postgis.transform.JsonBoxPostgresWriteTransformer;

public class PostgisApplication {

    public static void main(String[] args) {
        var url = System.getenv("POSTGRES_URL");
        var user = System.getenv("POSTGRES_USER");
        var password = System.getenv("POSTGRES_PASSWORD");

        System.out.println("PostGIS demo application started");
        System.out.println("Connecting to: " + url);

        var registry = new TypeTransformerRegistry();
        registry.registerRead(new BofBofReadTransformer());
        registry.registerWrite(new GeometryPostgisWriteTransformer());
        registry.registerWrite(new JsonBoxPostgresWriteTransformer());

        try (var rawConnection = DriverManager.getConnection(url, user, password);
                var connection = new RetyperConnection(rawConnection, registry)) {
            var geometry = new WKTReader().read("POINT (1 2)");

            // Round trip JTS Geometry to PostGIS geometry and back
            try (var preparedStatement = connection.prepareStatement("SELECT ST_Buffer(?, 1, 1)")) {
                preparedStatement.setObject(1, geometry);

                try (var resultSet = preparedStatement.executeQuery()) {
                    resultSet.next();
                    Geometry geomResult = (Geometry) resultSet.getObject(1);

                    System.out.println("Retrieved geometry: " + geomResult);
                }
            }

            // Round trip JSON to Postgres jsonb and back
            try (var preparedStatement = connection.prepareStatement("""
                    SELECT 
                        ?-> 'a' || '{"type": "letter"}', 
                        (?-> 'b') :: numeric
                    """)) {
                var json = new JsonBox("""
                        {"a": {"name":"a"}, "b": 42}
                        """);
                preparedStatement.setObject(1, json);
                preparedStatement.setObject(2, json);
                try (var resultSet = preparedStatement.executeQuery()) {
                    resultSet.next();
                    JsonBox box = resultSet.getObject(1, JsonBox.class);
                    int num = resultSet.getInt(2);

                    System.out.println("Retrieved geometry: " + box);
                    System.out.println("Retrieved number: " + num);
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Unable to run PostGIS JDBC demo", e);
        } catch (ParseException e) {
            throw new IllegalStateException("Unable to parse initial WKT geometry", e);
        }
    }
}
