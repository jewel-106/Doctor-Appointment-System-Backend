package com.appointment.demo.Controller;
import com.appointment.demo.model.DoctorTimeSlot;
import com.appointment.demo.Service.DoctorTimeSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/api/doctor-slots")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DoctorTimeSlotController {
    private final DoctorTimeSlotService timeSlotService;
    @GetMapping("/doctor/{doctorId}")
    public List<DoctorTimeSlot> getAvailableSlots(@PathVariable Long doctorId) {
        return timeSlotService.getAvailableSlots(doctorId);
    }
    @GetMapping("/doctor/{doctorId}/date/{date}")
    public List<DoctorTimeSlot> getAvailableSlotsByDate(
            @PathVariable Long doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return timeSlotService.getAvailableSlotsByDate(doctorId, date);
    }
    @GetMapping("/doctor/{doctorId}/all")
    public List<DoctorTimeSlot> getAllSlots(@PathVariable Long doctorId) {
        return timeSlotService.getAllSlots(doctorId);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorTimeSlot createSlot(@RequestBody DoctorTimeSlot slot) {
        return timeSlotService.createSlot(slot);
    }
    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    public List<DoctorTimeSlot> createSlots(@RequestBody List<DoctorTimeSlot> slots) {
        return timeSlotService.createSlots(slots);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSlot(@PathVariable Long id) {
        timeSlotService.deleteSlot(id);
    }
}