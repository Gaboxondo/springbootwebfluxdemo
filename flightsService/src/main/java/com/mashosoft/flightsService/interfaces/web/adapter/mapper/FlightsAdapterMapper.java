package com.mashosoft.flightsService.interfaces.web.adapter.mapper;

import com.mashosoft.flightsService.domain.model.Flight;
import com.mashosoft.flightsService.interfaces.web.dto.FlightDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class FlightsAdapterMapper {

    public FlightDTO fromDomainToDTO(Flight flight){
        if(flight == null){
            return null;
        }
        FlightDTO flightDTO = new FlightDTO();
        BeanUtils.copyProperties( flight,flightDTO );
        return flightDTO;
    }

}
