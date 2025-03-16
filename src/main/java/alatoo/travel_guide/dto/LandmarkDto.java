package alatoo.travel_guide.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LandmarkDto {
    String title;
    String description;
    String location;
    Double price;
    String imageUrl;
}