package alatoo.travel_guide.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "landmarks")
//@Getter
//@Setter
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LandmarkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String title;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    String location;

    @Column(nullable = false)
    Double price;

    String imageUrl;
}
