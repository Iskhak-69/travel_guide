package alatoo.travel_guide.controllers;

import alatoo.travel_guide.dto.TravelPlanDto;
import alatoo.travel_guide.entities.TourPlanEntity;
import alatoo.travel_guide.entities.TravelPlanEntity;
import alatoo.travel_guide.entities.UserEntity;
import alatoo.travel_guide.services.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllTourPlans() throws Exception {
        // Mock service response
        List<Map<String, Object>> mockResponse = Collections.singletonList(new HashMap<>());
        Mockito.when(bookingService.getAllTourPlans()).thenReturn(mockResponse);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/all-tour-plans"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }

    @Test
    public void testGetUserTourPlans() throws Exception {
        // Mock service response
        Long userId = 1L;
        List<TourPlanEntity> mockResponse = Arrays.asList(new TourPlanEntity(), new TourPlanEntity());
        Mockito.when(bookingService.getUserTourPlans(userId)).thenReturn(mockResponse);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/tour/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }

    @Test
    public void testAddTourPlan() throws Exception {
        // Mock service response
        Long userId = 1L;
        Long tourPlanId = 1L;

        // Perform POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/bookings/tour/{userId}/{tourPlanId}", userId, tourPlanId))
                .andExpect(status().isOk())
                .andExpect(content().string("Tour plan added"));
    }

    @Test
    public void testUpdateTourPlan() throws Exception {
        // Mock service response
        Long userId = 1L;
        List<Long> tourPlanIds = Arrays.asList(1L, 2L);
        UserEntity mockResponse = new UserEntity();
        Mockito.when(bookingService.updateTourPlan(userId, tourPlanIds)).thenReturn(mockResponse);

        // Perform PUT request
        mockMvc.perform(MockMvcRequestBuilders.put("/bookings/tour/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tourPlanIds)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }

    @Test
    public void testDeleteTourPlan() throws Exception {
        // Mock service response
        Long userId = 1L;
        Long tourPlanId = 1L;

        // Perform DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/bookings/tour/{userId}/{tourPlanId}", userId, tourPlanId))
                .andExpect(status().isOk())
                .andExpect(content().string("Tour plan deleted"));
    }

    @Test
    public void testAddTravelPlan() throws Exception {
        // Mock service response
        Long userId = 1L;
        TravelPlanDto travelPlanDto = new TravelPlanDto();
        travelPlanDto.setPlanName("Europe Trip");
        travelPlanDto.setStartDate(LocalDateTime.now());
        travelPlanDto.setEndDate(LocalDateTime.now().plusDays(7));
        travelPlanDto.setLandmarkIds(Arrays.asList(1L, 2L));

        // Perform POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/bookings/travel/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(travelPlanDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Travel plan added"));
    }
}