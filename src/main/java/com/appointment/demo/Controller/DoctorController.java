package com.appointment.demo.Controller;
import com.appointment.demo.model.Doctor;
import com.appointment.demo.Service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DoctorController {
    private final DoctorService doctorService;
    @GetMapping
    public List<Doctor> getAll() {
        return doctorService.getAll();
    }
    @PatchMapping("/{id}/status")
    public Doctor updateStatus(@PathVariable Long id, @RequestParam boolean active) {
        return doctorService.updateStatus(id, active);
    }
    @PutMapping("/{id}")
    public Doctor updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        return doctorService.updateDoctor(id, doctor);
    }
}