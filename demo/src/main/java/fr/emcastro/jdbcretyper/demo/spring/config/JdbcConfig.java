package fr.emcastro.jdbcretyper.demo.spring.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

import fr.emcastro.jdbcretyper.demo.spring.transform.JsonBoxReadTransformer;
import fr.emcastro.jdbcretyper.demo.spring.transform.JsonBoxWriteTransformer;
import fr.emcastro.jdbcretyper.jdbc.RetyperDatasource;
import fr.emcastro.jdbcretyper.transform.TypeTransformerRegistry;

@Configuration
public class JdbcConfig {

    @Bean
    public JdbcClient jdbcClient(DataSource dataSource) {
        TypeTransformerRegistry registry = new TypeTransformerRegistry();
        registry.registerRead(new JsonBoxReadTransformer());
        registry.registerWrite(new JsonBoxWriteTransformer());
        return JdbcClient.create(new RetyperDatasource(dataSource, registry));
    }
}
