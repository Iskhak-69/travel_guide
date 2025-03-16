package alatoo.travel_guide.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "travel_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String planName;

    @NotNull
    private String startDate;

    @NotNull
    private String endDate;

    @ManyToMany
    @JoinTable(
            name = "travel_plan_landmarks",
            joinColumns = @JoinColumn(name = "travel_plan_id"),
            inverseJoinColumns = @JoinColumn(name = "landmark_id")
    )
    private List<LandmarkEntity> landmarks;
}
