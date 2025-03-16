package alatoo.travel_guide.repositories;

import alatoo.travel_guide.entities.TravelPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelPlanRepository extends JpaRepository<TravelPlanEntity, Long> {
}
