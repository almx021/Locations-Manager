package com.mendes.desafio_tecnico_backend_nuven.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mendes.desafio_tecnico_backend_nuven.dto.LocationDTO;
import com.mendes.desafio_tecnico_backend_nuven.dto.LocationRequestDTO;
import com.mendes.desafio_tecnico_backend_nuven.dto.mapper.LocationMapper;
import com.mendes.desafio_tecnico_backend_nuven.model.Location;
import com.mendes.desafio_tecnico_backend_nuven.repository.LocationRepository;

@Service
public class LocationService {
    
    @Autowired
    LocationRepository locationRepository;

    @Autowired
    LocationMapper locationMapper;

    public List<LocationDTO> findAllLocations() {
        List<Location> foundLocations = locationRepository.findAllByOrderByCreatedAtAsc();
        return foundLocations
                .stream()
                .map(location -> locationMapper.toDTO(location))
                .collect(Collectors.toList());
    };

    public LocationDTO findLocationById(Integer id) {
        Location targetLocation = locationRepository.findById(id)
                                                    .orElseThrow(() -> new RuntimeException());
        return locationMapper.toDTO(targetLocation);
    }

    public LocationDTO saveLocation(LocationRequestDTO locationRequestDTO) {
        Location locationRequest = locationMapper.toEntity(locationRequestDTO);
        return locationMapper.toDTO(locationRepository.save(locationRequest));
    }

    public LocationDTO updateLocation(Integer id, LocationRequestDTO locationRequestDTO) {
        Location targetLocation = locationRepository.findById(id)
                                                    .orElseThrow(() -> new RuntimeException());

        targetLocation.setName(locationRequestDTO.getName());
        targetLocation.setNeighborhood(locationRequestDTO.getNeighborhood());
        targetLocation.setCity(locationRequestDTO.getCity());
        targetLocation.setState(locationRequestDTO.getState());
        
        return locationMapper.toDTO(locationRepository.save(targetLocation));
    };

    public void deleteLocationById(Integer id) {
        locationRepository.deleteById(id);
    };
}
