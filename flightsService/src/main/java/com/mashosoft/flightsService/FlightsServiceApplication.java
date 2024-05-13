package com.mashosoft.flightsService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class FlightsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightsServiceApplication.class, args);
	}

}
