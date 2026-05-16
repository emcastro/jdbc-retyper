package fr.emcastro.jdbctyper.spring;

import fr.emcastro.jdbctyper.JsonBox;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test repository that works with JSON data using plain JDBC. Uses
 * Spring @Component for dependency injection in demo/test context.
 */
@Component
public class ExampleRepository {

    private final DataSource dataSource;

    public ExampleRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public record X(String name) {}

    public record Y(JsonBox name) {}

    public Object work(JsonBox id) {
        String sql = "SELECT name: (?->'a')";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            // For DuckDB, we can pass the JSON directly as a string parameter
            // In a more sophisticated implementation, we might use a specific JSON type
            statement.setString(1, id.value());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getObject(1);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing query", e);
        }
    }
}
