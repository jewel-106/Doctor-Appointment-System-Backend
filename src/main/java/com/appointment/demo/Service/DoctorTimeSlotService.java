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
        List<DoctorTimeSlot> allSlots = timeSlotRepo.findByDoctorIdAndAvailableDate(doctorId, date);

        java.util.Map<java.time.LocalTime, List<DoctorTimeSlot>> slotsByTime = allSlots.stream()
                .collect(java.util.stream.Collectors.groupingBy(DoctorTimeSlot::getStartTime));

        List<DoctorTimeSlot> consolidatedSlots = new java.util.ArrayList<>();

        for (java.util.Map.Entry<java.time.LocalTime, List<DoctorTimeSlot>> entry : slotsByTime.entrySet()) {
            List<DoctorTimeSlot> timeSlots = entry.getValue();

            boolean isAnyBooked = timeSlots.stream().anyMatch(DoctorTimeSlot::getIsBooked);

            DoctorTimeSlot baseSlot = timeSlots.get(0);

            if (isAnyBooked) {
                baseSlot.setIsBooked(true);
            }

            consolidatedSlots.add(baseSlot);
        }

        consolidatedSlots.sort(java.util.Comparator.comparing(DoctorTimeSlot::getStartTime));

        return consolidatedSlots;
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