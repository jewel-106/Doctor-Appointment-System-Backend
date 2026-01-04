package com.appointment.demo.Repository;

import com.appointment.demo.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByPatientEmail(String patientEmail);

    List<Appointment> findByDoctor_User_Hospital_Id(Long hospitalId);

    boolean existsByDoctorIdAndAppointmentDateAndAppointmentTime(Long doctorId, java.time.LocalDate date,
            java.time.LocalTime time);
}