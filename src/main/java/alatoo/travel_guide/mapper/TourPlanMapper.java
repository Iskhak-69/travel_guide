package alatoo.travel_guide.mapper;

import alatoo.travel_guide.dto.TourPlanDto;
import alatoo.travel_guide.entities.TourPlanEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TourPlanMapper {
    TourPlanEntity tourPlanDtoToTourPlan(TourPlanDto tourPlanDto);
    TourPlanDto tourPlanToTourPlanDto(TourPlanEntity tourPlan);
}