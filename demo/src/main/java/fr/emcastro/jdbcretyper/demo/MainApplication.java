package fr.emcastro.jdbcretyper.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import fr.emcastro.jdbcretyper.demo.repository.ExampleRepository;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Autowired
    ExampleRepository repo;

    @Bean
    @Profile("!test")
    CommandLineRunner run() {
        return args -> {
            System.out.println("Demo application started");

            var jsonBox = new JsonBox("{\"a\": {\"name\":\"a\"}, \"b\": 42}");
            System.out.println("Input: " + jsonBox);
            System.out.println();

            System.out.println("Extracting a into a JsonBox...");
            JsonBox box = repo.extractA(jsonBox);
            System.out.println("Result: " + box + "  - awaiting JsonBox[value={\"name\":\"a\"}]");
            System.out.println("Type: " + box.getClass());
            System.out.println();

            System.out.println("Extracting a and b using a JdbcClient query into a record...");
            String result = repo.extractAB(jsonBox);
            System.out.println("Result: " + result + "  - awaiting JsonBox[value={\"name\":\"a\"}]-42");
        };
    }
}
