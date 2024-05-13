package com.mashosoft.flightsService.interfaces.web.adapter;

import com.mashosoft.flightsService.interfaces.web.dto.CreateFlightDTO;
import com.mashosoft.flightsService.interfaces.web.dto.FlightDTO;
import reactor.core.publisher.Mono;

public interface FlightsWebAdapter {

    public Mono<FlightDTO> createFlight(CreateFlightDTO createFlightDTO);

    public Mono<FlightDTO> findCheapest(String departureAirport,String landingAirport);
}
