package com.appointment.demo.DTO;

import com.appointment.demo.model.Appointment.Status;
import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentDTO {

    public record Request(
            String patientName,
            String patientEmail,
            String patientPhone,
            Long doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            Status status,
            String notes
    ) {}

    public record Response(
            Long id,
            String patientName,
            String patientEmail,
            String patientPhone,
            Long doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            Status status,
            String notes,
            DoctorInfo doctor
    ) {}
}