package com.mashosoft.flightsService.config.exceptionHandling.handler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
public class NoResultsDTO {

    public String errorMessage;


}
