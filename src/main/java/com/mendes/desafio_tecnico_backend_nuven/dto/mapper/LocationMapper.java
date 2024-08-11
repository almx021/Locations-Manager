package com.mendes.desafio_tecnico_backend_nuven.dto.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mendes.desafio_tecnico_backend_nuven.dto.LocationRequestDTO;
import com.mendes.desafio_tecnico_backend_nuven.dto.LocationDTO;
import com.mendes.desafio_tecnico_backend_nuven.model.Location;

@Component
public class LocationMapper {
    
    @Autowired
    private ModelMapper mapper;

    public Location toEntity(LocationRequestDTO dto) {
        Location location = mapper.map(dto, Location.class);
        return location;
    }

    public LocationDTO toDTO(Location entity) {
        LocationDTO dto = mapper.map(entity, LocationDTO.class);
        return dto;
    }
}
