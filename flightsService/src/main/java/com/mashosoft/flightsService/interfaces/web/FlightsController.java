package com.mashosoft.flightsService.interfaces.web;

import com.mashosoft.flightsService.infrastructure.mongodb.repository.FlightMongoReactiveRepository;
import com.mashosoft.flightsService.interfaces.web.adapter.FlightsWebAdapter;
import com.mashosoft.flightsService.interfaces.web.dto.CreateFlightDTO;
import com.mashosoft.flightsService.interfaces.web.dto.FlightDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/v1/flights")
@AllArgsConstructor
public class FlightsController {

    private final FlightMongoReactiveRepository flightMongoReactiveRepository;
    private final FlightsWebAdapter flightsWebAdapter;

    @GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
    @Operation(description = "Get all flights")
    @ResponseStatus(HttpStatus.OK)
    public Flux<FlightDTO> getAll(){
        return flightsWebAdapter.getAll(  ).delayElements( Duration.ofSeconds( 1 ) );
    }

    @GetMapping(value = "/{departureAirportCode}/{landingAirportCode}/cheapest",produces = MediaType.APPLICATION_NDJSON_VALUE)
    @Operation(description = "Get the cheapest price for an specific")
    @ResponseStatus(HttpStatus.OK)
    public Mono<FlightDTO> getTheCheapest(@PathVariable String departureAirportCode,@PathVariable String landingAirportCode){
        return flightsWebAdapter.findCheapest( departureAirportCode,landingAirportCode );
    }

    // Create new Product
    @PostMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
    @Operation(description = "Register a new flight")
    @ResponseStatus(HttpStatus.OK)
    public Mono<FlightDTO> insertFlight(@RequestBody CreateFlightDTO createFlightDTO){
        return flightsWebAdapter.create( createFlightDTO );
    }

}
