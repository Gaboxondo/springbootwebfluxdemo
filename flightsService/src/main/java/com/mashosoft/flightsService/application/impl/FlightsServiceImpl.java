package com.mashosoft.flightsService.application.impl;

import com.mashosoft.flightsService.application.FlightsService;
import com.mashosoft.flightsService.domain.model.Flight;
import com.mashosoft.flightsService.domain.model.FlightFactory;
import com.mashosoft.flightsService.domain.repository.FlightRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class FlightsServiceImpl implements FlightsService {

    private final FlightRepository flightRepository;
    private final FlightFactory flightfactory;

    @Override
    public Mono<Flight> createFlight(String departureAirportCode, String landingAirportCode, Double price) {
        return flightRepository.saveFlight( flightfactory.createFlight( departureAirportCode,landingAirportCode,price ) );
    }

    @Override
    public Mono<Flight> getCheapest(String departureAirportCode, String landingAirportCode) {
        return flightRepository.getAll()
            .filter( flight -> flight.getDepartureAirportCode().equals( departureAirportCode ) )
            .filter( flight -> flight.getLandingAirportCode().equals( landingAirportCode ) )
            .reduce( (flight1,flight2) -> {
                if(flight1.getPrice() > flight2.getPrice() || flight1.getPrice().equals( flight2.getPrice() )){
                    return flight1;
                } else {
                    return flight2;
                }
            });
    }
}
