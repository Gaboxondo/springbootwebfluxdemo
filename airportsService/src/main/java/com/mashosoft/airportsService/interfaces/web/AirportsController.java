package com.mashosoft.airportsService.interfaces.web;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/v1/airports")
@AllArgsConstructor
public class AirportsController {

    private static final List<String> validCodes = List.of("MLG","ROM");

    @GetMapping(value = "/validate/{airportCode}",produces = MediaType.APPLICATION_NDJSON_VALUE)
    @Operation(description = "Indicates if the airport code is valid or not")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Boolean> validateAirportCode(@PathVariable String airportCode){
        if(validCodes.contains( airportCode )){
            return Mono.just( true );
        }else {
            return Mono.just( false );
        }
    }
}
