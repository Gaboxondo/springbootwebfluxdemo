package com.mashosoft.flightsService.domain.model;

import com.mashosoft.flightsService.config.exceptionHandling.model.exception.BussinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class FlightFactory {

    public Flight createFlight(String departAirportCode,String landingAirportCode, Double price){
        //Of course this codes should be on an enum and should be within the class
        if(StringUtils.isAllBlank( departAirportCode )){
            log.warn( "Rejected request due to departure airport was null or empty" );
            throw new BussinessException("erro.code.01","departure airport code can not be blank");
        }
        if(StringUtils.isAllBlank( landingAirportCode )){
            log.warn( "Rejected request due to landing airport was null or empty" );
            throw new BussinessException("erro.code.02","landing airport code can not be blank");
        }
        if(price == null){
            log.warn( "Rejected request due to price is null" );
            throw new BussinessException("erro.code.03","Price can not be null");
        }
        if(price <0){
            log.warn( "Rejected request due to price is < 0" );
            throw new BussinessException("erro.code.04","The price can not be < than 0");
        }
        departAirportCode = StringUtils.toRootUpperCase( departAirportCode );
        landingAirportCode = StringUtils.toRootUpperCase( landingAirportCode );
        return Flight.builder()
            .id( UUID.randomUUID().toString() )
            .departureAirportCode( departAirportCode )
            .landingAirportCode( landingAirportCode )
            .price( price )
            .build();
    }
}
