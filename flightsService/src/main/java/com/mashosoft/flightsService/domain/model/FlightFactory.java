package com.mashosoft.flightsService.domain.model;

import com.mashosoft.flightsService.config.exceptionHandling.model.exception.BussinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FlightFactory {

    public Flight createFlight(String departAirport,String landingAirport, Double price){
        if(price <0){
            throw new BussinessException("erro.code.01","The price can not be < than 0");
        }
        departAirport = StringUtils.toRootUpperCase( departAirport );
        landingAirport = StringUtils.toRootUpperCase( landingAirport );
        return Flight.builder()
            .id( UUID.randomUUID().toString() )
            .departureAirportCode( departAirport )
            .landingAirportCode( landingAirport )
            .price( price )
            .build();
    }
}
