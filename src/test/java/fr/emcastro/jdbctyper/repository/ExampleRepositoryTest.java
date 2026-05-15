package fr.emcastro.jdbctyper.repository;

import fr.emcastro.jdbctyper.JsonBox;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExampleRepositoryTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private ExampleRepository exampleRepository;

    @Test
    void testWorkWithValidJsonBox() throws SQLException {
        // Arrange
        JsonBox jsonBox = new JsonBox("{\"a\": {\"b\": \"c\"}}");
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getObject(1)).thenReturn("test-value");

        // Act
        Object result = exampleRepository.work(jsonBox);

        // Assert
        assertEquals("test-value", result);
        verify(preparedStatement).setString(1, "{\"a\": {\"b\": \"c\"}}");
    }

    @Test
    void testWorkWithNullJsonBox() {
        // Arrange
        JsonBox jsonBox = null;
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            exampleRepository.work(jsonBox);
        });
    }
}