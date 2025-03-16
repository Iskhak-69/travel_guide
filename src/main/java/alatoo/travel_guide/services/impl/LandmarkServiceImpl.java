package alatoo.travel_guide.services.impl;

import alatoo.travel_guide.services.LandmarkService;
import alatoo.travel_guide.dto.LandmarkDto;
import alatoo.travel_guide.entities.LandmarkEntity;
import alatoo.travel_guide.entities.TourPlanEntity;
import alatoo.travel_guide.entities.TravelPlanEntity;
import alatoo.travel_guide.exceptions.ApiException;
import alatoo.travel_guide.repositories.LandmarkRepository;
import alatoo.travel_guide.repositories.TourPlanRepository;
import alatoo.travel_guide.repositories.TravelPlanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class LandmarkServiceImpl implements LandmarkService {
    @Autowired
    private LandmarkRepository landmarkRepository;

    @Autowired
    private TourPlanRepository tourPlanRepository;

    @Autowired
    private TravelPlanRepository travelPlanRepository;

    @Override
    public List<LandmarkEntity> getAll() {
        return landmarkRepository.findAll();
    }

    @Override
    public LandmarkEntity getById(Long id) {
        return landmarkRepository.findById(id).orElseThrow(() -> new ApiException("Landmark with id " + id + " is not found", HttpStatusCode.valueOf(404)));
    }

    @Override
    public LandmarkEntity create(LandmarkEntity newLandmark) {
        if (newLandmark.getPrice() == null || newLandmark.getPrice() <= 0) {
            throw new ApiException("landmark price should be greater than zero", HttpStatusCode.valueOf(400));
        }
        Optional<LandmarkEntity> existingLandmark = landmarkRepository.findByTitle(newLandmark.getTitle());
        if (existingLandmark.isPresent()) {
            throw new ApiException("Landmark with title '" + newLandmark.getTitle() + "' already exists", HttpStatusCode.valueOf(400));
        }
        return landmarkRepository.save(newLandmark);
    }

    @Override
    public LandmarkEntity update(LandmarkDto landmark, Long id) {
        LandmarkEntity toUpdate = landmarkRepository.findById(id).orElseThrow(() -> new ApiException("Landmark with id " + id + " is not found", HttpStatusCode.valueOf(404)));
        if (landmark.getTitle() != null) {
            toUpdate.setTitle(landmark.getTitle());
        }
        if (landmark.getDescription() != null) {
            toUpdate.setDescription(landmark.getDescription());
        }
        if (landmark.getLocation() != null) {
            toUpdate.setLocation(landmark.getLocation());
        }
        if (landmark.getPrice() != null) {
            if (landmark.getPrice() <= 0) {
                throw new ApiException("landmark price must be greater than zero", HttpStatusCode.valueOf(400));
            }
            toUpdate.setPrice(landmark.getPrice());
        }
        if (landmark.getImageUrl() != null){
            toUpdate.setImageUrl(landmark.getImageUrl());
        }
        return landmarkRepository.save(toUpdate);
    }

    @Override
    public ResponseEntity<Map<String, Object>> delete(Long id) {
        Optional<LandmarkEntity> landmark = landmarkRepository.findById(id);
        if (landmark.isPresent()) {
            LandmarkEntity landmarkEntity = landmark.get();
            List<TravelPlanEntity> travelPlans = travelPlanRepository.findAll();
            travelPlans.forEach(plan -> plan.getLandmarks().remove(landmarkEntity));
            travelPlanRepository.saveAll(travelPlans);
            List<TourPlanEntity> tourPlans = tourPlanRepository.findAll();
            tourPlans.forEach(plan -> plan.getLandmarks().remove(landmarkEntity));
            tourPlanRepository.saveAll(tourPlans);
            landmarkRepository.deleteById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Landmark with id " + id + " is deleted");
            response.put("deleted landmark", landmark.get());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else throw new ApiException("Landmark with id " + id + " is not found", HttpStatusCode.valueOf(404));
    }
}