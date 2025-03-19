package alatoo.travel_guide.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelPlanDto {
    private String planName;
    private String destination;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Long> landmarkIds;
}