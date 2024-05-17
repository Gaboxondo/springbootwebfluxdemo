package com.mashosoft.flightsService.interfaces.web.adapter.impl;

import com.mashosoft.flightsService.application.FlightsService;
import com.mashosoft.flightsService.config.exceptionHandling.ErrorCodes;
import com.mashosoft.flightsService.config.exceptionHandling.model.exception.ControlledErrorException;
import com.mashosoft.flightsService.domain.model.Flight;
import com.mashosoft.flightsService.interfaces.web.adapter.FlightsWebAdapter;
import com.mashosoft.flightsService.interfaces.web.adapter.mapper.FlightsAdapterMapper;
import com.mashosoft.flightsService.interfaces.web.dto.CreateFlightDTO;
import com.mashosoft.flightsService.interfaces.web.dto.FlightDTO;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class FlightsWebAdapterImpl implements FlightsWebAdapter {

    private final FlightsService flightsService;
    private final FlightsAdapterMapper flightsAdapterMapper;

    @Override
    public Mono<FlightDTO> create(CreateFlightDTO createFlightDTO) {
        Mono<Flight> flightMono = flightsService.createFlight( createFlightDTO.getDepartureAirportCode(),
            createFlightDTO.getLandingAirportCode(), createFlightDTO.getPrice() );
        return flightMono.map( flightsAdapterMapper::fromDomainToDTO );
    }

    @Override
    public Mono<FlightDTO> findCheapest(String departureAirport, String landingAirport) {
        if(StringUtils.isAllEmpty( departureAirport )){
           throw new ControlledErrorException( ErrorCodes.DEPARTURE_CODE_NULL,"Departure airport code can not be null or empty" );
        }
        if(StringUtils.isAllEmpty( landingAirport )){
            throw new ControlledErrorException( ErrorCodes.LANDING_CODE_NULL,"Landing airport code can not be null or empty" );
        }
        Mono<Flight> flightMono = flightsService.getCheapest( departureAirport,landingAirport);
        return flightMono.map( flightsAdapterMapper::fromDomainToDTO );
    }

    @Override
    public Flux<FlightDTO> getAll() {
        Flux<Flight> flightFlux = flightsService.getAll();
        return flightFlux.map( flightsAdapterMapper::fromDomainToDTO );
    }
}
