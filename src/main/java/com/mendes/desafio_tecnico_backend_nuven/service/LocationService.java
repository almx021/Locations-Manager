package com.mendes.desafio_tecnico_backend_nuven.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mendes.desafio_tecnico_backend_nuven.dto.LocationDTO;
import com.mendes.desafio_tecnico_backend_nuven.dto.LocationRequestDTO;
import com.mendes.desafio_tecnico_backend_nuven.dto.mapper.LocationMapper;
import com.mendes.desafio_tecnico_backend_nuven.exception.LocationNotFoundException;
import com.mendes.desafio_tecnico_backend_nuven.model.Location;
import com.mendes.desafio_tecnico_backend_nuven.repository.LocationRepository;

/**
 * Service Layer responsible for operations related to locations.
 */
@Service
public class LocationService {
    
    @Autowired
    LocationRepository locationRepository;

    @Autowired
    LocationMapper locationMapper;

    /**
     * Finds all locations.
     *
     * @return A list of {@link LocationDTO} objects ordered by creation date.
     */
    public List<LocationDTO> findAllLocations() {
        List<Location> foundLocations = locationRepository.findAllByOrderByCreatedAtAsc();
        return foundLocations
                .stream()
                .map(location -> locationMapper.toDTO(location))
                .collect(Collectors.toList());
    };

    /**
     * Fetches a location by its ID.
     *
     * @param id The ID of the location to be retrieved.
     * @return The {@link LocationDTO} object containing the details of the location.
     * @throws LocationNotFoundException If no locations with the given ID was found.
     */
    public LocationDTO findLocationById(Integer id) {
        Location targetLocation = locationRepository.findById(id)
                                                    .orElseThrow(() -> new LocationNotFoundException(id));
        return locationMapper.toDTO(targetLocation);
    }

    /**
     * Creates a new location.
     *
     * @param locationRequestDTO The {@link LocationRequestDTO} object containing the details of the location to be created, such as name.
     * @return A {@link LocationDTO} object containing the details of the newly created location.
     */
    public LocationDTO saveLocation(LocationRequestDTO locationRequestDTO) {
        Location locationRequest = locationMapper.toEntity(locationRequestDTO);
        return locationMapper.toDTO(locationRepository.save(locationRequest));
    }

    /**
     * Fetches a location by its ID and the updates it with received values.
     *
     * @param id The ID of the location to be retrieved.
     * @return A {@link LocationDTO} object containing the details of the updated location.
     * @throws LocationNotFoundException If no locations with the given ID was found.
     */
    public LocationDTO updateLocation(Integer id, LocationRequestDTO locationRequestDTO) {
        Location targetLocation = locationRepository.findById(id)
                                                    .orElseThrow(() -> new LocationNotFoundException(id));

        targetLocation.setName(locationRequestDTO.getName());
        targetLocation.setNeighborhood(locationRequestDTO.getNeighborhood());
        targetLocation.setCity(locationRequestDTO.getCity());
        targetLocation.setState(locationRequestDTO.getState());
        
        return locationMapper.toDTO(locationRepository.save(targetLocation));
    };

    /**
     * Deletes a location by its ID.
     * 
     * @param id The ID of the location to be retrieved.
     */
    public void deleteLocationById(Integer id) {
        locationRepository.deleteById(id);
    };
}
