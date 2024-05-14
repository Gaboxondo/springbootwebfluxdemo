package com.mashosoft.flightsService.infrastructure.airports.adapter;

import com.mashosoft.flightsService.domain.service.AirportsService;
import com.mashosoft.flightsService.infrastructure.airports.client.AirportsWebClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

//TODO it will be also an implementation mocked with a conditionalOnProperty
@Service
@AllArgsConstructor
public class AirportsServiceImpl implements AirportsService {

    private final AirportsWebClient airportsWebClient;

    @Override
    public Mono<Boolean> airportCodeIsValid(String airportCode) {
        return airportsWebClient.validateAirportCode( airportCode );
    }
}
