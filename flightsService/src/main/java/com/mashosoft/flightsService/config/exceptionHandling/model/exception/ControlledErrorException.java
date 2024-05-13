package com.mashosoft.flightsService.config.exceptionHandling.model.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControlledErrorException extends RuntimeException{

    private String errorCode;

    private String errorMessage;

    private HttpStatus httpStatusResponse;

    public ControlledErrorException(String errorCode,String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatusResponse = HttpStatus.BAD_REQUEST;
    }
}
