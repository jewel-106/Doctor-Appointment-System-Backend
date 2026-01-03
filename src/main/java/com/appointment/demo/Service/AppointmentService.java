package com.appointment.demo.Service;

import com.appointment.demo.Repository.DoctorTimeSlotRepository;
import com.appointment.demo.model.DoctorTimeSlot;
import com.appointment.demo.DTO.AppointmentResponse;
import com.appointment.demo.model.Appointment;
import com.appointment.demo.model.Doctor;
import com.appointment.demo.model.Role;
import com.appointment.demo.model.User;
import com.appointment.demo.Repository.AppointmentRepository;
import com.appointment.demo.Repository.DoctorRepository;
import com.appointment.demo.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepo;
    private final DoctorRepository doctorRepo;
    private final UserRepository userRepo;
    private final DoctorTimeSlotRepository slotRepo;

    public List<AppointmentResponse> getAppointmentsForUser(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(
                        "We couldn't find your account details. Please try logging in again."));
        List<Appointment> appointments;
        if (user.getRole() == Role.SUPER_ADMIN) {
            appointments = appointmentRepo.findAll();
        } else if (user.getRole() == Role.ADMIN) {
            if (user.getHospital() != null) {
                appointments = appointmentRepo.findByDoctor_User_Hospital_Id(user.getHospital().getId());
            } else {
                appointments = new ArrayList<>();
            }
        } else if (user.getRole() == Role.DOCTOR) {
            Doctor doctor = doctorRepo.findByEmail(email).orElse(null);
            if (doctor != null) {
                appointments = appointmentRepo.findByDoctorId(doctor.getId());
            } else {
                appointments = new ArrayList<>();
            }
        } else {
            appointments = appointmentRepo.findByPatientEmail(email);
        }
        return appointments.stream()
                .map(this::toResponse)
                .toList();
    }

    public List<AppointmentResponse> getAll() {
        return appointmentRepo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public AppointmentResponse getById(Long id) {
        return appointmentRepo.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException(
                        "We couldn't find the appointment you're looking for. It might have been removed."));
    }

    public AppointmentResponse create(Appointment request) {
        if (appointmentRepo.existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                request.getDoctorId(), request.getAppointmentDate(), request.getAppointmentTime())) {
            throw new RuntimeException("This time slot is already taken. Please choose another convenient time.");
        }
        java.util.Optional<DoctorTimeSlot> slotOpt = slotRepo.findByDoctorIdAndAvailableDateAndStartTime(
                request.getDoctorId(), request.getAppointmentDate(), request.getAppointmentTime());
        if (slotOpt.isPresent()) {
            var slot = slotOpt.get();
            if (slot.getIsBooked()) {
                throw new RuntimeException("Apologies, this specialist is fully booked for the selected time.");
            }
            slot.setIsBooked(true);
            slotRepo.save(slot);
        }
        request.setStatus(Appointment.Status.pending);
        Appointment saved = appointmentRepo.save(request);
        return toResponse(saved);
    }

    public AppointmentResponse update(Long id, Appointment request, String email) {
        Appointment existing = appointmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        checkAuthorization(user, existing);
        existing.setPatientName(request.getPatientName());
        existing.setPatientEmail(request.getPatientEmail());
        existing.setPatientPhone(request.getPatientPhone());
        existing.setAppointmentDate(request.getAppointmentDate());
        existing.setAppointmentTime(request.getAppointmentTime());
        existing.setReason(request.getReason());
        existing.setPreviousPrescription(request.getPreviousPrescription());
        existing.setPatientAge(request.getPatientAge());
        existing.setPatientGender(request.getPatientGender());
        existing.setEmergencyContact(request.getEmergencyContact());
        existing.setNotes(request.getNotes());
        existing.setPrescription(request.getPrescription());
        existing.setDiagnosis(request.getDiagnosis());
        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }
        existing.setPatientComment(request.getPatientComment());
        Appointment updated = appointmentRepo.save(existing);
        return toResponse(updated);
    }

    @Transactional
    public AppointmentResponse changeStatus(Long id, Appointment.Status status, String email) {
        Appointment apt = appointmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        checkAuthorization(user, apt);
        apt.setStatus(status);
        return toResponse(appointmentRepo.save(apt));
    }

    public void delete(Long id, String email) {
        Appointment apt = appointmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        checkAuthorization(user, apt);
        appointmentRepo.deleteById(id);
    }

    private void checkAuthorization(User user, Appointment apt) {
        if (user.getRole() == Role.SUPER_ADMIN) {
            return;
        } else if (user.getRole() == Role.ADMIN) {
            Doctor doctor = doctorRepo.findById(apt.getDoctorId()).orElse(null);
            if (doctor == null || user.getHospital() == null ||
                    doctor.getUser() == null || doctor.getUser().getHospital() == null ||
                    !user.getHospital().getId().equals(doctor.getUser().getHospital().getId())) {
                throw new RuntimeException("Unauthorized for this hospital's appointment");
            }
        } else if (user.getRole() == Role.DOCTOR) {
            Doctor doctor = doctorRepo.findByEmail(user.getEmail()).orElse(null);
            if (doctor == null || !doctor.getId().equals(apt.getDoctorId())) {
                throw new RuntimeException("Unauthorized for another doctor's appointment");
            }
        } else {
            throw new RuntimeException("As a patient, you can only view and manage your own bookings.");
        }
    }

    private AppointmentResponse toResponse(Appointment a) {
        var doctor = doctorRepo.findById(a.getDoctorId())
                .orElse(null);
        Long hospitalId = (doctor != null && doctor.getUser() != null && doctor.getUser().getHospital() != null)
                ? doctor.getUser().getHospital().getId()
                : null;
        return new AppointmentResponse(
                a.getId(),
                a.getPatientName(),
                a.getPatientEmail(),
                a.getPatientPhone(),
                a.getDoctorId(),
                doctor != null ? doctor.getName() : "Unknown",
                doctor != null ? doctor.getSpecialty() : "Unknown",
                a.getAppointmentDate(),
                a.getAppointmentTime(),
                a.getStatus(),
                a.getNotes(),
                a.getPatientComment(),
                a.getReason(),
                a.getPreviousPrescription(),
                a.getPrescription(),
                a.getDiagnosis(),
                a.getPatientAge(),
                a.getPatientGender(),
                a.getEmergencyContact(),
                hospitalId);
    }
}