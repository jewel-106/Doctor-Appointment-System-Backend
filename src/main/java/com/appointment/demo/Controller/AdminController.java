package com.appointment.demo.Controller;
import com.appointment.demo.DTO.RegisterRequest;
import com.appointment.demo.DTO.SuperAdminAnalytics;
import com.appointment.demo.Service.AdminService;
import com.appointment.demo.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.appointment.demo.DTO.SystemStats;
import java.util.List;
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {
    private final AdminService adminService;
    @PostMapping("/add-doctor")
    public ResponseEntity<String> addDoctor(@RequestBody RegisterRequest request) {
        adminService.addDoctor(request);
        return ResponseEntity.ok("Doctor added successfully");
    }
    @GetMapping("/stats")
    public ResponseEntity<SystemStats> getStats() {
        return ResponseEntity.ok(adminService.getSystemStats());
    }
    @GetMapping("/users/admins")
    public ResponseEntity<List<User>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }
    @PostMapping("/create-admin")
    public ResponseEntity<String> createAdmin(@RequestBody RegisterRequest request, @RequestParam(required = false) Long hospitalId) {
        adminService.createAdmin(request, hospitalId);
        return ResponseEntity.ok("Admin created successfully");
    }
    @GetMapping("/analytics")
    public ResponseEntity<SuperAdminAnalytics> getAnalytics() {
        return ResponseEntity.ok(adminService.getSuperAdminAnalytics());
    }
}