package alatoo.travel_guide.mapper;

import alatoo.travel_guide.dto.TravelPlanDto;
import alatoo.travel_guide.entities.TravelPlanEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TravelPlanMapper {
    TravelPlanEntity travelPlanDtoToTravelPlan(TravelPlanDto travelPlanDto);
    TravelPlanDto travelPlanToTravelPlanDto(TravelPlanEntity travelPlan);
}