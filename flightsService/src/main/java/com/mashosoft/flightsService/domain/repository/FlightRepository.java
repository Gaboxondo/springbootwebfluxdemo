package com.mashosoft.flightsService.domain.repository;

import com.mashosoft.flightsService.domain.model.Flight;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FlightRepository {

    public Mono<Flight> saveFlight(Flight flight);

    public Flux<Flight> getAll();
}
