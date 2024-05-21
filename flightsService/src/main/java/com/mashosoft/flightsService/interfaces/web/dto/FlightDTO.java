package com.mashosoft.flightsService.interfaces.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlightDTO {

    private String id;
    private String departureAirportCode;
    private String landingAirportCode;
    private Double price;

}
