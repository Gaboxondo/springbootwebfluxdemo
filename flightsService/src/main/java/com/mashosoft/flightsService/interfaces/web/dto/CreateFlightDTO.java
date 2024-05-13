package com.mashosoft.flightsService.interfaces.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFlightDTO {

    private String departureAirportCode;
    private String landingAirportCode;

}
