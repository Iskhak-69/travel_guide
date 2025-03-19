package alatoo.travel_guide.mapper;

import alatoo.travel_guide.dto.TourPlanDto;
import alatoo.travel_guide.entities.TourPlanEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TourPlanMapperTest {

    @Autowired
    private TourPlanMapper tourPlanMapper;

    @Test
    public void testTourPlanDtoToTourPlan() {
        TourPlanDto dto = new TourPlanDto();
        dto.setPlanName("Paris Tour");
        dto.setStartDate(LocalDateTime.of(2023, 12, 1, 10, 0));
        dto.setEndDate(LocalDateTime.of(2023, 12, 5, 18, 0));

        TourPlanEntity entity = tourPlanMapper.tourPlanDtoToTourPlan(dto);

        assertNotNull(entity);
        assertEquals(dto.getPlanName(), entity.getPlanName());
        assertEquals(dto.getStartDate(), entity.getStartDate());
        assertEquals(dto.getEndDate(), entity.getEndDate());
    }

    @Test
    public void testTourPlanToTourPlanDto() {
        TourPlanEntity entity = new TourPlanEntity();
        entity.setPlanName("Paris Tour");
        entity.setStartDate(LocalDateTime.of(2023, 12, 1, 10, 0));
        entity.setEndDate(LocalDateTime.of(2023, 12, 5, 18, 0));
        entity.setPrice(500.0);
        entity.setLandmarks(Collections.emptyList());

        TourPlanDto dto = tourPlanMapper.tourPlanToTourPlanDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getPlanName(), dto.getPlanName());
        assertEquals(entity.getStartDate(), dto.getStartDate());
        assertEquals(entity.getEndDate(), dto.getEndDate());
    }
}