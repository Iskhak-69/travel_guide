package alatoo.travel_guide.services;

import alatoo.travel_guide.dto.TourPlanDto;
import alatoo.travel_guide.entities.TourPlanEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface TourPlanService {
    List<TourPlanEntity> getAll();

    TourPlanEntity getById(Long id);

    TourPlanEntity create(TourPlanDto newTourPlanDto);

    TourPlanEntity update(TourPlanDto tourPlan, Long id);

    ResponseEntity<Map<String, Object>> delete(Long id);

    TourPlanEntity deleteLandmark(List<Long> landmarkIds, Long tourPlanId);
}
