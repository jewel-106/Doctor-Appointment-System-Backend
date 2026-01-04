package com.appointment.demo.model;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "n_doctors")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private Long userId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String specialty;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false, unique = true)
    private String email;
    private Double consultationFee;
    @Column(columnDefinition = "TEXT")
    private String qualifications;
    @Builder.Default
    private boolean active = true;
    @OneToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;
}