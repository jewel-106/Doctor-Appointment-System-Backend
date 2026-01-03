package com.appointment.demo.DTO;

import com.appointment.demo.model.Appointment;
import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentResponse(
        Long id,
        String patientName,
        String patientEmail,
        String patientPhone,
        Long doctorId,
        String doctorName,
        String doctorSpecialty,
        LocalDate appointmentDate,
        LocalTime appointmentTime,
        Appointment.Status status,
        String notes,
        String patientComment,
        String reason,
        String previousPrescription,
        String prescription,
        String diagnosis,
        String patientAge,
        String patientGender,
        String emergencyContact
) {}
