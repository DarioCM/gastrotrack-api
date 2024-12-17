# GastroTrack API

**GastroTrack** is a RESTful API built with **Spring Boot** to help individuals with gastritis track their food intake and identify ingredients or dishes that trigger symptoms. It provides insights to help users manage their health effectively.

---

## Features
- **User Management**: Secure user registration and authentication (JWT & OAuth2).
- **Food & Symptom Logging**: Track individual foods or dishes and associated symptoms.
- **Daily Diet Logs**: Maintain daily logs of meals.
- **Data Persistence**: PostgreSQL for structured storage and Redis for caching.
- **Containerization**: Docker Compose to simplify environment setup.

---

## Tech Stack
- **Java 17**
- **Spring Boot**
- **PostgreSQL**
- **Redis**
- **Lombok**
- **Spring Security (JWT & OAuth2)**
- **Docker Compose**

---

## Quick Start

1. Clone the repository:
   ```bash
   git clone https://github.com/<your-username>/gastrotrack-api.git
   cd gastrotrack-api
