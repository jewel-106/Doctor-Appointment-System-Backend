
package com.appointment.demo.Controller;

import com.appointment.demo.DTO.AuthResponse;
import com.appointment.demo.DTO.LoginRequest;
import com.appointment.demo.DTO.RegisterRequest;
import com.appointment.demo.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        authService.sendForgotPasswordOtp(email);
        return ResponseEntity.ok("OTP sent to your email");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> body) {
        authService.verifyResetOtp(body.get("email"), body.get("otp"));
        return ResponseEntity.ok("OTP verified");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> body) {
        authService.resetPassword(body.get("email"), body.get("newPassword"));
        return ResponseEntity.ok("Password reset successful");
    }

    // Profile Management Endpoints
    @PutMapping("/profile")
    public ResponseEntity<AuthResponse> updateProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> body) {
        String email = authService.getEmailFromToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok(authService.updateProfile(
            email, 
            body.get("name"), 
            body.get("email"), 
            body.get("phone"),
            body.get("address"),
            body.get("bio"),
            body.get("gender"),
            body.get("dateOfBirth"),
            body.get("specialty")
        ));
    }

    @PutMapping("/avatar")
    public ResponseEntity<String> updateAvatar(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> body) {
        String email = authService.getEmailFromToken(token.replace("Bearer ", ""));
        authService.updateAvatar(email, body.get("avatar"));
        return ResponseEntity.ok("Avatar updated successfully");
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> body) {
        String email = authService.getEmailFromToken(token.replace("Bearer ", ""));
        authService.changePassword(email, body.get("currentPassword"), body.get("newPassword"));
        return ResponseEntity.ok("Password changed successfully");
    }
}