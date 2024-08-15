package com.mendes.locations_manager.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mendes.locations_manager.dto.LocationRequestDTO;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LocationControllerIntegrationTest {

    final String PATH_END_POINT = "/api/locations";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenGetInvalidResource_thenReturnNotFound() throws Exception {
        mockMvc.perform(get("/invalidURI"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").value("No static resource invalidURI."));
    }

    @Test
    @Sql(scripts = "/data/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void whenGellAllLocations_thenReturnOk() throws Exception {
        mockMvc.perform(get(PATH_END_POINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Test name 2"))
                .andExpect(jsonPath("$[0].neighborhood").value("Test neighborhood 2"))
                .andExpect(jsonPath("$[0].city").value("Test city 2"))
                .andExpect(jsonPath("$[0].state").value("Test state 2"))
                .andExpect(jsonPath("$[1].id").value(1))
                .andExpect(jsonPath("$[1].name").value("Test name 1"))
                .andExpect(jsonPath("$[1].neighborhood").value("Test neighborhood 1"))
                .andExpect(jsonPath("$[1].city").value("Test city 1"))
                .andExpect(jsonPath("$[1].state").value("Test state 1"))
                .andExpect(jsonPath("$[2].id").doesNotExist());
    }

    @Test
    @Sql(scripts = "/data/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void whenGetLocationById_thenReturnOk() throws Exception {
        final Integer ID = 2;
        
        mockMvc.perform(get(String.format("%s/%s", PATH_END_POINT, ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value("Test name 2"))
                .andExpect(jsonPath("$.neighborhood").value("Test neighborhood 2"))
                .andExpect(jsonPath("$.city").value("Test city 2"))
                .andExpect(jsonPath("$.state").value("Test state 2"));
    }
    
    @Test
    public void whenGetLocationByIdWithInvalidId_thenReturnBadRequest() throws Exception {
        mockMvc.perform(get(String.format("%s/%s", PATH_END_POINT, -1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("ID must be a positive Integer."));

        mockMvc.perform(get(String.format("%s/%s", PATH_END_POINT, "InvalidID")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("ID must be a positive Integer."));
    }

    @Test
    public void whenGetLocationByIdWithLocationNotFound_thenReturnNotFound() throws Exception {
        final Integer ID = 1;

        mockMvc.perform(get(String.format("%s/%s", PATH_END_POINT, ID)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").value(String.format("Location with ID %s not found.", ID)));
    }

    @Test
    public void whenPostLocation_thenReturnCreated() throws Exception{
        final Integer ID = 1;
        final String LOCATION = String.format("http://localhost%s/%s", PATH_END_POINT, ID); // No port is defined since the rest api is not running 

        LocationRequestDTO locationRequestDTO = new LocationRequestDTO();
            locationRequestDTO.setName("Test name");
            locationRequestDTO.setNeighborhood("Test neighborhood");
            locationRequestDTO.setCity("Test city");
            locationRequestDTO.setState("Test state");

        mockMvc.perform(post(PATH_END_POINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(locationRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", LOCATION))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(locationRequestDTO.getName()))
                .andExpect(jsonPath("$.neighborhood").value(locationRequestDTO.getNeighborhood()))
                .andExpect(jsonPath("$.city").value(locationRequestDTO.getCity()))
                .andExpect(jsonPath("$.state").value(locationRequestDTO.getState()));
    }

    @Test
    public void whenPostLocationWithMissingField_thenReturnBadRequest() throws Exception {
        LocationRequestDTO locationRequestDTO = new LocationRequestDTO();
            locationRequestDTO.setName("Test name");
            locationRequestDTO.setCity("Test city");
            locationRequestDTO.setState("Test state");

        mockMvc.perform(post(PATH_END_POINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(locationRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("The field `neighborhood` cannot be empty."));
    }

    @Test
    public void whenPostLocationWithInvalidField_thenReturnBadRequest() throws Exception {
        final Integer MAX = 50;

        LocationRequestDTO locationRequestDTO = new LocationRequestDTO();
            locationRequestDTO.setName("Test name");
            locationRequestDTO.setNeighborhood("Test neigborhood");
            locationRequestDTO.setCity("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ");
            locationRequestDTO.setState("Test state");

        mockMvc.perform(post(PATH_END_POINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(locationRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value(String.format("The field `city` can have a maximum of %s characters.", MAX)));
    }

    @Test
    @Sql(scripts = "/data/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void whenUpdateLocation_thenReturnOK() throws Exception{
        final Integer ID = 1;
        LocationRequestDTO locationRequestDTO = new LocationRequestDTO();
            locationRequestDTO.setName("Test name 1");
            locationRequestDTO.setNeighborhood("Test new neighborhood");
            locationRequestDTO.setCity("Test city 1");
            locationRequestDTO.setState("Test state 1");

        mockMvc.perform(put(String.format("%s/%s", PATH_END_POINT, ID))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(locationRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(locationRequestDTO.getName()))
                .andExpect(jsonPath("$.neighborhood").value(locationRequestDTO.getNeighborhood()))
                .andExpect(jsonPath("$.city").value(locationRequestDTO.getCity()))
                .andExpect(jsonPath("$.state").value(locationRequestDTO.getState()));
    }

    @Test
    public void whenUpdateLocationWithMissingField_thenReturnBadRequest() throws Exception {
        final Integer ID = 1;

        LocationRequestDTO locationRequestDTO = new LocationRequestDTO();
            locationRequestDTO.setName("Test name");
            locationRequestDTO.setCity("Test city");
            locationRequestDTO.setState("Test state");

        mockMvc.perform(put(String.format("%s/%s", PATH_END_POINT, ID))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(locationRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("The field `neighborhood` cannot be empty."));
    }

    @Test
    public void whenUpdateLocationWithInvalidField_thenReturnBadRequest() throws Exception {
        final Integer ID = 1;
        final Integer MAX = 50;

        LocationRequestDTO locationRequestDTO = new LocationRequestDTO();
            locationRequestDTO.setName("Test name");
            locationRequestDTO.setNeighborhood("Test neigborhood");
            locationRequestDTO.setCity("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ");
            locationRequestDTO.setState("Test state");

        mockMvc.perform(put(String.format("%s/%s", PATH_END_POINT, ID))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(locationRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value(String.format("The field `city` can have a maximum of %s characters.", MAX)));
    }

    @Test
    public void whenUpdateLocationByIdWithInvalidId_thenReturnBadRequest() throws Exception {
        LocationRequestDTO locationRequestDTO = new LocationRequestDTO();
            locationRequestDTO.setName("Test name");
            locationRequestDTO.setNeighborhood("Test neighborhood");
            locationRequestDTO.setCity("Test city");
            locationRequestDTO.setState("Test state");
            
        mockMvc.perform(put(String.format("%s/%s", PATH_END_POINT, "-1"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(locationRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("ID must be a positive Integer."));

        mockMvc.perform(put(String.format("%s/%s", PATH_END_POINT, "invalidID"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(locationRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("ID must be a positive Integer."));
    }

    @Test
    public void whenUpdateLocationByIdWithLocationNotFound_thenReturnNotFound() throws Exception {
        final Integer ID = 1;

        LocationRequestDTO locationRequestDTO = new LocationRequestDTO();
            locationRequestDTO.setName("Test name");
            locationRequestDTO.setNeighborhood("Test neighborhood");
            locationRequestDTO.setCity("Test city");
            locationRequestDTO.setState("Test state");

        mockMvc.perform(put(String.format("%s/%s", PATH_END_POINT, ID))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(locationRequestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").value(String.format("Location with ID %s not found.", ID)));
    }

    @Test
    public void whenDeleteLocationById_thenReturnNoContent() throws Exception {
        final Integer ID = 1;

        mockMvc.perform(delete(String.format("%s/%s", PATH_END_POINT, ID)))
                .andExpect(status().isNoContent());
    }
 
    @Test
    public void whenDeleteLocationByIdWithInvalidId_thenReturnBadRequest() throws Exception {
        mockMvc.perform(delete(PATH_END_POINT + "/InvalidID"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("ID must be a positive Integer."));
    }

}
