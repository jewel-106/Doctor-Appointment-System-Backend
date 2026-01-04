package com.appointment.demo.Service;

import com.appointment.demo.DTO.RegisterRequest;
import com.appointment.demo.DTO.SuperAdminAnalytics;
import com.appointment.demo.DTO.SystemStats;
import com.appointment.demo.Repository.AppointmentRepository;
import com.appointment.demo.Repository.DoctorRepository;
import com.appointment.demo.Repository.UserRepository;
import com.appointment.demo.model.Hospital;
import com.appointment.demo.Repository.HospitalRepository;
import com.appointment.demo.model.Doctor;
import com.appointment.demo.model.Role;
import com.appointment.demo.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepo;
    private final DoctorRepository doctorRepo;
    private final AppointmentRepository appointmentRepo;
    private final HospitalRepository hospitalRepo;
    private final PasswordEncoder passwordEncoder;
    private final com.appointment.demo.Repository.AdminRepository adminRepository;

    public SystemStats getSystemStats() {
        return new SystemStats(
                hospitalRepo.count(),
                userRepo.countByRole(Role.ADMIN),
                userRepo.countByRole(Role.DOCTOR),
                userRepo.countByRole(Role.PATIENT),
                appointmentRepo.count());
    }

    @Transactional
    public void addDoctor(RegisterRequest request) {
        if (userRepo.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }
        String currentUserEmail = authentication.getName();
        User currentUser = userRepo.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
        if (currentUser.getRole() != Role.ADMIN && currentUser.getRole() != Role.SUPER_ADMIN) {
            throw new RuntimeException("Access denied: Only admins can add doctors");
        }
        User newDoctorUser = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.DOCTOR)
                .phone(request.phone())
                .specialty(request.specialty())
                .hospital(currentUser.getHospital())
                .build();
        userRepo.save(newDoctorUser);
        Doctor doctor = Doctor.builder()
                .userId(newDoctorUser.getId())
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .specialty(request.specialty())
                .build();
        doctorRepo.save(doctor);
    }

    public List<User> getAllAdmins() {
        return userRepo.findByRoleIn(List.of(Role.ADMIN, Role.SUPER_ADMIN));
    }

    @Transactional
    public void createAdmin(RegisterRequest request, Long hospitalId) {
        if (userRepo.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        Hospital hospital = null;
        if (hospitalId != null) {
            hospital = hospitalRepo.findById(hospitalId)
                    .orElseThrow(() -> new RuntimeException("Hospital not found"));
        }
        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ADMIN)
                .phone(request.phone())
                .hospital(hospital)
                .build();
        user = userRepo.save(user); // Capture saved user to get ID

        // Create Admin Entry in n_admins
        Admin admin = Admin.builder()
                .userId(user.getId())
                .name(user.getName())
                .phone(user.getPhone())
                .user(user)
                .build();
        adminRepository.save(admin);
    }

    public SuperAdminAnalytics getSuperAdminAnalytics() {
        Map<String, Long> hospitalsByDivision = hospitalRepo.findAll().stream()
                .collect(groupingBy(
                        h -> h.getDivision() != null ? h.getDivision().getNameEn() : "Unknown",
                        counting()));
        Map<String, Long> doctorsBySpecialty = doctorRepo.findAll().stream()
                .collect(groupingBy(
                        d -> d.getSpecialty() != null ? d.getSpecialty() : "General",
                        counting()));
        Map<String, Long> usersByRole = userRepo.findAll().stream()
                .collect(groupingBy(
                        u -> u.getRole().name(),
                        counting()));
        Map<String, Long> appointmentsByStatus = appointmentRepo.findAll().stream()
                .collect(groupingBy(
                        a -> a.getStatus().name(),
                        counting()));
        return new SuperAdminAnalytics(
                hospitalsByDivision,
                doctorsBySpecialty,
                usersByRole,
                appointmentsByStatus);
    }
}