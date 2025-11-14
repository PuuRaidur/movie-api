# 🎬 Movies API

A robust RESTful API for managing a movie database, built with **Spring Boot**, **JPA**, and **SQLite**.

## 🌟 Features

- Full **CRUD operations** for **Movies**, **Genres**, and **Actors**
- **Many-to-Many relationships**:
    - Movies ↔ Genres (e.g., *Inception* is Action + Sci-Fi + Thriller)
    - Movies ↔ Actors (e.g., *The Avengers* stars multiple actors)
- **Filtering & Search**:
    - Get movies by **genre**, **release year**, or **actor**
- **Partial updates** using `PATCH`
- **Safe deletion** with optional **force-delete** for entities in use
- **Input validation** and **global error handling** (400, 404 error codes)
- **Sample data** included (loaded via API — no hardcoding)

---

## ⚙️ Setup & Installation

### Prerequisites
- **Java 25+**
- **Gradle** (included via wrapper)
- **SQLite** (no install needed — embedded via JDBC)
- **Postman** (needs to be installed)
- **Postman Collection** (included in the project as a JSON file and needs to be imported into Postman for use)

### Running the Application

1. Clone or download this project
2. Open a terminal in the project root
3. Start the server:
   ```bash
   ./gradlew bootRun
   
4. **Postman Guide**:
1) Open Postman and click "Import"
2) In "files" select the JSON file
3) Once the collection is imported you can start using different endpoints under "Genre", "Actor" and "Movie" section

The API will be available at:
🔗 http://localhost:8080
