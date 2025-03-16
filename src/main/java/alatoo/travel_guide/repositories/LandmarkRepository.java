package alatoo.travel_guide.repositories;

import alatoo.travel_guide.entities.LandmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LandmarkRepository extends JpaRepository<LandmarkEntity, Long> {
    Optional<LandmarkEntity> findByTitle(String title);
}