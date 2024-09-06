package org.example.notetakingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class NoteTakingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoteTakingAppApplication.class, args);
    }

}
