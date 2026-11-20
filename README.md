# RepoRadar – GitHub Repository Searcher

RepoRadar is a Java Spring Boot REST API application that allows users to search GitHub repositories using the GitHub REST API, store repository information in PostgreSQL, and retrieve saved repositories using filters and sorting options.

## Features

- Search GitHub repositories by name or keyword.
- Filter GitHub search results by programming language.
- Sort GitHub search results by stars, forks, or last updated date.
- Store GitHub repository information in PostgreSQL.
- Update existing repository records instead of creating duplicates.
- Retrieve all stored repositories.
- Filter repositories by programming language and minimum stars.
- Sort stored repositories by stars, forks, or updated date.
- Handle GitHub API errors and rate limits.
- Validate incoming API requests.
- Interactive API documentation using Swagger UI.
- Unit and controller tests using JUnit and Mockito.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 25 | Programming language |
| Spring Boot 4.1.2 | Backend framework |
| Spring Web | REST API development |
| Spring Data JPA | Database operations |
| PostgreSQL 18 | Relational database |
| Hibernate | ORM |
| Maven | Dependency management and build |
| GitHub REST API | Repository search |
| Spring RestClient | External API communication |
| JUnit 5 | Unit testing |
| Mockito | Mocking dependencies |
| Swagger / OpenAPI | API documentation |

## Project Structure

```text
repo-radar/
├── src/
│   ├── main/
│   │   ├── java/com/kishan/reporadar/
│   │   │   ├── client/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   └── response/
│   │   │   ├── entity/
│   │   │   ├── exception/
│   │   │   ├── mapper/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   │       └── impl/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/kishan/reporadar/
├── .gitignore
├── pom.xml
├── mvnw
├── mvnw.cmd
├── README.md
└── RepoRadar_Postman_Collection.json
```

## Prerequisites

Make sure the following are installed:

- Java 25 or compatible configured JDK.
- PostgreSQL 18.
- Git.
- Maven (or use the included Maven Wrapper).
- Postman (optional, for API testing).

## Database Configuration

Create a PostgreSQL database:

```sql
CREATE DATABASE repo_radar;
```

Configure the following environment variables before starting the application:

**Windows PowerShell:**

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/repo_radar"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="YOUR_DATABASE_PASSWORD"
```

Replace `YOUR_DATABASE_PASSWORD` with your local PostgreSQL password.

Never commit actual database credentials to GitHub.

## Installation and Setup

Clone the repository:

```bash
git clone https://github.com/Kishandev-debug/RepoRadar.git
```

Navigate to the project directory:

```bash
cd RepoRadar
```

Build the project:

```bash
./mvnw clean install
```

On Windows PowerShell:

```powershell
.\mvnw.cmd clean install
```

## Run the Application

Start the Spring Boot application:

```powershell
.\mvnw.cmd spring-boot:run
```

The application runs at:

```text
http://localhost:8080
```

## API Documentation

Interactive Swagger documentation:

http://localhost:8080/swagger-ui.html

OpenAPI JSON:

http://localhost:8080/v3/api-docs

## API Endpoints

### 1. Search GitHub Repositories

**Endpoint:** `POST /api/github/search`

Searches GitHub repositories and saves the returned repository information in PostgreSQL.

**Request Body:**

```json
{
  "query": "spring boot",
  "language": "Java",
  "sort": "stars"
}
```

Supported sorting options:

- `stars`
- `forks`
- `updated`

### 2. Retrieve Stored Repositories

**Endpoint:** `GET /api/github/repositories`

Retrieves repositories previously stored in PostgreSQL.

**Optional query parameters:**

| Parameter | Description |
|---|---|
| language | Filter by programming language |
| minStars | Minimum star count |
| sort | Sort by stars, forks, or updated date |

**Example:**

```text
GET /api/github/repositories?language=Java&minStars=100&sort=stars
```

**Other examples:**

```text
GET /api/github/repositories
GET /api/github/repositories?language=Java
GET /api/github/repositories?minStars=100
GET /api/github/repositories?sort=forks
GET /api/github/repositories?sort=updated
```

## Error Handling

| Scenario | HTTP Status |
|---|---:|
| Successful request | 200 OK |
| Invalid request | 400 Bad Request |
| GitHub rate limit exceeded | 503 Service Unavailable |
| GitHub API failure | 502 Bad Gateway |
| Unexpected server error | 500 Internal Server Error |

## Testing

Run all automated tests:

```powershell
.\mvnw.cmd clean test
```

The project includes tests for:

- Application context loading.
- Repository service operations.
- Repository persistence and filtering.
- GitHub API client responses.
- GitHub API error handling and rate limits.
- REST controller endpoints and validation.

## Postman Collection

A Postman collection is included in the repository:

`RepoRadar_Postman_Collection.json`

Import this file into Postman to test the API endpoints.

The collection includes repository search, retrieval, filtering, and sorting requests.

## Database Verification

You can verify saved repositories using PostgreSQL:

```sql
SELECT COUNT(*) FROM github_repositories;

SELECT github_id, name, owner_name, language, stars, forks
FROM github_repositories
ORDER BY stars DESC;
```

## Author

**Kishan Kumar Rana**

GitHub: https://github.com/Kishandev-debug

LinkedIn: https://www.linkedin.com/in/kishan-rana-646659244/
