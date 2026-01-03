package com.appointment.demo.model;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "n_patients")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private Long userId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String phone;
    private String address;
    @Column(columnDefinition = "TEXT")
    private String medicalHistory;
    @OneToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;
}