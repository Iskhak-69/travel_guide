package alatoo.travel_guide.services;

import alatoo.travel_guide.dto.TravelPlanDto;
import alatoo.travel_guide.entities.TravelPlanEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface TravelPlanService {
    List<TravelPlanEntity> getAll();

    TravelPlanEntity getById(Long id);

    TravelPlanEntity create(TravelPlanEntity newTravelPlan);

    TravelPlanEntity create(TravelPlanDto newTravelPlanDto);

    TravelPlanEntity update(TravelPlanDto travelPlan, Long id);

    ResponseEntity<Map<String, Object>> delete(Long id);

    TravelPlanEntity deleteLandmark(List<Long> landmarkIds, Long travelPlanId);
}
