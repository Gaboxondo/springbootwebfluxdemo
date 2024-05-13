package com.mashosoft.flightsService.config.exceptionHandling.handler;

import com.mashosoft.flightsService.config.exceptionHandling.handler.model.ExceptionDTO;
import com.mashosoft.flightsService.config.exceptionHandling.model.exception.BussinessException;
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


    @ExceptionHandler(BussinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handleException(BussinessException ex) {
        return new ExceptionDTO(ex.getErrorCode(),ex.getErrorMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handleException(HttpMessageNotReadableException ex) {
        if(appname == null || appname.isBlank()){
            appname = "AppNameNotSet";
        }
        String errorMessage = "Wrong Body in the request, please check the API definition";
        return new ExceptionDTO("uw.error." + appname + ".httpBody.00",errorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handleException(MethodArgumentNotValidException ex) {
        if(appname == null || appname.isBlank()){
            appname = "AppNameNotSet";
        }
        return new ExceptionDTO("uw.error." + appname + ".http.arguments.00","Not Valid Argument " + ex.getParameter());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDTO handleException(Exception ex) {
        LOGGER.error( "context: ",ex );
        if(appname == null || appname.isBlank()){
            appname = "AppNameNotSet";
        }
        return new ExceptionDTO("uw.error." + appname + ".generic","Es ist ein Fehler aufgetreten. Bitte kontaktieren Sie den Support.");
    }
}
