package com.mashosoft.airportsService.interfaces.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AirportInfoDTO {

    public String cityName;
    public String country;
    public Integer surfaceExtension;
}
