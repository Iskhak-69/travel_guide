package alatoo.travel_guide.repositories;

import alatoo.travel_guide.entities.LandmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandmarkRepository extends JpaRepository<LandmarkEntity, Long> {
}
