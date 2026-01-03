package com.appointment.demo.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Entity
@Table(name = "hospitals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 200)
    private String name;
    @Column(nullable = false, unique = true, length = 50)
    private String code;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "division_id", nullable = false)
    private Division division;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "upazila_id")
    private Upazila upazila;
    @Column(name = "phone_primary", nullable = false, length = 20)
    private String phonePrimary;
    @Column(name = "phone_secondary", length = 20)
    private String phoneSecondary;
    @Column(length = 100)
    private String email;
    @Column(name = "emergency_hotline", length = 20)
    private String emergencyHotline;
    @Column(name = "address_line1", nullable = false, length = 255)
    private String addressLine1;
    @Column(name = "address_line2", length = 255)
    private String addressLine2;
    @Column(name = "postal_code", length = 10)
    private String postalCode;
    @Column(name = "logo_url", columnDefinition = "LONGTEXT")
    private String logoUrl;
    @Column(length = 255)
    private String tagline;
    @Column(length = 255)
    private String website;
    @Column(name = "facebook_url", length = 255)
    private String facebookUrl;
    @Column(name = "twitter_url", length = 255)
    private String twitterUrl;
    @Column(name = "linkedin_url", length = 255)
    private String linkedinUrl;
    @Column(name = "operating_hours", columnDefinition = "TEXT")
    private String operatingHours;
    @Column(name = "is_active")
    private Boolean isActive = true;
    @Column(name = "is_featured")
    private Boolean isFeatured = false;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String services;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}