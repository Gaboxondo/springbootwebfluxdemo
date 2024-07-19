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
@RequestMapping("/v1/aspectTest")
@AllArgsConstructor
public class TestAspectController {

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/empty-mono")
    @Operation(description = "Empty mono")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> testEmptyMono(){
        return Mono.empty();
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/empty-flux")
    @Operation(description = "Empty flux")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Void> testEmptyFlux(){
        return Flux.empty();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/delay")
    @Operation(description = "Test delay for cancel with a flux")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Integer> delayForCancel(){
        return Flux.just( 1,2,3,4,5,6,7,8,9,10,11,12,13 ).delayElements( Duration.ofSeconds( 1 ) );
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/error-Mono-controlled")
    @Operation(description = "Controlled error with mono")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Integer> errorMono(){
        throw new ControlledErrorException( ErrorCodes.DEPARTURE_CODE_NULL, "Error");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/error-Flux-controlled")
    @Operation(description = "Controlled error with flux")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Integer> errorFlux(){
        throw new ControlledErrorException( ErrorCodes.DEPARTURE_CODE_NULL, "Error");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/error-Mono-Bug")
    @Operation(description = "Mono error Bug")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Integer> monoBug(){
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.getPrice().doubleValue();
        return Mono.just( 1 );
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/error-Flux-Bug")
    @Operation(description = "Flux error Bug")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Object> fluxBug(){
        return Flux.just( 1,"hello",3,null,5,"bye" ).map( Object::toString );
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/ok-Mono")
    @Operation(description = "Mono all ok to test debug logs")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Integer> monoOK(){
        return Mono.just( 12394 );
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/ok-Flux")
    @Operation(description = "flux ok to test debug logs")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Integer> FluxOk(){
        return Flux.just( 1,2,3,4,5 );
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/error-Mono-As-Error")
    @Operation(description = "test delay for cancel")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Integer> errorMonoError(){
        return Mono.error( new ControlledErrorException(ErrorCodes.NOT_VALID_AIRPORT_CODE,"error") );
    }
}
