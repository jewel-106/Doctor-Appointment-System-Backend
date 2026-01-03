package com.appointment.demo.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "divisions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Division {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String nameEn;
    @Column(length = 100)
    private String nameBn;
    @Column(unique = true, length = 10)
    private String code;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "division", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<District> districts;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}