package fr.emcastro.jdbctyper;

import fr.emcastro.jdbctyper.repository.ExampleRepository;
import fr.emcastro.jdbctyper.JsonBox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

  private static final Logger logger = LoggerFactory.getLogger(
    MainApplication.class
  );

  public static void main(String[] args) {
    SpringApplication.run(MainApplication.class, args);
  }

  @Autowired
  ExampleRepository repo;

  @Autowired
  com.fasterxml.jackson.databind.ObjectMapper objectMapper;

  @Bean
  CommandLineRunner run() {
    return args -> {
      logger.info("Application CLI démarrée avec succès ! ");
      // var objectNode = (ObjectNode) objectMapper.readTree(
      //   "{\"a\": {\"b\":\"c\"}}"
      // );
      var objectNode = new JsonBox("{\"a\": {\"b\":\"c\"}}");

      var data = repo.work(objectNode);
      System.out.println(data);
      System.out.println(data.getClass());
    };
  }
}
