package alatoo.travel_guide.dto;

import lombok.Data;

import java.util.List;

@Data
public class TravelPlanDto {
    private String planName;
    private String startDate;
    private String endDate;
    private List<Long> landmarkIds;
}
