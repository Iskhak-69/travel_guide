package alatoo.travel_guide.repositories;


import alatoo.travel_guide.entities.TourPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourPlanRepository extends JpaRepository<TourPlanEntity, Long> {
}