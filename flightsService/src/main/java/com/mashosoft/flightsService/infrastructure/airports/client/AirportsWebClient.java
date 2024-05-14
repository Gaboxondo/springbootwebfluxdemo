package com.mashosoft.flightsService.infrastructure.airports.client;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AirportsWebClient {

    //I know all is hardcoded and should be configurable with some @ConfigurationProperties, this is just for testing
    public Mono<Boolean> validateAirportCode(String airportCode){
        WebClient airportsWebClient = WebClient.builder().baseUrl( "http://localhost:8082" ).build();
        return airportsWebClient.get().uri( "/v1/airports/validate/" + airportCode ).accept(
                MediaType.valueOf( MediaType.TEXT_EVENT_STREAM_VALUE ) )
            .retrieve().bodyToMono( Boolean.class );
    }
}
