package alatoo.travel_guide.services.impl;

import alatoo.travel_guide.dto.TravelPlanDto;
import alatoo.travel_guide.entities.LandmarkEntity;
import alatoo.travel_guide.entities.TourPlanEntity;
import alatoo.travel_guide.entities.TravelPlanEntity;
import alatoo.travel_guide.entities.UserEntity;
import alatoo.travel_guide.exceptions.ApiException;
import alatoo.travel_guide.repositories.LandmarkRepository;
import alatoo.travel_guide.repositories.TourPlanRepository;
import alatoo.travel_guide.repositories.TravelPlanRepository;
import alatoo.travel_guide.repositories.UserRepository;
import alatoo.travel_guide.services.BookingService;
import alatoo.travel_guide.services.TravelPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TourPlanRepository tourPlanRepository;

    @Autowired
    private TravelPlanRepository travelPlanRepository;

    @Autowired
    private TravelPlanService travelPlanService;

    @Autowired
    private LandmarkRepository landmarkRepository;

    @Override
    public void addTravelPlan(Long userId, TravelPlanDto travelPlanDto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User with id " + userId + " is not found", HttpStatusCode.valueOf(404)));
        TravelPlanEntity travelPlan = new TravelPlanEntity();
        travelPlan.setPlanName(travelPlanDto.getPlanName());
        travelPlan.setStartDate(travelPlanDto.getStartDate());
        travelPlan.setEndDate(travelPlanDto.getEndDate());
        List<LandmarkEntity> landmarks = landmarkRepository.findAllById(travelPlanDto.getLandmarkIds());
        travelPlan.setLandmarks(landmarks);
        travelPlan.updatePrice();
        travelPlanRepository.save(travelPlan);
        user.getTravelPlan().add(travelPlan);
        userRepository.save(user);
    }


    @Override
    public void addTourPlan(Long userId, Long tourPlanId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ApiException("User with id " + userId + " is not found", HttpStatusCode.valueOf(404)));
        TourPlanEntity tourPlan = tourPlanRepository.findById(tourPlanId).orElseThrow(() -> new ApiException("Tour plan with id " + tourPlanId + " is not found", HttpStatusCode.valueOf(404)));
        user.getTourPlan().add(tourPlan);
        userRepository.save(user);
    }

    @Override
    public List<Map<String, Object>> getAllTourPlans() {
        List<UserEntity> users = userRepository.findAll();

        return users.stream()
                .flatMap(user -> user.getTourPlan().stream()
                        .map(plan -> {
                            Map<String, Object> result = new HashMap<>();
                            Map<String, Object> tourPlan = new HashMap<>();
                            tourPlan.put("id", plan.getId());
                            tourPlan.put("planName", plan.getPlanName());
                            tourPlan.put("startDate", plan.getStartDate());
                            tourPlan.put("endDate", plan.getEndDate());
                            tourPlan.put("price", plan.getPrice());
                            tourPlan.put("landmarks", plan.getLandmarks().stream().map(landmark -> {
                                Map<String, Object> landmarkDto = new HashMap<>();
                                landmarkDto.put("id", landmark.getId());
                                landmarkDto.put("title", landmark.getTitle());
                                landmarkDto.put("description", landmark.getDescription());
                                landmarkDto.put("location", landmark.getLocation());
                                landmarkDto.put("price", landmark.getPrice());
                                landmarkDto.put("imageUrl", landmark.getImageUrl());
                                return landmarkDto;
                            }).collect(Collectors.toList()));
                            result.put("tourPlan", tourPlan);
                            result.put("user", user.getId());
                            return result;
                        })
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getAllTravelPlans() {
        List<UserEntity> users = userRepository.findAll();

        return users.stream()
                .flatMap(user -> user.getTravelPlan().stream()
                        .map(plan -> {
                            Map<String, Object> result = new HashMap<>();
                            Map<String, Object> travelPlan = new HashMap<>();
                            travelPlan.put("id", plan.getId());
                            travelPlan.put("planName", plan.getPlanName());
                            travelPlan.put("startDate", plan.getStartDate());
                            travelPlan.put("endDate", plan.getEndDate());
                            travelPlan.put("price", plan.getPrice());
                            travelPlan.put("landmarks", plan.getLandmarks().stream().map(landmark -> {
                                Map<String, Object> landmarkDto = new HashMap<>();
                                landmarkDto.put("id", landmark.getId());
                                landmarkDto.put("title", landmark.getTitle());
                                landmarkDto.put("description", landmark.getDescription());
                                landmarkDto.put("location", landmark.getLocation());
                                landmarkDto.put("price", landmark.getPrice());
                                landmarkDto.put("imageUrl", landmark.getImageUrl());
                                return landmarkDto;
                            }).collect(Collectors.toList()));
                            result.put("travelPlan", travelPlan);
                            result.put("user", user.getId());
                            return result;
                        })
                )
                .collect(Collectors.toList());
    }


    @Override
    public List<TravelPlanEntity> getUserTravelPlans(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ApiException("User with id " + userId + " is not found", HttpStatusCode.valueOf(404)));
        return user.getTravelPlan();
    }

    @Override
    public List<TourPlanEntity> getUserTourPlans(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ApiException("User with id " + userId + " is not found", HttpStatusCode.valueOf(404)));
        return user.getTourPlan();
    }

    @Override
    public TravelPlanEntity updateTravelPlan(Long userId, Long travelPlanId, TravelPlanDto travelPlanDto) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ApiException("User with id " + userId + " is not found", HttpStatusCode.valueOf(404)));
        boolean ownsTravelPlan = user.getTravelPlan().stream().anyMatch(travelPlan -> travelPlan.getId().equals(travelPlanId));
        if (!ownsTravelPlan) throw new ApiException("Travel plan with id " + travelPlanId + " is not owned with user id " + userId, HttpStatusCode.valueOf(403));
        return travelPlanService.update(travelPlanDto, travelPlanId);
    }

    @Override
    public UserEntity updateTourPlan(Long userId, List<Long> tourPlanIds) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ApiException("User with id " + userId + " is not found", HttpStatusCode.valueOf(404)));
        List<TourPlanEntity> newTourPlans = tourPlanRepository.findAllById(tourPlanIds);
        List<Long> foundIds = newTourPlans.stream().map(TourPlanEntity::getId).toList();
        List<Long> missingIds = tourPlanIds.stream().filter(id -> !foundIds.contains(id)).toList();
        if (!missingIds.isEmpty()) {
            throw new ApiException("Tour plans with id " + missingIds + " are not found", HttpStatusCode.valueOf(404));
        }
        List<TourPlanEntity> existingTourPlans = user.getTourPlan();
        newTourPlans.forEach(newPlan -> {
            if (!existingTourPlans.contains(newPlan)) {
                existingTourPlans.add(newPlan);
            }
        });
        user.setTourPlan(existingTourPlans);
        return userRepository.save(user);
    }


    @Override
    public void deleteTravelPlan(Long userId, Long travelPlanId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ApiException("User with id " + userId + " is not found", HttpStatusCode.valueOf(404)));
        user.getTravelPlan().removeIf(plan -> plan.getId().equals(travelPlanId));
        userRepository.save(user);
    }

    @Override
    public void deleteTourPlan(Long userId, Long tourPlanId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ApiException("User with id " + userId + " is not found", HttpStatusCode.valueOf(404)));
        user.getTourPlan().removeIf(plan -> plan.getId().equals(tourPlanId));
        userRepository.save(user);
    }
}