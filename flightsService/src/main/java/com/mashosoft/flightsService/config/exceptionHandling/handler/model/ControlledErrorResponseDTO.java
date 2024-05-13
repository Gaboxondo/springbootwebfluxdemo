package com.mashosoft.flightsService.config.exceptionHandling.handler.model;

import lombok.Data;

@Data
public class ControlledErrorResponseDTO {

    public ControlledErrorResponseDTO(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String errorCode;

    public String errorMessage;


}
