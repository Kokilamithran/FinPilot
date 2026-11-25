# FinPilot

Personal finance manager with a Spring Boot REST API and a React single-page app. Track accounts, transactions, categories, budgets, financial goals, recurring transactions, shared group expenses, notifications, and import history.

## Stack

- **Backend:** Java 21, Spring Boot 4.1, Spring Security, JWT, Spring Data JPA, MySQL 8
- **Frontend:** React 18, Vite 6, Tailwind CSS 4, Zustand, Axios, Recharts / Chart.js

## Layout

```
finpilot/   Spring Boot backend (Maven)
frontend/   React + Vite frontend (npm)
```

## Prerequisites

- JDK 21 or newer
- Node.js 20 or newer
- MySQL 8 (create a database named `finpilot`)

## Configuration

The backend reads configuration from environment variables. Copy `.env.example` as a reference and set these in your shell:

| Variable | Default | Description |
| --- | --- | --- |
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/finpilot` | JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | `root` | Database user |
| `SPRING_DATASOURCE_PASSWORD` | empty | Database password |
| `JWT_SECRET` | required | Secret used to sign JWTs (at least 32 characters) |
| `JWT_EXPIRATION` | `86400000` | Token lifetime in milliseconds |
| `VITE_API_BASE_URL` | `http://localhost:8080` | Backend URL used by the frontend build |

Generate a secret, for example: `node -e "console.log(require('crypto').randomBytes(48).toString('base64'))"`.

PowerShell example:

```powershell
$env:SPRING_DATASOURCE_USERNAME = "root"
$env:SPRING_DATASOURCE_PASSWORD = "your-local-db-password"
$env:JWT_SECRET = "your-generated-secret"
```

## Run locally

Backend (port 8080):

```powershell
mvn -f finpilot/pom.xml spring-boot:run
```

Frontend (port 5173):

```powershell
npm --prefix frontend install
npm --prefix frontend run dev
```

API documentation is available at `http://localhost:8080/swagger-ui.html`. The backend allows CORS from `http://localhost:5173`.

## Build and test

```powershell
mvn -f finpilot/pom.xml verify
npm --prefix frontend run build
```

Backend tests use an in-memory H2 database and need no external services. CI runs the same commands on pushes and pull requests.

## License

MIT
