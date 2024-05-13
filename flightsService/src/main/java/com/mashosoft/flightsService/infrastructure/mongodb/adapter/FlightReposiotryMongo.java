package com.mashosoft.flightsService.infrastructure.mongodb.adapter;

import com.mashosoft.flightsService.domain.model.Flight;
import com.mashosoft.flightsService.domain.repository.FlightRepository;
import com.mashosoft.flightsService.infrastructure.mongodb.adapter.mapper.FlightMongoMapper;
import com.mashosoft.flightsService.infrastructure.mongodb.entity.FlightMongo;
import com.mashosoft.flightsService.infrastructure.mongodb.repository.FlightMongoReactiveRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class FlightReposiotryMongo implements FlightRepository {

    private final FlightMongoReactiveRepository flightMongoReactiveRepository;
    private final FlightMongoMapper flightMongoMapper;

    @Override
    public Mono<Flight> saveFlight(Flight flight) {
        FlightMongo flightMongo = flightMongoMapper.fromDomainToMongo( flight );
        Mono<FlightMongo> flightMongoMono = flightMongoReactiveRepository.save( flightMongo );
        return flightMongoMono.map( flightMongoMapper::fromMongoToDomain );
    }
}
