package com.mashosoft.flightsService.infrastructure.mongodb.repository;

import com.mashosoft.flightsService.infrastructure.mongodb.entity.FlightMongo;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightMongoReactiveRepository extends ReactiveCrudRepository<FlightMongo, String> {
}
