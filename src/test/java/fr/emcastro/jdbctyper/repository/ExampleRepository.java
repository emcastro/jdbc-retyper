package fr.emcastro.jdbctyper.repository;

import fr.emcastro.jdbctyper.JsonBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * Repository that works with JSON data using plain JDBC.
 * This implementation avoids Spring-specific dependencies to keep the core library lightweight.
 * Spring/JdbcClient usage should be confined to unit tests only.
 */
public class ExampleRepository {

  private final DataSource dataSource;

  public ExampleRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public record X(String name) {}

  public record Y(JsonBox name) {}

  public Object work(JsonBox id) {
    String sql = "SELECT (?->'a') name";
    
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
