package fr.emcastro.jdbcretyper.demo.repository;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import fr.emcastro.jdbcretyper.demo.JsonBox;

@Component
public class ExampleRepository {

    private final JdbcClient jdbcClient;

    public ExampleRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    record ABResult(JsonBox a, int b) {
    }

    public String extractAB(JsonBox json) {
        String sql = """
                SELECT a: (:box -> 'a'),
                       b: (:box -> 'b')
                    """;
        ABResult result = jdbcClient.sql(sql).param("box", json).query(ABResult.class).single();
        return result.a + "-" + result.b;
    }

    public JsonBox extractA(JsonBox json) {
        String sql = "SELECT (? -> 'a') AS a";
        return (JsonBox) jdbcClient.sql(sql).param(1, json).query().singleValue();
    }
}
