package com.appointment.demo.Service;

import com.appointment.demo.model.Doctor;
import com.appointment.demo.Repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepo;

    public List<Doctor> getAll() {
        return doctorRepo.findAll();
    }

    public Doctor updateStatus(Long id, boolean active) {
        Doctor doctor = doctorRepo.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
        doctor.setActive(active);
        return doctorRepo.save(doctor);
    }

    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        Doctor doctor = doctorRepo.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
        doctor.setName(updatedDoctor.getName());
        doctor.setSpecialty(updatedDoctor.getSpecialty());
        doctor.setPhone(updatedDoctor.getPhone());
        doctor.setConsultationFee(updatedDoctor.getConsultationFee());
        doctor.setQualifications(updatedDoctor.getQualifications());
        return doctorRepo.save(doctor);
    }
}