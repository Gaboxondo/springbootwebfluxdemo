package com.mashosoft.flightsService.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Data
@SuperBuilder
public class Flight {

    private String id;
    private String departureAirportCode;
    private String landingAirportCode;
    private Double price;
}
