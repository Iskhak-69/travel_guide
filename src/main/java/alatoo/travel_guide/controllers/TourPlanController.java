package alatoo.travel_guide.controllers;

import alatoo.travel_guide.dto.TourPlanDto;
import alatoo.travel_guide.entities.TourPlanEntity;
import alatoo.travel_guide.services.TourPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tour-plans")
public class TourPlanController {

    @Autowired
    private TourPlanService tourPlanService;

    @GetMapping("/get-all")
    public List<TourPlanEntity> getAll() {
        return tourPlanService.getAll();
    }

    @GetMapping("/get/{id}")
    public TourPlanEntity getById(@PathVariable("id") Long id) {
        return tourPlanService.getById(id);
    }

    @PostMapping("/create")
    public TourPlanEntity create(@RequestBody TourPlanDto newTourPlan) {
        return tourPlanService.create(newTourPlan);
    }

    @PutMapping("update/{id}")
    public TourPlanEntity update(@RequestBody TourPlanDto tourPlan, @PathVariable("id") Long id) {
        return tourPlanService.update(tourPlan, id);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        return tourPlanService.delete(id);
    }

    @DeleteMapping("delete-landmark/{id}")
    public TourPlanEntity deleteLandmark(@RequestBody List<Long> landmarkIds, @PathVariable("id") Long tourPlanId) {
        return tourPlanService.deleteLandmark(landmarkIds, tourPlanId);
    }
}