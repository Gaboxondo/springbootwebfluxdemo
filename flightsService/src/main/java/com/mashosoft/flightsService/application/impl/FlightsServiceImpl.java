package com.mashosoft.flightsService.application.impl;

import com.mashosoft.flightsService.application.FlightsService;
import com.mashosoft.flightsService.domain.model.Flight;
import com.mashosoft.flightsService.domain.model.FlightFactory;
import com.mashosoft.flightsService.domain.repository.FlightRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
        String finalDepartureAirportCode = StringUtils.toRootUpperCase( departureAirportCode );
        String finalLandingAirportCode = StringUtils.toRootUpperCase( landingAirportCode );
        return flightRepository.getAll()
            .filter( flight -> flight.getDepartureAirportCode().equals( finalDepartureAirportCode ) )
            .filter( flight -> flight.getLandingAirportCode().equals( finalLandingAirportCode ) )
            .reduce( (flight1,flight2) -> {
                if(flight1.getPrice() < flight2.getPrice() || flight1.getPrice().equals( flight2.getPrice() )){
                    return flight1;
                } else {
                    return flight2;
                }
            });
    }

    @Override
    public Flux<Flight> getAll() {
        return flightRepository.getAll();
    }
}
