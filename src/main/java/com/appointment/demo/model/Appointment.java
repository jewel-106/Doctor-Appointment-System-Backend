package com.appointment.demo.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
@Entity
@Table(name = "n_appointments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "patientName", nullable = false)
    private String patientName;
    @Column(name = "patientEmail", nullable = false)
    private String patientEmail;
    @Column(name="patientPhone",nullable = true)
    private String patientPhone;
    @Column(name = "doctorId", nullable = false)
    private Long doctorId;
    @Column(name = "appointmentDate", nullable = false)
    private LocalDate appointmentDate;
    @Column(name = "appointmentTime", nullable = false)
    private LocalTime appointmentTime;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.pending;
    @Column(columnDefinition = "TEXT")
    private String notes;
    @Column(columnDefinition = "TEXT")
    private String patientComment;
    @Column(columnDefinition = "TEXT")
    private String reason;
    @Column(columnDefinition = "LONGTEXT")
    private String previousPrescription;
    @Column(columnDefinition = "TEXT")
    private String prescription;
    @Column(columnDefinition = "TEXT")
    private String diagnosis;
    private String patientAge;
    private String patientGender;
    private String emergencyContact;
    @ManyToOne
    @JoinColumn(name = "doctorId", insertable = false, updatable = false)
    private Doctor doctor;
    public enum Status {
        pending, confirmed, cancelled, complete
    }
}