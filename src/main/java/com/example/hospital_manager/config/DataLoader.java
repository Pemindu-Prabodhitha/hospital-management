package com.example.hospital_manager.config;

import com.example.hospital_manager.model.AppUser;
import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.model.Role;
import com.example.hospital_manager.repository.PatientRepository;
import com.example.hospital_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(AppUser.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("System Administrator")
                    .role(Role.ADMIN)
                    .build());

            userRepository.save(AppUser.builder()
                    .username("dr.perera")
                    .password(passwordEncoder.encode("doctor123"))
                    .fullName("Dr. Kasun Perera")
                    .role(Role.DOCTOR)
                    .build());

            userRepository.save(AppUser.builder()
                    .username("nurse.silva")
                    .password(passwordEncoder.encode("nurse123"))
                    .fullName("Nurse Nadeesha Silva")
                    .role(Role.NURSE)
                    .build());

            userRepository.save(AppUser.builder()
                    .username("reception")
                    .password(passwordEncoder.encode("recep123"))
                    .fullName("Amali Fernando")
                    .role(Role.RECEPTIONIST)
                    .build());
        }

        if (patientRepository.count() == 0) {
            patientRepository.save(Patient.builder()
                    .fullName("Saman Kumara")
                    .age(34)
                    .gender("Male")
                    .phone("0771234567")
                    .address("Kelaniya")
                    .build());

            patientRepository.save(Patient.builder()
                    .fullName("Dilini Jayasinghe")
                    .age(28)
                    .gender("Female")
                    .phone("0719876543")
                    .address("Colombo")
                    .build());
        }
    }
}