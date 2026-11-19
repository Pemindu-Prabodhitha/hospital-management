package com.example.hospital_manager.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(optional = false)
    @JoinColumn(name = "doctor_id")
    private AppUser doctor;

    @Column(nullable = false)
    private String diagnosis;

    @Column(nullable = false)
    private String prescription;

    private String notes;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}