package com.appointment.demo.Controller;
import com.appointment.demo.Service.DoctorMigrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class MigrationController {
    private final DoctorMigrationService migrationService;
    @PostMapping("/sync-doctor-emails")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> syncDoctorEmails() {
        String result = migrationService.syncDoctorEmails();
        return ResponseEntity.ok(result);
    }
}