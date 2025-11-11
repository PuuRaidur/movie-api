# 🎬 Movies API

A robust RESTful API for managing a movie database, built with **Spring Boot**, **JPA**, and **SQLite**.

## 🌟 Features

- Full **CRUD operations** for **Movies**, **Genres**, and **Actors**
- **Many-to-Many relationships**:
    - Movies ↔ Genres (e.g., *Inception* is Action + Sci-Fi + Thriller)
    - Movies ↔ Actors (e.g., *The Avengers* stars multiple actors)
- **Filtering & Search**:
    - Get movies by **genre**, **release year**, or **actor**
    - Search movies by **title** (case-insensitive partial match)
- **Pagination** support for all list endpoints (`?page=0&size=20`)
- **Partial updates** using `PATCH`
- **Safe deletion** with optional **force-delete** for entities in use
- **Input validation** and **global error handling** (400, 404, 500 with clear messages)
- **Sample data** included (loaded via API — no hardcoding)

---

## ⚙️ Setup & Installation

### Prerequisites
- **Java 25+**
- **Gradle** (included via wrapper)
- **SQLite** (no install needed — embedded via JDBC)
- **Postman Collection** (included in the project and needs to be imported)

### Running the Application

1. Clone or download this project
2. Open a terminal in the project root
3. Start the server:
   ```bash
   ./gradlew bootRun
The API will be available at:
🔗 http://localhost:8080