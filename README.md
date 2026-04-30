#  PickThem Backend API (Spring Boot)

This project is the **backend API** of **PickThem**, a CAN 2025 prediction platform.
It exposes a **secure REST API** consumed by a **Next.js frontend**.

---

## Classes diagram

![Architecture PickThem](/images/diagram.png)

## 🛠️ Tech Stack

* **Java 17**
* **Spring Boot**
* **Spring Security (JWT Authentication)**
* **Spring Data JPA (Hibernate)**
* **PostgreSQL**
* **Swagger / OpenAPI**
* **Maven**
* **Docker**

---

## 📂 Project Structure

```
src/
 └── main/
     ├── java/com/example/pickthem/
     │   ├── controller/
     │   ├── service/
     │   ├── repository/
     │   ├── model/
     │   ├── dto/
     │   ├── config/
      │   ├── exceptions/
     │   └── PickThemApplication.java
     └── resources/
         ├── application.yml
```

---

## 🔐 Authentication & Security

* JWT-based authentication
* Two types of endpoints:

  * 🔓 **Public APIs** (no authentication required)
  * 🔒 **Private APIs** (JWT required)



---

## 📌 Main API Endpoints

### 🔑 Authentication

```
POST /api/v1/auth/login
POST /api/v1/auth/register
```

### 👥 Teams (Public)

```
GET /api/v1/teams
```

### 🏆 Tours (Private)

```
GET /api/v1/tours
```

### 🎯 Predictions (Private)

```
POST   /api/v1/predictions/create
PATCH  /api/v1/predictions/{id}
GET    /api/v1/predictions/me
```

---

## 📘 API Documentation (Swagger)

Swagger UI is available at:

```
http://localhost:8081/swagger-ui/index.html
```

or

```
http://localhost:8081/swagger-ui.html
```

---

## ⚙️ Configuration

### Database Configuration (`application.properties`)

```yaml
spring-datasource-url: jdbc:postgresql://localhost:5432/pickthem
spring-datasource-username: postgres
spring-datasource-password: password
```

---

## ▶️ Running the Project Locally

### 1️⃣ Clone the repository

```bash
git clone https://github.com/your-username/pickthem-backend.git
cd pickthem-backend
```

### 2️⃣ Run the application

```bash
docker compose up build
```

The API will be available at:

```
http://localhost:8081
```
---

## 📌 Best Practices Applied

* Clear separation of concerns (Controller / Service / Repository)
* DTO usage for API communication
* Centralized exception handling
* Role-based security
* API documentation with Swagger

---

## 👨‍💻 Author

Developed by **Mouad Sadik, Badr Ziane, Khalil Baidouri and Ibrahim Ait Kadiss**

---

## 📄 License

This project is licensed under the **MIT License**.


"# Spring-Auth" 
