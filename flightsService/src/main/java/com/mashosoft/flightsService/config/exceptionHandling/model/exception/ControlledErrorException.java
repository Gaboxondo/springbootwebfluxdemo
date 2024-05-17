package com.mashosoft.flightsService.config.exceptionHandling.model.exception;

import com.mashosoft.flightsService.config.exceptionHandling.ErrorCodes;
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

    public ControlledErrorException(ErrorCodes errorCode,String errorMessage){
        this.errorCode = errorCode.getCode();
        this.errorMessage = errorMessage;
        this.httpStatusResponse = HttpStatus.BAD_REQUEST;
    }
}
