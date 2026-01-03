package com.appointment.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "n_admins")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private String name;

    private String phone;

    private String department;

    @OneToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;
}
