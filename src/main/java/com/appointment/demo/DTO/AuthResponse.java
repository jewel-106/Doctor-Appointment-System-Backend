package com.appointment.demo.DTO;

import com.appointment.demo.model.Role;

public record AuthResponse(
        String token,
        String name,
        String email,
        Role role,
        String phone,
        String avatar,
        Long userId,
        Long profileId,  
        String address,
        String bio,
        String gender,
        String dateOfBirth,
        Long hospitalId,
        String specialty
) {}