
package com.appointment.demo.Service;

import com.appointment.demo.Repository.DoctorRepository;
import com.appointment.demo.DTO.AuthResponse;
import com.appointment.demo.DTO.LoginRequest;
import com.appointment.demo.DTO.RegisterRequest;
import com.appointment.demo.Repository.OtpRepository;
import com.appointment.demo.Repository.UserRepository;
import com.appointment.demo.config.JwtService;
import com.appointment.demo.model.OtpStore;
import com.appointment.demo.model.Role;
import com.appointment.demo.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final OtpRepository otpRepo;
    private final DoctorRepository doctorRepo;

    public AuthResponse register(RegisterRequest request) {
        if (userRepo.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        var user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .phone(request.phone())
                .specialty(request.role() == Role.DOCTOR ? request.specialty() : null)
                .build();
        userRepo.save(user);
        var jwt = jwtService.generateToken(user);
        
        Long profileId = null;
        if (user.getRole() == Role.DOCTOR) {
             // Try to find if a doctor record exists (unlikely on fresh register but consistent)
             var doctor = doctorRepo.findByUserId(user.getId()).orElse(null);
             if (doctor != null) profileId = doctor.getId();
        }

        return new AuthResponse(jwt, user.getName(), user.getEmail(), user.getRole(), user.getPhone(), 
                user.getAvatar(), user.getId(), profileId, user.getAddress(), user.getBio(), 
                user.getGender(), user.getDateOfBirth(), user.getHospital() != null ? user.getHospital().getId() : null, user.getSpecialty());
    }

    public AuthResponse login(LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        var user = userRepo.findByEmail(request.email()).orElseThrow();
        var jwt = jwtService.generateToken(user);
        
        Long profileId = null;
        if (user.getRole() == Role.DOCTOR) {
             var doctor = doctorRepo.findByUserId(user.getId()).orElse(null);
             if (doctor != null) profileId = doctor.getId();
        }

        return new AuthResponse(jwt, user.getName(), user.getEmail(), user.getRole(), user.getPhone(), 
                user.getAvatar(), user.getId(), profileId, user.getAddress(), user.getBio(), 
                user.getGender(), user.getDateOfBirth(), user.getHospital() != null ? user.getHospital().getId() : null, user.getSpecialty());
    }

    private String generateOtp() {
        return String.format("%06d", ThreadLocalRandom.current().nextInt(100000, 999999));
    }

    @Transactional
    public void sendForgotPasswordOtp(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = generateOtp();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(500);

        otpRepo.deleteByEmail(email);
        otpRepo.flush();
        OtpStore otpEntity = OtpStore.builder()
                .email(email)
                .phone(user.getPhone())
                .otp(otp)
                .expiresAt(expiresAt)
                .build();
        otpRepo.saveAndFlush(otpEntity);

        System.out.println("EMAIL OTP for " + email + " → " + otp);
        System.out.println("PHONE OTP for " + user.getPhone() + " → " + otp);
    }

    @Transactional
    public void verifyResetOtp(String email, String otpInput) {
        otpInput = otpInput.trim();

        OtpStore otpEntity = otpRepo.findByEmailAndUsedAtIsNull(email)
                .orElseThrow(() -> new RuntimeException("No active OTP found or already used"));

        if (!otpEntity.getOtp().equals(otpInput)) {
            throw new RuntimeException("Wrong OTP entered");
        }

        if (LocalDateTime.now().isAfter(otpEntity.getExpiresAt())) {
            throw new RuntimeException("OTP has expired");
        }

        otpEntity.setUsedAt(LocalDateTime.now());
        otpRepo.saveAndFlush(otpEntity);

        System.out.println("OTP verified successfully for: " + email);
    }

    @Transactional
    public void resetPassword(String email, String newPassword) {
        OtpStore otpEntity = otpRepo.findTopByEmailOrderByExpiresAtDesc(email)
                .orElseThrow(() -> new RuntimeException("No OTP found for this email"));

        if (otpEntity.getUsedAt() == null) {
            throw new RuntimeException("Please verify OTP first");
        }

        if (LocalDateTime.now().isAfter(otpEntity.getExpiresAt())) {
            throw new RuntimeException("OTP has expired");
        }

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.saveAndFlush(user);

        otpRepo.delete(otpEntity);
        otpRepo.flush();

        System.out.println("Password reset successful for: " + email);
    }

    public String getEmailFromToken(String token) {
        return jwtService.extractUsername(token);
    }

    @Transactional
    public AuthResponse updateProfile(String currentEmail, String newName, String newEmail, String newPhone, 
                                     String address, String bio, String gender, String dateOfBirth, String specialty) {
        User user = userRepo.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!currentEmail.equals(newEmail) && userRepo.findByEmail(newEmail).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        
        user.setName(newName);
        user.setEmail(newEmail);
        user.setPhone(newPhone);
        user.setAddress(address);
        user.setBio(bio);
        user.setGender(gender);
        user.setDateOfBirth(dateOfBirth);
        if (user.getRole() == Role.DOCTOR) {
            user.setSpecialty(specialty);
        }
        userRepo.saveAndFlush(user);
        
        var jwt = jwtService.generateToken(user);
        System.out.println("Profile updated for: " + newEmail);
        
        Long profileId = null;
        if (user.getRole() == Role.DOCTOR) {
             var doctor = doctorRepo.findByUserId(user.getId()).orElse(null);
             if (doctor != null) profileId = doctor.getId();
        }

        return new AuthResponse(jwt, user.getName(), user.getEmail(), user.getRole(), user.getPhone(), 
                user.getAvatar(), user.getId(), profileId, user.getAddress(), user.getBio(), 
                user.getGender(), user.getDateOfBirth(), user.getHospital() != null ? user.getHospital().getId() : null, user.getSpecialty());
    }

    @Transactional
    public void updateAvatar(String email, String avatar) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setAvatar(avatar);
        userRepo.saveAndFlush(user);
        
        System.out.println("Avatar updated for: " + email);
    }

    @Transactional
    public void changePassword(String email, String currentPassword, String newPassword) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            System.out.println("Password mismatch for user: " + email);
            throw new RuntimeException("Current password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.saveAndFlush(user);
        
        System.out.println("Password changed for: " + email);
    }
}