package com.appointment.demo.Service;
import com.appointment.demo.Repository.DoctorRepository;
import com.appointment.demo.Repository.UserRepository;
import com.appointment.demo.model.Doctor;
import com.appointment.demo.model.Role;
import com.appointment.demo.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@RequiredArgsConstructor
public class DoctorMigrationService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    @Transactional
    public String syncDoctorEmails() {
        List<Doctor> doctors = doctorRepository.findAll();
        int updated = 0;
        for (Doctor doctor : doctors) {
            if (doctor.getEmail() == null || doctor.getEmail().isEmpty()) {
                List<User> doctorUsers = userRepository.findAll().stream()
                        .filter(u -> u.getRole() == Role.DOCTOR)
                        .filter(u -> u.getName().equalsIgnoreCase(doctor.getName()))
                        .toList();
                if (!doctorUsers.isEmpty()) {
                    doctor.setEmail(doctorUsers.get(0).getEmail());
                    doctorRepository.save(doctor);
                    updated++;
                }
            }
        }
        return "Updated " + updated + " doctor records with email addresses";
    }
}