package com.mashosoft.flightsService.infrastructure.mongodb.adapter.mapper;

import com.mashosoft.flightsService.domain.model.Flight;
import com.mashosoft.flightsService.infrastructure.mongodb.entity.FlightMongo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class FlightMongoMapper {

    public FlightMongo fromDomainToMongo(Flight flight){
        FlightMongo flightMongo = new FlightMongo();
        BeanUtils.copyProperties( flight,flightMongo );
        return flightMongo;
    }

    public Flight fromMongoToDomain(FlightMongo flightMongo){
        Flight flight = new Flight();
        BeanUtils.copyProperties( flightMongo,flight );
        return flight;
    }
}
