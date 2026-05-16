package fr.emcastro.jdbctyper.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.emcastro.jdbctyper.JsonBoxTypeTransformer;
import fr.emcastro.jdbctyper.jdbc.MagicDatasource;
import fr.emcastro.jdbctyper.transform.TypeTransformerRegistry;

@Configuration
public class JdbcConfig {

    @Bean
    public TypeTransformerRegistry typeTransformerRegistry() {
        TypeTransformerRegistry registry = new TypeTransformerRegistry();
        registry.register(new JsonBoxTypeTransformer());
        return registry;
    }

    @Bean
    public DataSource dataSource(TypeTransformerRegistry registry) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.duckdb.DuckDBDriver");
        dataSource.setUrl("jdbc:duckdb:");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return new MagicDatasource(dataSource, registry);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource, ObjectMapper objectMapper) {

        return new JdbcTemplate(dataSource);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
