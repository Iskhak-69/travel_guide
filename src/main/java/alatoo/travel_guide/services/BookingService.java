package alatoo.travel_guide.services;

import alatoo.travel_guide.dto.TravelPlanDto;
import alatoo.travel_guide.entities.TourPlanEntity;
import alatoo.travel_guide.entities.TravelPlanEntity;
import alatoo.travel_guide.entities.UserEntity;

import java.util.List;
import java.util.Map;

public interface BookingService {
    void addTravelPlan(Long userId, TravelPlanDto travelPlanDto);

    void addTourPlan(Long userId, Long tourPlanId);

    List<Map<String, Object>> getAllTourPlans();

    List<Map<String, Object>> getAllTravelPlans();

    List<TravelPlanEntity> getUserTravelPlans(Long userId);

    List<TourPlanEntity> getUserTourPlans(Long userId);

    TravelPlanEntity updateTravelPlan(Long userId, Long travelPlanId, TravelPlanDto travelPlanDto);

    UserEntity updateTourPlan(Long userId, List<Long> tourPlanIds);

    void deleteTravelPlan(Long userId, Long travelPlanId);

    void deleteTourPlan(Long userId, Long tourPlanId);
}
