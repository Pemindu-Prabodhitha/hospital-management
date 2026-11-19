package com.example.hospital_manager.service;

import com.example.hospital_manager.dto.AppointmentRequest;
import com.example.hospital_manager.dto.AppointmentResponse;
import com.example.hospital_manager.exception.ApiException;
import com.example.hospital_manager.model.*;
import com.example.hospital_manager.repository.AppointmentRepository;
import com.example.hospital_manager.repository.PatientRepository;
import com.example.hospital_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Patient not found with ID: " + request.patientId()));

        AppUser doctor = userRepository.findById(request.doctorId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Doctor not found with ID: " + request.doctorId()));

        if (doctor.getRole() != Role.DOCTOR) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Selected user is not a DOCTOR");
        }

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDateTime(request.appointmentDateTime())
                .reason(request.reason())
                .status(AppointmentStatus.SCHEDULED)
                .build();

        return toResponse(appointmentRepository.save(appointment));
    }

    public List<AppointmentResponse> getAppointmentsForCurrentUser(String username) {
        AppUser currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));

        List<Appointment> appointments = (currentUser.getRole() == Role.DOCTOR)
                ? appointmentRepository.findByDoctor(currentUser)
                : appointmentRepository.findAll();

        return appointments.stream().map(this::toResponse).toList();
    }

    public AppointmentResponse updateStatus(Long id, AppointmentStatus newStatus, String username) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Appointment not found with ID: " + id));

        AppUser currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));

        if (currentUser.getRole() == Role.DOCTOR && !appointment.getDoctor().getId().equals(currentUser.getId())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Doctors can only update their own appointments");
        }

        appointment.setStatus(newStatus);
        return toResponse(appointmentRepository.save(appointment));
    }

    private AppointmentResponse toResponse(Appointment a) {
        return new AppointmentResponse(
                a.getId(),
                a.getPatient().getId(),
                a.getPatient().getFullName(),
                a.getDoctor().getId(),
                a.getDoctor().getFullName(),
                a.getAppointmentDateTime(),
                a.getReason(),
                a.getStatus()
        );
    }
}