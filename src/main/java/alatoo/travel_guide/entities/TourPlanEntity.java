package alatoo.travel_guide.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tour_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourPlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String planName;

    @Column(nullable = false)
    LocalDateTime startDate;

    @Column(nullable = false)
    LocalDateTime endDate;

    @Column(nullable = false)
    Double price;

    @ManyToMany(fetch = FetchType.EAGER)
    List<LandmarkEntity> landmarks;

    public Double calculatePrice() {
        if (landmarks == null || landmarks.isEmpty()) return 0.0;
        return landmarks.stream()
                .mapToDouble(LandmarkEntity::getPrice)
                .sum();
    }

    public void updatePrice() {
        this.price = calculatePrice();
    }
}