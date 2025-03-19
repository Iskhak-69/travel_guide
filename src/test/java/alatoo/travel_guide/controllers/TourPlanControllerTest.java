package alatoo.travel_guide.controllers;

import alatoo.travel_guide.dto.TourPlanDto;
import alatoo.travel_guide.entities.TourPlanEntity;
import alatoo.travel_guide.services.TourPlanService;
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
@WebMvcTest(TourPlanController.class)
public class TourPlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TourPlanService tourPlanService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllTourPlans() throws Exception {
        // Mock service response
        TourPlanEntity tourPlan1 = new TourPlanEntity();
        tourPlan1.setId(1L);
        tourPlan1.setPlanName("Paris Tour");

        TourPlanEntity tourPlan2 = new TourPlanEntity();
        tourPlan2.setId(2L);
        tourPlan2.setPlanName("New York Tour");

        List<TourPlanEntity> mockResponse = Arrays.asList(tourPlan1, tourPlan2);
        Mockito.when(tourPlanService.getAll()).thenReturn(mockResponse);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/tour-plans/get-all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }

    @Test
    public void testGetTourPlanById() throws Exception {
        // Mock service response
        Long id = 1L;
        TourPlanEntity mockResponse = new TourPlanEntity();
        mockResponse.setId(id);
        mockResponse.setPlanName("Paris Tour");

        Mockito.when(tourPlanService.getById(id)).thenReturn(mockResponse);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/tour-plans/get/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }

    @Test
    public void testCreateTourPlan() throws Exception {
        // Mock service response
        TourPlanDto newTourPlanDto = TourPlanDto.builder()
                .planName("Paris Tour")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(7))
                .landmarkIds(Arrays.asList(1L, 2L))
                .build();

        TourPlanEntity mockResponse = new TourPlanEntity();
        mockResponse.setId(1L);
        mockResponse.setPlanName(newTourPlanDto.getPlanName());

        Mockito.when(tourPlanService.create(newTourPlanDto)).thenReturn(mockResponse);

        // Perform POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/tour-plans/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTourPlanDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }

    @Test
    public void testUpdateTourPlan() throws Exception {
        // Mock service response
        Long id = 1L;
        TourPlanDto updateDto = TourPlanDto.builder()
                .planName("Updated Tour Plan")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(7))
                .landmarkIds(Arrays.asList(1L, 2L))
                .build();

        TourPlanEntity mockResponse = new TourPlanEntity();
        mockResponse.setId(id);
        mockResponse.setPlanName(updateDto.getPlanName());

        Mockito.when(tourPlanService.update(updateDto, id)).thenReturn(mockResponse);

        // Perform PUT request
        mockMvc.perform(MockMvcRequestBuilders.put("/tour-plans/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }

    @Test
    public void testDeleteTourPlan() throws Exception {
        // Mock service response
        Long id = 1L;
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("message", "Tour plan with id " + id + " is deleted");
        mockResponse.put("deleted tour plan", new TourPlanEntity());

        Mockito.when(tourPlanService.delete(id)).thenReturn(ResponseEntity.ok(mockResponse));

        // Perform DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/tour-plans/delete/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }

    @Test
    public void testDeleteLandmarkFromTourPlan() throws Exception {
        // Mock service response
        Long tourPlanId = 1L;
        List<Long> landmarkIds = Arrays.asList(1L, 2L);
        TourPlanEntity mockResponse = new TourPlanEntity();
        mockResponse.setId(tourPlanId);
        mockResponse.setPlanName("Paris Tour");

        Mockito.when(tourPlanService.deleteLandmark(landmarkIds, tourPlanId)).thenReturn(mockResponse);

        // Perform DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/tour-plans/delete-landmark/{id}", tourPlanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(landmarkIds)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }
}