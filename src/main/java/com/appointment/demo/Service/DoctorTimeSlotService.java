package com.appointment.demo.Service;

import com.appointment.demo.model.DoctorTimeSlot;
import com.appointment.demo.Repository.DoctorTimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorTimeSlotService {
    private final DoctorTimeSlotRepository timeSlotRepo;

    public List<DoctorTimeSlot> getAvailableSlots(Long doctorId) {
        return timeSlotRepo.findByDoctorIdAndIsBookedFalse(doctorId);
    }

    public List<DoctorTimeSlot> getAvailableSlotsByDate(Long doctorId, LocalDate date) {
        List<DoctorTimeSlot> slots = timeSlotRepo.findByDoctorIdAndAvailableDateAndIsBookedFalse(doctorId, date);
        // Also fetch booked slots to ensure we have the complete picture
        // Note: The repository method name
        // 'findByDoctorIdAndAvailableDateAndIsBookedFalse' implies we only get free
        // slots.
        // We should actually fetch ALL slots for the date to correctly determine status
        // if duplicates exist.
        // But since we can't easily change the repo method call without checking if
        // 'findByDoctorIdAndAvailableDate' exists or creating it,
        // Let's first check if we have a method for that.
        // Looking at the repo file content previously read:
        // findByDoctorIdAndAvailableDateAndStartTime exists.
        // But we want ALL for a date.
        // Let's assume we need to fix the repository fetching first or work with what
        // we have.
        // Wait, if we only fetch "IsBookedFalse", we will NEVER see the booked ones to
        // merge them!
        // So the UI sees "Free" because we explicitly asked for "Free".
        // The duplicate issue is: We likely have multiple "Free" slots.
        // BUT if the error is "Already Taken", it means an Appointment exists.
        // If an Appointment exists, there SHOULD be a Booked slot.
        // If we only query "IsBookedFalse", we hide the Booked slot and show the Free
        // duplicate.
        // FIX: We must query ALL slots for the date (Booked AND Free).

        // Let's use a repository method that finds all by Doctor and Date.
        // Looking at repo: findByDoctorIdAndAvailableDateAndIsBookedFalse is there.
        // We need 'findByDoctorIdAndAvailableDate'.
        // I need to add this method to the Repository first.
        return slots; // Placeholder to stop this tool call and switch to adding repo method.
    }

    public List<DoctorTimeSlot> getAllSlots(Long doctorId) {
        return timeSlotRepo.findByDoctorId(doctorId);
    }

    public DoctorTimeSlot createSlot(DoctorTimeSlot slot) {
        return timeSlotRepo.save(slot);
    }

    public List<DoctorTimeSlot> createSlots(List<DoctorTimeSlot> slots) {
        return timeSlotRepo.saveAll(slots);
    }

    public void deleteSlot(Long id) {
        timeSlotRepo.deleteById(id);
    }
}