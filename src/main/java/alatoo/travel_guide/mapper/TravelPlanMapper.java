package alatoo.travel_guide.mapper;

import alatoo.travel_guide.dto.TravelPlanDto;
import alatoo.travel_guide.entities.TravelPlanEntity;
import org.springframework.stereotype.Component;

@Component
public class TravelPlanMapper {
    public TravelPlanEntity toEntity(TravelPlanDto dto) {
        TravelPlanEntity entity = new TravelPlanEntity();
        entity.setPlanName(dto.getPlanName());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        return entity;
    }
}
