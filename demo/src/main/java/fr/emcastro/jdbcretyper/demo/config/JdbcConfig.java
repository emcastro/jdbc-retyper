package fr.emcastro.jdbcretyper.demo.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

import fr.emcastro.jdbcretyper.demo.transform.JsonBoxTypeTransformer;
import fr.emcastro.jdbcretyper.jdbc.RetyperDatasource;
import fr.emcastro.jdbcretyper.transform.TypeTransformerRegistry;

@Configuration
public class JdbcConfig {

    @Bean
    public JdbcClient jdbcClient(DataSource dataSource) {
        TypeTransformerRegistry registry = new TypeTransformerRegistry();
        registry.register(new JsonBoxTypeTransformer());
        return JdbcClient.create(new RetyperDatasource(dataSource, registry));
    }
}
