package com.example.hospital_manager.repository;

import com.example.hospital_manager.model.Appointment;
import com.example.hospital_manager.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctor(AppUser doctor);
    boolean existsByPatientId(Long patientId);
}