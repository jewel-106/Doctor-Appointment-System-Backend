package com.appointment.demo.Controller;
import com.appointment.demo.model.Hospital;
import com.appointment.demo.Repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HospitalController {
    private final HospitalRepository hospitalRepository;
    @GetMapping
    public ResponseEntity<List<Hospital>> getAllHospitals() {
        return ResponseEntity.ok(hospitalRepository.findByIsActiveTrue());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Hospital> getHospital(@PathVariable Long id) {
        return hospitalRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/default")
    public ResponseEntity<Hospital> getDefaultHospital() {
        return hospitalRepository.findFirstByIsActiveTrueOrderByIdAsc()
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/by-area")
    public ResponseEntity<List<Hospital>> getHospitalsByArea(
            @RequestParam Long divisionId,
            @RequestParam(required = false) Long districtId,
            @RequestParam(required = false) Long upazilaId) {
        List<Hospital> hospitals;
        if (upazilaId != null && districtId != null) {
            hospitals = hospitalRepository.findByDivisionIdAndDistrictIdAndUpazilaIdAndIsActiveTrue(
                divisionId, districtId, upazilaId);
        } else if (districtId != null) {
            hospitals = hospitalRepository.findByDivisionIdAndDistrictIdAndIsActiveTrue(
                divisionId, districtId);
        } else {
            hospitals = hospitalRepository.findByDivisionIdAndIsActiveTrue(divisionId);
        }
        return ResponseEntity.ok(hospitals);
    }
    @PostMapping
    public ResponseEntity<Hospital> createHospital(@RequestBody Hospital hospital) {
        hospital.setId(null);
        return ResponseEntity.ok(hospitalRepository.save(hospital));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Hospital> updateHospital(@PathVariable Long id, @RequestBody Hospital hospital) {
        return hospitalRepository.findById(id)
            .map(existing -> {
                hospital.setId(id);
                return ResponseEntity.ok(hospitalRepository.save(hospital));
            })
            .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHospital(@PathVariable Long id) {
        hospitalRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}