package alatoo.travel_guide.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    String password;

    LocalDateTime createdAt;

    @Column(nullable = false, unique = true)
    String username;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<RoleEntity> roles = new HashSet<>();



    @PrePersist
    private void init() {
        this.createdAt = LocalDateTime.now();
    }

    @OneToMany(fetch = FetchType.EAGER)
    List<TravelPlanEntity> travelPlan;

    @ManyToMany(fetch = FetchType.EAGER)
    List<TourPlanEntity> tourPlan;

    boolean verified = false;
}