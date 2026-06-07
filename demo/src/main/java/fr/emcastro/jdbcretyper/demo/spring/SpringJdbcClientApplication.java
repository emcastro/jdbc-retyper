package fr.emcastro.jdbcretyper.demo.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import fr.emcastro.jdbcretyper.demo.spring.repository.ExampleRepository;

@SpringBootApplication(scanBasePackages = "fr.emcastro.jdbcretyper.demo")
public class SpringJdbcClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringJdbcClientApplication.class, args);
    }

    @Autowired
    ExampleRepository repo;

    @Bean
    @Profile("!test")
    CommandLineRunner run() {
        return args -> {
            System.out.println("Demo application started");

            var jsonbox = new JsonBox("""
                    {"a": {"name":"a"}, "b": 42}
                    """);
            System.out.println("Input: " + jsonbox);
            System.out.println();

            System.out.println("Extracting a into a JsonBox...");
            var resultBox1 = repo.extractA(jsonbox);
            System.out.println("Result: " + resultBox1 + "  - awaiting JsonBox[value={\"name\":\"a\"}]");
            System.out.println("Type: " + resultBox1.getClass());
            System.out.println();

            System.out.println("Extracting a and b using a JdbcClient query into a record...");
            var resultBox2 = repo.extractAB(jsonbox);
            System.out.println("Result: " + resultBox2 + "  - awaiting JsonBox[value={\"name\":\"a\"}]-42");
        };
    }
}
