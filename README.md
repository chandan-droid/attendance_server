# Attendance and Project Monitoring System

A comprehensive Spring Boot application for managing employee attendance with geofencing capabilities and project time tracking.

## Features

1. **User Management**
   - User signup with name, email, phone, employee ID, and password
   - JWT-based authentication
   - Role-based access (EMPLOYEE, ADMIN)
   - Work mode management (ONSITE, REMOTE)

2. **Attendance Management**
   - Multiple punch in/out per day
   - Geofencing validation for ONSITE employees
   - Location tracking with latitude/longitude
   - Project and task selection required for each punch in

3. **Project Management**
   - Create and manage projects
   - Assign projects to multiple employees
   - Create tasks under projects
   - Track time spent on each task

4. **Work Sessions**
   - Automatic calculation of work duration
   - Link punch in/out to create work sessions
   - Track time per project and task

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Gradle

## Database Setup

1. Create MySQL database:
```sql
CREATE DATABASE attendance_db;
```

2. Update `application.properties` with your MySQL credentials:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Run the SQL script provided to create tables, or let Hibernate create them automatically.

## Build and Run

### Using Gradle

```bash
# Build the project
gradlew build

# Run the application
gradlew bootRun
```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication APIs

#### 1. Sign Up
```
POST /api/auth/signup
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "employeeId": "EMP001",
  "password": "password123"
}
```

#### 2. Login
```
POST /api/auth/login
Content-Type: application/json

{
  "emailOrEmployeeId": "john@example.com",
  "password": "password123"
}
```

Response includes JWT token for authentication.

### User APIs (Requires Authentication)

#### Get Current User Profile
```
GET /api/users/me
Authorization: Bearer {token}
```

#### Get All Users
```
GET /api/users
Authorization: Bearer {token}
```

#### Update Work Mode
```
PUT /api/users/{userId}/work-mode?workMode=REMOTE
Authorization: Bearer {token}
```

### Attendance APIs (Requires Authentication)

#### Punch In
```
POST /api/attendance/punch-in
Authorization: Bearer {token}
Content-Type: application/json

{
  "punchType": "IN",
  "projectId": 1,
  "taskId": 1,
  "latitude": 28.6139,
  "longitude": 77.2090
}
```

#### Punch Out
```
POST /api/attendance/punch-out
Authorization: Bearer {token}
Content-Type: application/json

{
  "punchType": "OUT",
  "latitude": 28.6139,
  "longitude": 77.2090
}
```

#### Get Attendance History
```
GET /api/attendance/history
Authorization: Bearer {token}
```

#### Get Work Sessions
```
GET /api/attendance/work-sessions
Authorization: Bearer {token}
```

### Project APIs (Requires Authentication)

#### Create Project
```
POST /api/projects
Authorization: Bearer {token}
Content-Type: application/json

{
  "projectName": "Mobile App Development",
  "description": "Development of mobile application",
  "startDate": "2024-01-01",
  "endDate": "2024-12-31"
}
```

#### Get All Projects
```
GET /api/projects
Authorization: Bearer {token}
```

#### Get User Assigned Projects
```
GET /api/projects/assigned
Authorization: Bearer {token}
```

#### Assign Project to User
```
POST /api/projects/{projectId}/assign/{userId}
Authorization: Bearer {token}
```

#### Create Task
```
POST /api/projects/tasks
Authorization: Bearer {token}
Content-Type: application/json

{
  "projectId": 1,
  "taskName": "UI Design",
  "description": "Design user interface",
  "estimatedHours": 40.5
}
```

#### Get Project Tasks
```
GET /api/projects/{projectId}/tasks
Authorization: Bearer {token}
```

### Geofence APIs (Requires Authentication)

#### Create Geofence Location
```
POST /api/geofence
Authorization: Bearer {token}
Content-Type: application/json

{
  "locationName": "Main Office",
  "latitude": 28.6139,
  "longitude": 77.2090,
  "radiusMeters": 200
}
```

#### Get All Geofences
```
GET /api/geofence
Authorization: Bearer {token}
```

#### Assign Geofence to User
```
POST /api/geofence/assign/{userId}/{geofenceId}
Authorization: Bearer {token}
```

## How It Works

### Geofencing Logic

1. **ONSITE Employees**: Must be within assigned geofence radius during punch in/out
2. **REMOTE Employees**: Can punch in/out from anywhere
3. Geofence validation uses Haversine formula to calculate distance

### Work Session Tracking

1. Employee punches in with project and task selection
2. Employee can punch in/out multiple times per day
3. Each punch out creates a work session
4. Duration is automatically calculated between punch in and punch out
5. Time is tracked per project and task

### Authentication Flow

1. User signs up or logs in
2. Server returns JWT token
3. Client includes token in Authorization header for all protected endpoints
4. Token expires after 24 hours (configurable)

## Security

- Passwords are encrypted using BCrypt
- JWT tokens for stateless authentication
- Role-based access control
- CORS enabled for all origins (configure as needed)

## Project Structure

```
src/main/java/com/devdroid/give_your_attendance_backend/
├── config/              # Security and app configuration
├── controller/          # REST API endpoints
├── dto/                 # Data Transfer Objects
├── entity/              # JPA entities
├── repository/          # Database repositories
├── security/            # JWT and authentication
└── service/             # Business logic
```

## Configuration

Key configuration in `application.properties`:

- `jwt.secret`: Secret key for JWT signing (change for production)
- `jwt.expiration`: Token expiration time in milliseconds (default: 24 hours)
- Database connection settings
- JPA/Hibernate settings

## Notes

- Ensure MySQL is running before starting the application
- Change JWT secret for production deployment
- Update CORS configuration for production
- Consider adding rate limiting for production
- Add proper error handling and logging as needed

