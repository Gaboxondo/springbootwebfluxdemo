package com.mashosoft.flightsService.config.exceptionHandling.handler;

import com.mashosoft.flightsService.config.exceptionHandling.handler.model.ControlledErrorResponseDTO;
import com.mashosoft.flightsService.config.exceptionHandling.handler.model.NoResultsDTO;
import com.mashosoft.flightsService.config.exceptionHandling.model.exception.ControlledErrorException;
import com.mashosoft.flightsService.config.exceptionHandling.model.exception.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class MainControllerExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger( MainControllerExceptionHandler.class);

    @Value("${spring.application.name:unknown}")
    private String appname;


    @ExceptionHandler(ControlledErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ControlledErrorResponseDTO handleException(ControlledErrorException ex) {
        return new ControlledErrorResponseDTO(ex.getErrorCode(),ex.getErrorMessage());
    }

    @ExceptionHandler(NoResultException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public NoResultsDTO handleException(NoResultException ex) {
        return NoResultsDTO.builder().errorMessage( "No results for the request" ).build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ControlledErrorResponseDTO handleException(HttpMessageNotReadableException ex) {
        if(appname == null || appname.isBlank()){
            appname = "AppNameNotSet";
        }
        String errorMessage = "Wrong Body in the request, please check the API definition";
        return new ControlledErrorResponseDTO( "uw.error." + appname + ".httpBody.00",errorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ControlledErrorResponseDTO handleException(MethodArgumentNotValidException ex) {
        if(appname == null || appname.isBlank()){
            appname = "AppNameNotSet";
        }
        return new ControlledErrorResponseDTO( "uw.error." + appname + ".http.arguments.00", "Not Valid Argument " + ex.getParameter());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ControlledErrorResponseDTO handleException(Exception ex) {
        LOGGER.error( "context: ",ex );
        if(appname == null || appname.isBlank()){
            appname = "AppNameNotSet";
        }
        return new ControlledErrorResponseDTO( "uw.error." + appname + ".generic","Es ist ein Fehler aufgetreten. Bitte kontaktieren Sie den Support.");
    }
}
