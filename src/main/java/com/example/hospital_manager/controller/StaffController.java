package com.example.hospital_manager.controller;

import com.example.hospital_manager.dto.RegisterRequest;
import com.example.hospital_manager.dto.UserResponse;
import com.example.hospital_manager.model.Role;
import com.example.hospital_manager.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StaffController {

    private final AuthService authService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }

    @GetMapping("/doctors")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<List<UserResponse>> getAllDoctors() {
        return ResponseEntity.ok(authService.getAllDoctors());
    }

    @PostMapping("/admin/doctors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createDoctor(@Valid @RequestBody RegisterRequest request) {
        RegisterRequest doctorRequest = new RegisterRequest(
                request.username(),
                request.password(),
                request.fullName(),
                Role.DOCTOR
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(doctorRequest));
    }
}