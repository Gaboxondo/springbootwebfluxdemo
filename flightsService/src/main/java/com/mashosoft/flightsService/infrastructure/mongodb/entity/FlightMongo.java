package com.mashosoft.flightsService.infrastructure.mongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "flights")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FlightMongo {

    @Id
    private String id;
    private String departureAirportCode;
    private String landingAirportCode;

}
