
package com.appointment.demo.DTO;

import com.appointment.demo.model.Role;

public record RegisterRequest(
        String name,
        String email,
        String password,
        Role role,
        String phone,
        String specialty 
) {}