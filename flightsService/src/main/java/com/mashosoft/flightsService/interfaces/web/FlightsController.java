package com.mashosoft.flightsService.interfaces.web;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.mashosoft.flightsService.infrastructure.mongodb.entity.FlightMongo;
import com.mashosoft.flightsService.infrastructure.mongodb.repository.FlightMongoReactiveRepository;
import com.mashosoft.flightsService.interfaces.web.dto.CreateFlightDTO;
import com.mashosoft.flightsService.interfaces.web.dto.FlightDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/flights")
@AllArgsConstructor
public class FlightsController {

    private final FlightMongoReactiveRepository flightMongoReactiveRepository;

    @GetMapping("/{id}")
    public Mono<FlightMongo> getFlight(@PathVariable String id){
        return flightMongoReactiveRepository.findById( id );
    }

    // Create new Product
    @PostMapping
    @Operation(description = "Register a new flight")
    public Mono<FlightDTO> insertFlight(@RequestBody CreateFlightDTO createFlightDTO){
        FlightMongo flightMongo = new FlightMongo();
        BeanUtils.copyProperties( createFlightDTO,flightMongo );
        Mono<FlightMongo> flightMongoMono = flightMongoReactiveRepository.save( flightMongo ).log();
        return flightMongoMono.map( flightMongoSaved -> {
            FlightDTO flightDTO = new FlightDTO();
            flightDTO.setId( flightMongoSaved.getId() );
            flightDTO.setLandingAirportCode( flightMongoSaved.getLandingAirportCode() );
            flightDTO.setDepartureAirportCode( flightMongoSaved.getDepartureAirportCode() );
            return flightDTO;
        } );
    }

}
