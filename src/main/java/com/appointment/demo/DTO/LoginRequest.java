package com.appointment.demo.DTO;
public record LoginRequest(
        String email,
        String password
) {}