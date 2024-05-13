package com.mashosoft.flightsService.interfaces.web.adapter.impl;

import com.mashosoft.flightsService.application.FlightsService;
import com.mashosoft.flightsService.domain.model.Flight;
import com.mashosoft.flightsService.interfaces.web.adapter.FlightsWebAdapter;
import com.mashosoft.flightsService.interfaces.web.adapter.mapper.FlightsAdapterMapper;
import com.mashosoft.flightsService.interfaces.web.dto.CreateFlightDTO;
import com.mashosoft.flightsService.interfaces.web.dto.FlightDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class FlightsWebAdapterImpl implements FlightsWebAdapter {

    private final FlightsService flightsService;
    private final FlightsAdapterMapper flightsAdapterMapper;

    @Override
    public Mono<FlightDTO> createFlight(CreateFlightDTO createFlightDTO) {
        Mono<Flight> flightMono = flightsService.createFlight( createFlightDTO.getDepartureAirportCode(),
            createFlightDTO.getLandingAirportCode(), createFlightDTO.getPrice() );
        return flightMono.map( flightsAdapterMapper::fromDomainToDTO );
    }

    @Override
    public Mono<FlightDTO> findCheapest(String departureAirport, String landingAirport) {
        Mono<Flight> flightMono = flightsService.getCheapest( departureAirport,landingAirport);
        return flightMono.map( flightsAdapterMapper::fromDomainToDTO );
    }
}
