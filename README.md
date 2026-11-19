# Hospital Management System — Secure RESTful API

A secure, stateless RESTful Backend API for a Hospital Management System built with **Spring Boot**, **Spring Security 6+**, **JSON Web Tokens (JWT)**, **Spring Data JPA**, and **MySQL**.

Developed for **COSC 32133 / BECS 32263 — Full-Stack Software Development** (Department of Statistics & Computer Science, University of Kelaniya).

---

## Features & Security Architecture

- **Stateless JWT Authentication (`HS384`):** Generates signed JWT tokens upon login containing `sub` (username), `role`, `iat`, and `exp` claims.
- **Role-Based Access Control (RBAC):** Enforces endpoint and method-level authorization using `SecurityFilterChain` and `@PreAuthorize` across roles (`ADMIN`, `DOCTOR`, `NURSE`, `RECEPTIONIST`, `PATIENT`).
- **Password Encryption:** Secures all user passwords in MySQL using `BCryptPasswordEncoder`.
- **Custom JSON Security Handlers:** Returns structured JSON error responses for `401 Unauthorized` (`JsonAuthenticationEntryPoint`) and `403 Forbidden` (`JsonAccessDeniedHandler`) directly from the filter chain.
- **Global Exception Handling:** Standardized JSON error payloads (`ErrorResponse`) across validation errors, missing resources, conflicts, and access violations.
- **Database Seeding (`DataLoader`):** Automatically seeds default staff accounts and sample patient records on initial startup.

---

## Tech Stack

- **Java** 17 / 21
- **Spring Boot** (Web, Data JPA, Security, Validation, DevTools)
- **JJWT (`io.jsonwebtoken` 0.11.5)** for token creation and signature verification
- **MySQL** relational database
- **Lombok** & **Maven**

---

## Project Structure

```text
src/main/java/com/example/hospital_manager/
├── config/          # DataLoader (initial users & sample patients)
├── controller/      # REST controllers (Auth, Staff, Patient, Appointment, MedicalRecord)
├── dto/             # Java records for request/response payloads & errors
├── exception/       # ApiException & GlobalExceptionHandler
├── model/           # JPA entities (AppUser, Patient, Appointment, MedicalRecord) & enums
├── repository/      # Spring Data JPA repositories
├── security/        # JwtService, JwtAuthenticationFilter, SecurityConfig & JSON handlers
└── service/         # Business logic services
```

---

## Getting Started

### 1. Prerequisites
- JDK 17 or 21
- Maven
- MySQL Server running on `localhost:3306`

### 2. Configuration
Update `src/main/resources/application.properties` with your MySQL credentials if needed:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
jwt.secret=MySuperSecretKeyForHospitalManagementSystemAssignment2026!@#
jwt.expiration=3600000
```

### 3. Run the Application
```bash
./mvnw spring-boot:run
```
The API will start at `http://localhost:8080`.

---

## Default Seeded Accounts

| Role | Username | Password |
| :--- | :--- | :--- |
| `ADMIN` | `admin` | `admin123` |
| `DOCTOR` | `dr.perera` | `doctor123` |
| `NURSE` | `nurse.silva` | `nurse123` |
| `RECEPTIONIST` | `reception` | `recep123` |

---

## API Endpoints Overview

All protected endpoints require the HTTP header:
```http
Authorization: Bearer <JWT_TOKEN>
```

| Method | Endpoint | Allowed Roles | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Public (`PATIENT`) / `ADMIN` (Staff) | Register a new account |
| `POST` | `/api/auth/login` | Public | Authenticate user & return JWT token |
| `GET` | `/api/auth/me` | Authenticated | View current logged-in user profile |
| `GET` | `/api/users` | `ADMIN` | List all registered system users |
| `GET` | `/api/doctors` | All Authenticated Roles | List available doctors |
| `POST` | `/api/admin/doctors` | `ADMIN` | Create a new doctor profile |
| `GET` | `/api/patients` (`& /api/admin/patients`) | `ADMIN`, `DOCTOR`, `NURSE`, `RECEPTIONIST` | List or search patients (`?name=`) |
| `POST` | `/api/patients` | `ADMIN`, `RECEPTIONIST` | Register a new patient |
| `PUT` | `/api/patients/{id}` | `ADMIN`, `RECEPTIONIST` | Update patient details |
| `DELETE` | `/api/patients/{id}` | `ADMIN` | Delete a patient (blocked if records exist) |
| `POST` | `/api/appointments` (`& /api/patients/appointments`) | `ADMIN`, `RECEPTIONIST`, `PATIENT` | Book a future appointment |
| `GET` | `/api/appointments` (`& /api/doctors/appointments`) | `ADMIN`, `DOCTOR`, `NURSE`, `RECEPTIONIST` | View appointments (filtered for doctors) |
| `PATCH` | `/api/appointments/{id}/status` | `ADMIN`, `DOCTOR`, `RECEPTIONIST` | Update appointment status |
| `POST` | `/api/records` | `DOCTOR` | Create diagnosis & prescription record |
| `GET` | `/api/records/patient/{patientId}` | `ADMIN`, `DOCTOR`, `NURSE` | View medical history of a patient |