package alatoo.travel_guide.controllers;

import alatoo.travel_guide.dto.TravelPlanDto;
import alatoo.travel_guide.entities.TravelPlanEntity;
import alatoo.travel_guide.services.TravelPlanService;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TravelPlanController.class)
public class TravelPlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TravelPlanService travelPlanService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllTravelPlans() throws Exception {
        // Mock service response
        TravelPlanEntity travelPlan1 = new TravelPlanEntity();
        travelPlan1.setId(1L);
        travelPlan1.setPlanName("Paris Trip");

        TravelPlanEntity travelPlan2 = new TravelPlanEntity();
        travelPlan2.setId(2L);
        travelPlan2.setPlanName("New York Trip");

        List<TravelPlanEntity> mockResponse = Arrays.asList(travelPlan1, travelPlan2);
        Mockito.when(travelPlanService.getAll()).thenReturn(mockResponse);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/travel-plans/get-all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }

    @Test
    public void testGetTravelPlanById() throws Exception {
        // Mock service response
        Long id = 1L;
        TravelPlanEntity mockResponse = new TravelPlanEntity();
        mockResponse.setId(id);
        mockResponse.setPlanName("Paris Trip");

        Mockito.when(travelPlanService.getById(id)).thenReturn(mockResponse);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/travel-plans/get/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }

    @Test
    public void testCreateTravelPlan() throws Exception {
        // Mock service response
        TravelPlanDto newTravelPlanDto = TravelPlanDto.builder()
                .planName("Paris Trip")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(7))
                .landmarkIds(Arrays.asList(1L, 2L))
                .build();

        TravelPlanEntity mockResponse = new TravelPlanEntity();
        mockResponse.setId(1L);
        mockResponse.setPlanName(newTravelPlanDto.getPlanName());

        Mockito.when(travelPlanService.create(newTravelPlanDto)).thenReturn(mockResponse);

        // Perform POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/travel-plans/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTravelPlanDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }

    @Test
    public void testUpdateTravelPlan() throws Exception {
        // Mock service response
        Long id = 1L;
        TravelPlanDto updateDto = TravelPlanDto.builder()
                .planName("Updated Travel Plan")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(7))
                .landmarkIds(Arrays.asList(1L, 2L))
                .build();

        TravelPlanEntity mockResponse = new TravelPlanEntity();
        mockResponse.setId(id);
        mockResponse.setPlanName(updateDto.getPlanName());

        Mockito.when(travelPlanService.update(updateDto, id)).thenReturn(mockResponse);

        // Perform PUT request
        mockMvc.perform(MockMvcRequestBuilders.put("/travel-plans/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }

    @Test
    public void testDeleteTravelPlan() throws Exception {
        // Mock service response
        Long id = 1L;
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("message", "Travel plan with id " + id + " is deleted");
        mockResponse.put("deleted travel plan", new TravelPlanEntity());

        Mockito.when(travelPlanService.delete(id)).thenReturn(ResponseEntity.ok(mockResponse));

        // Perform DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/travel-plans/delete/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }

    @Test
    public void testDeleteLandmarkFromTravelPlan() throws Exception {
        // Mock service response
        Long travelPlanId = 1L;
        List<Long> landmarkIds = Arrays.asList(1L, 2L);
        TravelPlanEntity mockResponse = new TravelPlanEntity();
        mockResponse.setId(travelPlanId);
        mockResponse.setPlanName("Paris Trip");

        Mockito.when(travelPlanService.deleteLandmark(landmarkIds, travelPlanId)).thenReturn(mockResponse);

        // Perform DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/travel-plans/delete-landmark/{id}", travelPlanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(landmarkIds)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }
}