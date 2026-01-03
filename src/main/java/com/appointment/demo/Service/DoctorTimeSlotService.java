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
        // Remove duplicates based on start time
        return slots.stream()
                .filter(com.appointment.demo.utils.DistinctByKey.distinctByKey(DoctorTimeSlot::getStartTime))
                .collect(java.util.stream.Collectors.toList());
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