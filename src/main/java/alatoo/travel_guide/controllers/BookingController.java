package alatoo.travel_guide.controllers;

import alatoo.travel_guide.dto.TravelPlanDto;
import alatoo.travel_guide.entities.TourPlanEntity;
import alatoo.travel_guide.entities.TravelPlanEntity;
import alatoo.travel_guide.entities.UserEntity;
import alatoo.travel_guide.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Get all tour plans
    @GetMapping("/all-tour-plans")
    public ResponseEntity<List<Map<String, Object>>> getAllTourPlans() {
        List<Map<String, Object>> response = bookingService.getAllTourPlans();
        return ResponseEntity.ok(response);
    }

    // Get tour plans for a specific user
    @GetMapping("/tour/{userId}")
    public ResponseEntity<List<TourPlanEntity>> getUserTourPlans(@PathVariable Long userId) {
        List<TourPlanEntity> tourPlans = bookingService.getUserTourPlans(userId);
        return ResponseEntity.ok(tourPlans);
    }

    // Add a tour plan to a user
    @PostMapping("/tour/{userId}/{tourPlanId}")
    public ResponseEntity<String> addTourPlan(@PathVariable Long userId, @PathVariable Long tourPlanId) {
        bookingService.addTourPlan(userId, tourPlanId);
        return ResponseEntity.ok("Tour plan added");
    }

    // Update a user's tour plans
    @PutMapping("/tour/{userId}")
    public ResponseEntity<UserEntity> updateTourPlan(@PathVariable Long userId, @RequestBody List<Long> tourPlanIds) {
        UserEntity updatedUser = bookingService.updateTourPlan(userId, tourPlanIds);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete a tour plan from a user
    @DeleteMapping("/tour/{userId}/{tourPlanId}")
    public ResponseEntity<String> deleteTourPlan(@PathVariable Long userId, @PathVariable Long tourPlanId) {
        bookingService.deleteTourPlan(userId, tourPlanId);
        return ResponseEntity.ok("Tour plan deleted");
    }

    // Get all travel plans
    @GetMapping("/all-travel-plans")
    public ResponseEntity<List<Map<String, Object>>> getTravelPlansWithUsers() {
        List<Map<String, Object>> response = bookingService.getAllTravelPlans();
        return ResponseEntity.ok(response);
    }

    // Get travel plans for a specific user
    @GetMapping("/travel/{userId}")
    public ResponseEntity<List<TravelPlanEntity>> getUserTravelPlans(@PathVariable Long userId) {
        List<TravelPlanEntity> travelPlans = bookingService.getUserTravelPlans(userId);
        return ResponseEntity.ok(travelPlans);
    }

    // Add a travel plan to a user
    @PostMapping("/travel/{userId}")
    public ResponseEntity<String> addTravelPlan(@PathVariable Long userId, @RequestBody TravelPlanDto travelPlan) {
        bookingService.addTravelPlan(userId, travelPlan);
        return ResponseEntity.ok("Travel plan added");
    }

    // Update a travel plan for a user
    @PutMapping("/travel/{userId}/{travelPlanId}")
    public ResponseEntity<TravelPlanEntity> updateTravelPlan(@PathVariable Long userId, @PathVariable Long travelPlanId, @RequestBody TravelPlanDto travelPlanDto) {
        TravelPlanEntity updatedTravelPlan = bookingService.updateTravelPlan(userId, travelPlanId, travelPlanDto);
        return ResponseEntity.ok(updatedTravelPlan);
    }

    // Delete a travel plan from a user
    @DeleteMapping("/travel/{userId}/{travelPlanId}")
    public ResponseEntity<String> deleteTravelPlan(@PathVariable Long userId, @PathVariable Long travelPlanId) {
        bookingService.deleteTravelPlan(userId, travelPlanId);
        return ResponseEntity.ok("Travel plan deleted");
    }
}