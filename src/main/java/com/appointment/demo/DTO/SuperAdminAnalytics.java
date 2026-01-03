package com.appointment.demo.DTO;
import java.util.Map;
public record SuperAdminAnalytics(
    Map<String, Long> hospitalsByDivision,
    Map<String, Long> doctorsBySpecialty,
    Map<String, Long> usersByRole,
    Map<String, Long> appointmentsByStatus
) {}