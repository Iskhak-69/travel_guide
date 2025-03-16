package alatoo.travel_guide.services;

import alatoo.travel_guide.dto.LandmarkDto;
import alatoo.travel_guide.entities.LandmarkEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface LandmarkService {
    List<LandmarkEntity> getAll();

    LandmarkEntity getById(Long id);

    LandmarkEntity create(LandmarkEntity newLandmark);

    LandmarkEntity update(LandmarkDto landmark, Long id);

    ResponseEntity<Map<String, Object>> delete(Long id);
}
