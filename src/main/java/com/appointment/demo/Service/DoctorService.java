package com.appointment.demo.Service;

import com.appointment.demo.model.Doctor;
import com.appointment.demo.model.Role;
import com.appointment.demo.model.User;
import com.appointment.demo.Repository.DoctorRepository;
import com.appointment.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepo;
    
    @Cacheable(value = "doctors", key = "'allDoctors'")
    public List<Doctor> getAll() {
        System.out.println("Fetching all doctors from database...");
    private final UserRepository userRepo;

    public List<Doctor> getAll(Long hospitalId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepo.findByEmail(email).orElse(null);

        if (user != null) {
            if (user.getRole() == Role.ADMIN) {
                // Admin: Strictly only their own hospital
                if (user.getHospital() != null) {
                    Long adminHospitalId = user.getHospital().getId();
                    return doctorRepo.findAll().stream()
                            .filter(d -> d.getUser() != null && d.getUser().getHospital() != null
                                    && d.getUser().getHospital().getId().equals(adminHospitalId))
                            .collect(Collectors.toList());
                }
            } else if (user.getRole() == Role.SUPER_ADMIN) {
                // Super Admin: Filter by requested hospitalId if provided, otherwise return all
                if (hospitalId != null) {
                    return doctorRepo.findAll().stream()
                            .filter(d -> d.getUser() != null && d.getUser().getHospital() != null
                                    && d.getUser().getHospital().getId().equals(hospitalId))
                            .collect(Collectors.toList());
                }
            }
        }
        return doctorRepo.findAll();
    }
    
    @CacheEvict(value = "doctors", allEntries = true)

    public Doctor updateStatus(Long id, boolean active) {
        Doctor doctor = doctorRepo.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
        doctor.setActive(active);
        return doctorRepo.save(doctor);
    }
    
    @CacheEvict(value = "doctors", allEntries = true)

    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        Doctor doctor = doctorRepo.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Update Doctor Entity
        doctor.setName(updatedDoctor.getName());
        doctor.setSpecialty(updatedDoctor.getSpecialty());
        doctor.setPhone(updatedDoctor.getPhone());
        doctor.setConsultationFee(updatedDoctor.getConsultationFee());
        doctor.setQualifications(updatedDoctor.getQualifications());

        Doctor savedDoctor = doctorRepo.save(doctor);

        // Sync with User Entity
        if (doctor.getUser() != null) {
            User user = doctor.getUser();
            boolean changed = false;

            if (updatedDoctor.getName() != null && !updatedDoctor.getName().equals(user.getName())) {
                user.setName(updatedDoctor.getName());
                changed = true;
            }
            if (updatedDoctor.getSpecialty() != null && !updatedDoctor.getSpecialty().equals(user.getSpecialty())) {
                user.setSpecialty(updatedDoctor.getSpecialty());
                changed = true;
            }
            if (updatedDoctor.getPhone() != null && !updatedDoctor.getPhone().equals(user.getPhone())) {
                user.setPhone(updatedDoctor.getPhone());
                changed = true;
            }

            if (changed) {
                userRepo.save(user);
            }
        }

        return savedDoctor;
    }
}