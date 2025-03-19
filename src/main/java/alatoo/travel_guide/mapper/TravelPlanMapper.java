package alatoo.travel_guide.mapper;

import alatoo.travel_guide.dto.TravelPlanDto;
import alatoo.travel_guide.entities.TravelPlanEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TravelPlanMapper {

    TravelPlanMapper INSTANCE = Mappers.getMapper(TravelPlanMapper.class);

    @Mapping(target = "landmarks", ignore = true) // Ignore landmarks for now
    TravelPlanEntity travelPlanDtoToTravelPlan(TravelPlanDto travelPlanDto);

    @Mapping(target = "landmarkIds", ignore = true) // Ignore landmarkIds for now
    TravelPlanDto travelPlanToTravelPlanDto(TravelPlanEntity travelPlan);
}