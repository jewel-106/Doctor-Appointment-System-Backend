package com.appointment.demo.DTO;

public record SystemStats(
    long totalHospitals,
    long totalAdmins,
    long totalDoctors,
    long totalPatients,
    long totalAppointments
) {}
