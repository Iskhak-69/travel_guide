package alatoo.travel_guide.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "travel_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TravelPlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String planName;

    @Column(nullable = false)
    String destination;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime startDate;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime endDate;

    @Column(nullable = false)
    Double price;

    @ManyToMany(fetch = FetchType.EAGER)
    List<LandmarkEntity> landmarks;

    public Double calculatePrice() {
        if (landmarks == null || landmarks.isEmpty()) return 0.0;
        double sum = landmarks.stream().mapToDouble(LandmarkEntity::getPrice).sum();
        return sum + sum * 0.10;
    }

    public void updatePrice() {
        this.price = calculatePrice();
    }
}
