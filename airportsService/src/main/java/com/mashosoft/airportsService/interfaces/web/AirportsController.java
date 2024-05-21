package com.mashosoft.airportsService.interfaces.web;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/v1/airports")
@AllArgsConstructor
@Slf4j
public class AirportsController {

    private static final List<String> validCodes = List.of("MLG","ROM");

    @GetMapping(value = "/validate/{airportCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Indicates if the airport code is valid or not")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Boolean> validateAirportCode(@PathVariable String airportCode){
        log.info("received request with airport code {}", airportCode);
        if(validCodes.contains( airportCode )){
            return Mono.just( true ).log();
        }else {
            return Mono.just( false ).log();
        }
    }

    @GetMapping(value = "/info/{airportCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "gets airports information")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Boolean> getAirportInfo(@PathVariable String airportCode){
        log.info("received request with airport code {}", airportCode);
        return null;
    }
}
