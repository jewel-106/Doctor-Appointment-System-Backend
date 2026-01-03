package com.appointment.demo.Controller;
import com.appointment.demo.model.Division;
import com.appointment.demo.model.District;
import com.appointment.demo.model.Upazila;
import com.appointment.demo.Repository.DivisionRepository;
import com.appointment.demo.Repository.DistrictRepository;
import com.appointment.demo.Repository.UpazilaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LocationController {
    private final DivisionRepository divisionRepository;
    private final DistrictRepository districtRepository;
    private final UpazilaRepository upazilaRepository;
    @GetMapping("/divisions")
    public ResponseEntity<List<Division>> getAllDivisions() {
        return ResponseEntity.ok(divisionRepository.findAll());
    }
    @GetMapping("/divisions/{divisionId}/districts")
    public ResponseEntity<List<District>> getDistrictsByDivision(@PathVariable Long divisionId) {
        return ResponseEntity.ok(districtRepository.findByDivisionId(divisionId));
    }
    @GetMapping("/districts/{districtId}/upazilas")
    public ResponseEntity<List<Upazila>> getUpazilasByDistrict(@PathVariable Long districtId) {
        return ResponseEntity.ok(upazilaRepository.findByDistrictId(districtId));
    }
}