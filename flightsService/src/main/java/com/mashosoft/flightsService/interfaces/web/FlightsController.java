package com.mashosoft.flightsService.interfaces.web;

import com.mashosoft.flightsService.config.exceptionHandling.ErrorCodes;
import com.mashosoft.flightsService.config.exceptionHandling.model.exception.ControlledErrorException;
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get all flights")
    @ResponseStatus(HttpStatus.OK)
    public Flux<FlightDTO> getAll(){
        return flightsWebAdapter.getAll(  ).delayElements( Duration.ofSeconds( 1 ) ).log();
    }

    @GetMapping(value = "/{departureAirportCode}/{landingAirportCode}/cheapest",produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get the cheapest price for an specific")
    @ResponseStatus(HttpStatus.OK)
    public Mono<FlightDTO> getTheCheapest(@PathVariable String departureAirportCode,@PathVariable String landingAirportCode){
        return flightsWebAdapter.findCheapest( departureAirportCode,landingAirportCode ).log();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Register a new flight")
    @ResponseStatus(HttpStatus.OK)
    public Mono<FlightDTO> insertFlight(@RequestBody CreateFlightDTO createFlightDTO){
        return flightsWebAdapter.create( createFlightDTO ).log();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/test")
    @Operation(description = "test Empty")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> testEmpty(){
        return Mono.empty();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/delay")
    @Operation(description = "test delay for cancel")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Integer> delayForCancel(){
        return Flux.just( 1,2,3,4,5,6,7,8,9,10 ).delayElements( Duration.ofSeconds( 1 ) );
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/errorMono")
    @Operation(description = "test delay for cancel")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Integer> errorMono(){
        throw new ControlledErrorException( ErrorCodes.DEPARTURE_CODE_NULL, "Error");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/errorFlux")
    @Operation(description = "test delay for cancel")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Integer> errorFlux(){
        throw new ControlledErrorException( ErrorCodes.DEPARTURE_CODE_NULL, "Error");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/errorMonoBug")
    @Operation(description = "mono error Bug")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Integer> monoBug(){
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.getPrice().doubleValue();
        return Mono.just( 1 );
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/errorMonoAsError")
    @Operation(description = "test delay for cancel")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Integer> errorMonoError(){
        return Mono.error( new ControlledErrorException(ErrorCodes.NOT_VALID_AIRPORT_CODE,"error") );
    }
}
