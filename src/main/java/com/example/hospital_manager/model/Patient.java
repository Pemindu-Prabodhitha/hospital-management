package com.example.hospital_manager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    @Column(nullable = false)
    private String fullName;

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be positive")
    private Integer age;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Phone number is required")
    private String phone;

    private String address;
}