# Backend - BD Healthcare

Welcome to the backend server of **BD Healthcare**. This is a robust, scalable, and secure Spring Boot application that powers the system.

---

## 🛠️ Technical Specifications

- **Framework:** [Spring Boot 3.3.4](https://spring.io/projects/spring-boot)
- **Language:** Java 17
- **Security:** Spring Security with **JWT (JSON Web Tokens)** for stateless authentication.
- **Database:** MySQL (Cloud-ready with Aiven Cloud support).
- **ORM:** Spring Data JPA with Hibernate.
- **Build Tool:** Maven.
- **Features:** 
  - Automated Database Schema Generation (`ddl-auto=update`).
  - Environment-based configuration for cloud deployment (Render, Aiven).
  - Secure Password Hashing with BCrypt.
  - Multi-role support: **Patient, Doctor, Admin, Super Admin**.

---

## 🚀 Key Features

- **Auth System:** Secure login, registration, and OTP-based password reset.
- **Appointment Engine:** Complex logic for booking, scheduling, and status tracking (Pending, Accepted, Rejected).
- **Location API:** Integrated support for Bangladesh's location hierarchy (Division -> District -> Upazila).
- **Profile Management:** Dynamic profile updates including bio, specialty, and contact info.
- **Dashboard Analytics:** Specialized endpoints for system stats and analytics.

---

## 📂 Project Structure

```text
src/main/java/com/appointment/demo/
├── Controller/    # REST API Endpoints
├── Service/       # Business Logic Layer
├── Repository/    # Database Access Layer (JPA)
├── model/         # Entity Classes (POJOs)
├── DTO/           # Data Transfer Objects
├── config/        # Security & Bean Configurations
└── Security/      # JWT Filter & Auth Logic
```

---

## 📥 Getting Started (Local Setup)

### Prerequisites
- **JDK 17** installed.
- **MySQL** installed (or an Aiven Cloud account).
- **Maven** (optional, you can use the included wrapper `./mvnw`).

### Installation
1.  **Clone the Repo:**
    ```bash
    git clone [backend-repo-url]
    cd Backend-Doctor-Appointment-system
    ```

2.  **Configuration:**
    - Create a `src/main/resources/secrets.properties` file (This is git-ignored for safety).
    - Add your database credentials:
    ```properties
    SPRING_DATASOURCE_URL=jdbc:mysql://[host]:[port]/[db_name]?sslMode=REQUIRED
    SPRING_DATASOURCE_USERNAME=[your_user]
    SPRING_DATASOURCE_PASSWORD=[your_password]
    ```

3.  **Run the Server:**
    ```bash
    ./mvnw spring-boot:run
    ```
    *The server will start on `http://localhost:5000`.*

---

## 🔐 Demo Credentials

| Role | Email | Password |
| :--- | :--- | :--- |
| **Super Admin** | `sadmin@gmail.com` | `123456` |
| **Admin** | `admin@gmail.com` | `123456` |
| **Doctor** | `doctor@gmail.com` | `123456` |
| **Patient** | `patient@gmail.com` | `123456` |

---

## 🌐 API Endpoints (Quick Overview)

- **Auth:** `POST /api/auth/login`, `POST /api/auth/register`
- **Appointments:** `GET /api/appointments/patient/{id}`, `POST /api/appointments/book`
- **Admin:** `GET /api/admin/stats`, `GET /api/admin/analytics`
- **Doctors:** `GET /api/doctors/list`

---

## 🤝 Contributing
Feel free to fork this project and submit PRs for any improvements.


