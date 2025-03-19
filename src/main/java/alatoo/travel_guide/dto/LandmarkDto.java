package alatoo.travel_guide.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LandmarkDto {
    private String title;
    private String description;
    private String location;
    private Double price;
    private String imageUrl;
}