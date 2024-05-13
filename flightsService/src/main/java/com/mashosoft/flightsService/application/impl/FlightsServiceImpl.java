package com.mashosoft.flightsService.application.impl;

import com.mashosoft.flightsService.application.FlightsService;
import com.mashosoft.flightsService.domain.model.Flight;
import com.mashosoft.flightsService.domain.model.FlightFactory;
import com.mashosoft.flightsService.domain.repository.FlightRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class FlightsServiceImpl implements FlightsService {

    private final FlightRepository flightRepository;
    private final FlightFactory flightfactory;

    @Override
    public Mono<Flight> createFlight(String departureAirport, String landingAirport, Double price) {
        return flightRepository.saveFlight( flightfactory.createFlight( departureAirport,landingAirport,price ) );
    }
}
