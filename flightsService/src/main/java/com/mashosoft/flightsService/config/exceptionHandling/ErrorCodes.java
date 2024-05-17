package com.mashosoft.flightsService.config.exceptionHandling;

public enum ErrorCodes {
    DEPARTURE_CODE_NULL(Prefix.API_PARAMS_ERRORS + "01"),
    LANDING_CODE_NULL(Prefix.API_PARAMS_ERRORS + "02"),
    PRICE_NULL(Prefix.BUSSINESS_ERRORS + "01"),
    PRICE_NEGATUVE(Prefix.BUSSINESS_ERRORS + "02"),
    NOT_VALID_AIRPORT_CODE(Prefix.BUSSINESS_ERRORS + "03"),
    ;

    ErrorCodes(String errorCode) {
        this.errorCode = errorCode;
    }

    private String errorCode;

    public String getCode() {
        return errorCode;
    }

    public static class Prefix {
        static final String API_PARAMS_ERRORS = "flights.api.params.validation.error.";
        static final String BUSSINESS_ERRORS = "flights.bussiness.error.";
    }

}
