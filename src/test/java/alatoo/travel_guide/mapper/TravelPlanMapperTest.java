package alatoo.travel_guide.mapper;

import alatoo.travel_guide.dto.TravelPlanDto;
import alatoo.travel_guide.entities.TravelPlanEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TravelPlanMapperTest {

    @Autowired
    private TravelPlanMapper travelPlanMapper;

    @Test
    public void testTravelPlanDtoToTravelPlan() {
        TravelPlanDto dto = TravelPlanDto.builder()
                .planName("Europe Trip")
                .destination("Paris")
                .startDate(LocalDateTime.of(2023, 12, 1, 10, 0))
                .endDate(LocalDateTime.of(2023, 12, 10, 18, 0))
                .landmarkIds(Collections.emptyList())
                .build();

        TravelPlanEntity entity = travelPlanMapper.travelPlanDtoToTravelPlan(dto);

        assertNotNull(entity);
        assertEquals(dto.getPlanName(), entity.getPlanName());
        assertEquals(dto.getDestination(), entity.getDestination()); // Ensure destination is mapped
        assertEquals(dto.getStartDate(), entity.getStartDate());
        assertEquals(dto.getEndDate(), entity.getEndDate());
    }

    @Test
    public void testTravelPlanToTravelPlanDto() {
        TravelPlanEntity entity = new TravelPlanEntity();
        entity.setPlanName("Europe Trip");
        entity.setDestination("Paris");
        entity.setStartDate(LocalDateTime.of(2023, 12, 1, 10, 0));
        entity.setEndDate(LocalDateTime.of(2023, 12, 10, 18, 0));
        entity.setLandmarks(Collections.emptyList());

        TravelPlanDto dto = travelPlanMapper.travelPlanToTravelPlanDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getPlanName(), dto.getPlanName());
        assertEquals(entity.getDestination(), dto.getDestination()); // Ensure destination is mapped
        assertEquals(entity.getStartDate(), dto.getStartDate());
        assertEquals(entity.getEndDate(), dto.getEndDate());
    }
}