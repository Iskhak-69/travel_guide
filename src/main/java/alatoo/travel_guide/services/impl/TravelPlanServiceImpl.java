package alatoo.travel_guide.services.impl;

import alatoo.travel_guide.services.TravelPlanService;
import alatoo.travel_guide.dto.TravelPlanDto;
import alatoo.travel_guide.entities.LandmarkEntity;
import alatoo.travel_guide.entities.TravelPlanEntity;
import alatoo.travel_guide.entities.UserEntity;
import alatoo.travel_guide.exceptions.ApiException;
import alatoo.travel_guide.mapper.TravelPlanMapper;
import alatoo.travel_guide.repositories.LandmarkRepository;
import alatoo.travel_guide.repositories.TravelPlanRepository;
import alatoo.travel_guide.repositories.UserRepository;
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
public class TravelPlanServiceImpl implements TravelPlanService {
    @Autowired
    private TravelPlanRepository travelPlanRepository;

    @Autowired
    private LandmarkRepository landmarkRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TravelPlanMapper travelPlanMapper;

    @Override
    public List<TravelPlanEntity> getAll() {
        return travelPlanRepository.findAll();
    }

    @Override
    public TravelPlanEntity getById(Long id) {
        return travelPlanRepository.findById(id).orElseThrow(() -> new ApiException("Travel plan with id " + id + " is not found", HttpStatusCode.valueOf(404)));
    }

    @Override
    public TravelPlanEntity create(TravelPlanEntity newTravelPlan) {
        return travelPlanRepository.save(newTravelPlan);
    }

    @Override
    public TravelPlanEntity create(TravelPlanDto newTravelPlanDto) {
        TravelPlanEntity travelPlanEntity = travelPlanMapper.travelPlanDtoToTravelPlan(newTravelPlanDto);
        List<Long> requestedLandmarkIds = newTravelPlanDto.getLandmarkIds();
        List<LandmarkEntity> landmarks = landmarkRepository.findAllById(requestedLandmarkIds);
        List<Long> foundLandmarkIds = landmarks.stream().map(LandmarkEntity::getId).toList();
        List<Long> missingIds = requestedLandmarkIds.stream().filter(id -> !foundLandmarkIds.contains(id)).toList();

        if (!missingIds.isEmpty()) {
            throw new ApiException("Landmarks with id " + missingIds + " is not found", HttpStatusCode.valueOf(404));
        }
        travelPlanEntity.setLandmarks(landmarks);
        travelPlanEntity.updatePrice();
        return travelPlanRepository.save(travelPlanEntity);
    }

    @Override
    public TravelPlanEntity update(TravelPlanDto travelPlan, Long id) {
        TravelPlanEntity toUpdate = travelPlanRepository.findById(id).orElseThrow(() -> new ApiException("Travel plan with id " + id + " is not found", HttpStatusCode.valueOf(404)));
        if (travelPlan.getPlanName() != null) {
            toUpdate.setPlanName(travelPlan.getPlanName());
        }
        if (travelPlan.getStartDate() != null) {
            toUpdate.setStartDate(travelPlan.getStartDate());
        }
        if (travelPlan.getEndDate() != null) {
            toUpdate.setEndDate(travelPlan.getEndDate());
        }
        if (travelPlan.getLandmarkIds() != null && !travelPlan.getLandmarkIds().isEmpty()) {
            List<LandmarkEntity> currentLandmarks = toUpdate.getLandmarks();
            List<LandmarkEntity> newLandmarks = landmarkRepository.findAllById(travelPlan.getLandmarkIds());
            for (LandmarkEntity newLandmark : newLandmarks) {
                if (!currentLandmarks.contains(newLandmark)) {
                    currentLandmarks.add(newLandmark);
                }
            }
            toUpdate.setLandmarks(currentLandmarks);
            toUpdate.updatePrice();
        }
        return travelPlanRepository.save(toUpdate);
    }

    @Override
    public ResponseEntity<Map<String, Object>> delete(Long id) {
        Optional<TravelPlanEntity> travelPlan = travelPlanRepository.findById(id);
        if (travelPlan.isPresent()) {
            TravelPlanEntity travelPlanEntity = travelPlan.get();
            List<UserEntity> users = userRepository.findAll();
            users.forEach(user -> user.getTravelPlan().remove(travelPlanEntity));
            userRepository.saveAll(users);
            travelPlanRepository.deleteById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Travel plan with id " + id + " is deleted");
            response.put("deleted travel plan", travelPlan.get());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else throw new ApiException("Travel plan with id " + id + " is not found", HttpStatusCode.valueOf(404));
    }

    @Override
    public TravelPlanEntity deleteLandmark(List<Long> landmarkIds, Long travelPlanId) {
        TravelPlanEntity travelPlan = travelPlanRepository.findById(travelPlanId).orElseThrow(() -> new ApiException("Travel plan with id " + travelPlanId + " is not found", HttpStatusCode.valueOf(404)));
        List<LandmarkEntity> currentLandmarks = travelPlan.getLandmarks();
        for (Long landmarkId : landmarkIds) {
            boolean exists = currentLandmarks.stream().anyMatch(landmark -> landmark.getId().equals(landmarkId));
            if (!exists) {
                throw new ApiException("Landmark with id " + landmarkId + " is not found in this travel plan.", HttpStatusCode.valueOf(404));
            }
        }
        currentLandmarks.removeIf(landmark -> landmarkIds.contains(landmark.getId()));
        travelPlan.setLandmarks(currentLandmarks);
        return travelPlanRepository.save(travelPlan);
    }
}