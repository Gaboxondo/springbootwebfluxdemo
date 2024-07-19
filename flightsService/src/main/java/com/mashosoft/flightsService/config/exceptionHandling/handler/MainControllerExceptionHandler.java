package com.mashosoft.flightsService.config.exceptionHandling.handler;

import com.mashosoft.flightsService.config.exceptionHandling.handler.model.ControlledErrorResponseDTO;
import com.mashosoft.flightsService.config.exceptionHandling.handler.model.ServerErrorResponseDTO;
import com.mashosoft.flightsService.config.exceptionHandling.model.exception.ControlledErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.resource.NoResourceFoundException;

@ControllerAdvice
@RestController
public class MainControllerExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger( MainControllerExceptionHandler.class);

    @Value("${spring.application.name:unknown}")
    private String appname;


    @ExceptionHandler(ControlledErrorException.class)
    public ResponseEntity<ControlledErrorResponseDTO> handleException(ControlledErrorException ex) {
        ControlledErrorResponseDTO controlledErrorException = new ControlledErrorResponseDTO( ex.getErrorCode(),ex.getErrorMessage() );
        return new ResponseEntity<>(controlledErrorException,ex.getHttpStatusResponse() );
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

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ControlledErrorResponseDTO handleException(NoResourceFoundException ex) {
        if(appname == null || appname.isBlank()){
            appname = "AppNameNotSet";
        }
        return new ControlledErrorResponseDTO( "uw.error." + appname + ".http.service.01", "Not found service " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServerErrorResponseDTO handleException(Exception ex) {
        LOGGER.error( "context: ",ex );
        if(appname == null || appname.isBlank()){
            appname = "AppNameNotSet";
        }
        //Here the thing would be also to add the request key, but in this case no request keys are generated
        return new ServerErrorResponseDTO("SERVER_ERROR","server error, please contact with support");
    }
}
