package alatoo.travel_guide.controllers;

import alatoo.travel_guide.dto.LandmarkDto;
import alatoo.travel_guide.entities.LandmarkEntity;
import alatoo.travel_guide.services.LandmarkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LandmarkController.class)
public class LandmarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LandmarkService landmarkService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllLandmarks() throws Exception {
        // Mock service response
        LandmarkEntity landmark1 = new LandmarkEntity();
        landmark1.setId(1L);
        landmark1.setTitle("Eiffel Tower");

        LandmarkEntity landmark2 = new LandmarkEntity();
        landmark2.setId(2L);
        landmark2.setTitle("Statue of Liberty");

        List<LandmarkEntity> mockResponse = Arrays.asList(landmark1, landmark2);
        Mockito.when(landmarkService.getAll()).thenReturn(mockResponse);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/landmarks/get-all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }

    @Test
    public void testGetLandmarkById() throws Exception {
        // Mock service response
        Long id = 1L;
        LandmarkEntity mockResponse = new LandmarkEntity();
        mockResponse.setId(id);
        mockResponse.setTitle("Eiffel Tower");

        Mockito.when(landmarkService.getById(id)).thenReturn(mockResponse);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/landmarks/get/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }

    @Test
    public void testCreateLandmark() throws Exception {
        // Mock service response
        LandmarkEntity newLandmark = new LandmarkEntity();
        newLandmark.setTitle("Eiffel Tower");
        newLandmark.setDescription("A famous landmark in Paris.");
        newLandmark.setLocation("Paris, France");
        newLandmark.setPrice(25.0);

        Mockito.when(landmarkService.create(newLandmark)).thenReturn(newLandmark);

        // Perform POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/landmarks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newLandmark)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(newLandmark)));
    }

    @Test
    public void testUpdateLandmark() throws Exception {
        // Mock service response
        Long id = 1L;
        LandmarkDto updateDto = new LandmarkDto();
        updateDto.setTitle("Updated Title");
        updateDto.setDescription("Updated Description");
        updateDto.setPrice(30.0);

        LandmarkEntity updatedLandmark = new LandmarkEntity();
        updatedLandmark.setId(id);
        updatedLandmark.setTitle(updateDto.getTitle());
        updatedLandmark.setDescription(updateDto.getDescription());
        updatedLandmark.setPrice(updateDto.getPrice());

        Mockito.when(landmarkService.update(updateDto, id)).thenReturn(updatedLandmark);

        // Perform PUT request
        mockMvc.perform(MockMvcRequestBuilders.put("/landmarks/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedLandmark)));
    }

    @Test
    public void testDeleteLandmark() throws Exception {
        // Mock service response
        Long id = 1L;
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("message", "Landmark with id " + id + " is deleted");
        mockResponse.put("deleted landmark", new LandmarkEntity());

        Mockito.when(landmarkService.delete(id)).thenReturn(ResponseEntity.ok(mockResponse));

        // Perform DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/landmarks/delete/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }
}