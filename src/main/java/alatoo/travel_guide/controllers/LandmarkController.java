package alatoo.travel_guide.controllers;

import alatoo.travel_guide.dto.LandmarkDto;
import alatoo.travel_guide.entities.LandmarkEntity;
import alatoo.travel_guide.services.LandmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/landmarks")
public class LandmarkController {

    @Autowired
    private LandmarkService landmarkService;

    @GetMapping("/get-all")
    public List<LandmarkEntity> getAll() {
        return landmarkService.getAll();
    }

    @GetMapping("/get/{id}")
    public LandmarkEntity getById(@PathVariable("id") Long id) {
        return landmarkService.getById(id);
    }

    @PostMapping("/create")
    public LandmarkEntity create(@RequestBody LandmarkEntity newLandmark) {
        return landmarkService.create(newLandmark);
    }

    @PutMapping("update/{id}")
    public LandmarkEntity update(@RequestBody LandmarkDto landmark, @PathVariable("id") Long id) {
        return landmarkService.update(landmark, id);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        return landmarkService.delete(id);
    }

}