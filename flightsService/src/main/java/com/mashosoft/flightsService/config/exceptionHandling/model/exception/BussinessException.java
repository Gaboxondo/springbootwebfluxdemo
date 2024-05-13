package com.mashosoft.flightsService.config.exceptionHandling.model.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BussinessException extends RuntimeException{

    private String errorCode;

    private String errorMessage;
}
