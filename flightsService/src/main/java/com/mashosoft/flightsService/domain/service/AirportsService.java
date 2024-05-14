package com.mashosoft.flightsService.domain.service;

import reactor.core.publisher.Mono;

public interface AirportsService {

    public Mono<Boolean> airportCodeIsValid(String airportCode);
}
