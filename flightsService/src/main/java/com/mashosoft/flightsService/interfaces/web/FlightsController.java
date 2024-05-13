package com.mashosoft.flightsService.interfaces.web;

import com.mashosoft.flightsService.infrastructure.mongodb.entity.FlightMongo;
import com.mashosoft.flightsService.infrastructure.mongodb.repository.FlightMongoReactiveRepository;
import com.mashosoft.flightsService.interfaces.web.adapter.FlightsWebAdapter;
import com.mashosoft.flightsService.interfaces.web.dto.CreateFlightDTO;
import com.mashosoft.flightsService.interfaces.web.dto.FlightDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/flights")
@AllArgsConstructor
public class FlightsController {

    private final FlightMongoReactiveRepository flightMongoReactiveRepository;
    private final FlightsWebAdapter flightsWebAdapter;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @Operation(description = "Get a flight By id")
    public Mono<FlightMongo> getFlight(@PathVariable String id){
        return null;
    }

    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @Operation(description = "Get all flights")
    public Flux<FlightMongo> getAllFlights(){
        return null;
    }

    @GetMapping(value = "/{departureAirportCode}/{landingAirportCode}/cheapest",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @Operation(description = "Get the cheapest price for an specific")
    public Mono<FlightDTO> getTheCheapest(@PathVariable String departureAirportCode,@PathVariable String landingAirportCode){
        return flightsWebAdapter.findCheapest( departureAirportCode,landingAirportCode );
    }

    // Create new Product
    @PostMapping
    @Operation(description = "Register a new flight")
    public Mono<FlightDTO> insertFlight(@RequestBody CreateFlightDTO createFlightDTO){
        return flightsWebAdapter.createFlight( createFlightDTO );
    }

}
