package com.mashosoft.flightsService.interfaces.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FlightDTO {

    private String id;
    private String departureAirportCode;
    private String landingAirportCode;

}
