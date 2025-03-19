package alatoo.travel_guide.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourPlanDto {
    private String planName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Long> landmarkIds;
}