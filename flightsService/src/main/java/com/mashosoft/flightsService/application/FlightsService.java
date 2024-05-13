package com.mashosoft.flightsService.application;

import com.mashosoft.flightsService.domain.model.Flight;
import reactor.core.publisher.Mono;

public interface FlightsService {

    public Mono<Flight> createFlight(String departureAirport,String landingAirport,Double price);

    public Mono<Flight> getCheapest(String departureAirport,String landingAirport);
}
