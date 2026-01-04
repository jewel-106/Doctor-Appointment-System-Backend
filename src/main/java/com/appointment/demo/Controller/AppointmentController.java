package com.appointment.demo.Controller;

import com.appointment.demo.DTO.AppointmentResponse;
import com.appointment.demo.model.Appointment;
import com.appointment.demo.Service.AppointmentService;
import com.appointment.demo.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final AuthenticationService authService;

    @GetMapping
    public List<AppointmentResponse> getAll(@RequestHeader("Authorization") String token,
            @RequestParam(required = false) Long hospitalId) {
        String email = authService.getEmailFromToken(token.replace("Bearer ", ""));
        return appointmentService.getAppointmentsForUser(email, hospitalId);
    }

    @GetMapping("/{id}")
    public AppointmentResponse getById(@PathVariable Long id) {
        return appointmentService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse create(@RequestBody Appointment request) {
        return appointmentService.create(request);
    }

    @PutMapping("/{id}")
    public AppointmentResponse update(@PathVariable Long id, @RequestBody Appointment request,
            @RequestHeader("Authorization") String token) {
        String email = authService.getEmailFromToken(token.replace("Bearer ", ""));
        return appointmentService.update(id, request, email);
    }

    @PatchMapping("/{id}/status")
    public AppointmentResponse changeStatus(@PathVariable Long id,
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String token) {
        String email = authService.getEmailFromToken(token.replace("Bearer ", ""));
        Appointment.Status status = Appointment.Status.valueOf(body.get("status").toLowerCase());
        return appointmentService.changeStatus(id, status, email);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        String email = authService.getEmailFromToken(token.replace("Bearer ", ""));
        appointmentService.delete(id, email);
    }
}