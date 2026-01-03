package com.appointment.demo.Repository;

import com.appointment.demo.model.DoctorTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DoctorTimeSlotRepository extends JpaRepository<DoctorTimeSlot, Long> {
    List<DoctorTimeSlot> findByDoctorIdAndAvailableDateAndIsBookedFalse(Long doctorId, LocalDate date);

    List<DoctorTimeSlot> findByDoctorIdAndAvailableDate(Long doctorId, LocalDate date);

    List<DoctorTimeSlot> findByDoctorIdAndIsBookedFalse(Long doctorId);

    List<DoctorTimeSlot> findByDoctorId(Long doctorId);

    List<DoctorTimeSlot> findByDoctorIdAndAvailableDateAndStartTime(Long doctorId, LocalDate date,
            java.time.LocalTime startTime);
}