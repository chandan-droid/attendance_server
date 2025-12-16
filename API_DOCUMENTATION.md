# Complete API Documentation - Attendance Management System

## Base URL
```
http://localhost:8080
```

## Authentication
Most endpoints require JWT authentication. Include the token in the Authorization header:
```
Authorization: Bearer <your_jwt_token>
```

---

## 📋 Table of Contents
1. [Authentication Endpoints](#authentication-endpoints)
2. [User Management Endpoints](#user-management-endpoints)
3. [Attendance Endpoints](#attendance-endpoints)
4. [Project Management Endpoints](#project-management-endpoints)
5. [Leave Management Endpoints](#leave-management-endpoints)
6. [Attendance Query Endpoints](#attendance-query-endpoints)
7. [Geofence Endpoints](#geofence-endpoints)

---

## Authentication Endpoints

### 1. Sign Up
**Endpoint:** `POST /api/auth/signup`  
**Auth Required:** No  
**Description:** Register a new user account

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "1234567890",
  "employeeId": "EMP001",
  "password": "password123",
  "role": "EMPLOYEE",
  "workMode": "ONSITE"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "userId": 1,
      "name": "John Doe",
      "email": "john.doe@example.com",
      "role": "EMPLOYEE",
      "workMode": "ONSITE"
    }
  }
}
```

### 2. Login
**Endpoint:** `POST /api/auth/login`  
**Auth Required:** No  
**Description:** Authenticate user and get JWT token

**Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "userId": 1,
      "name": "John Doe",
      "email": "john.doe@example.com",
      "role": "EMPLOYEE"
    }
  }
}
```

---

## User Management Endpoints

### 1. Get Current User Profile
**Endpoint:** `GET /api/users/me`  
**Auth Required:** Yes  
**Description:** Get authenticated user's profile

**Response (200 OK):**
```json
{
  "success": true,
  "message": "User profile retrieved",
  "data": {
    "userId": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "1234567890",
    "employeeId": "EMP001",
    "role": "EMPLOYEE",
    "workMode": "ONSITE",
    "createdAt": "2025-12-01T10:00:00"
  }
}
```

### 2. Get All Users (Admin)
**Endpoint:** `GET /api/users`  
**Auth Required:** Yes  
**Description:** Get list of all users

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Users retrieved",
  "data": [
    {
      "userId": 1,
      "name": "John Doe",
      "email": "john.doe@example.com",
      "role": "EMPLOYEE",
      "workMode": "ONSITE"
    },
    {
      "userId": 2,
      "name": "Jane Smith",
      "email": "jane.smith@example.com",
      "role": "ADMIN",
      "workMode": "REMOTE"
    }
  ]
}
```

### 3. Update User Work Mode
**Endpoint:** `PUT /api/users/{userId}/work-mode?workMode=REMOTE`  
**Auth Required:** Yes  
**Description:** Update user's work mode (ONSITE/REMOTE)

**Path Parameters:**
- `userId` (Long): User ID

**Query Parameters:**
- `workMode` (String): "ONSITE" or "REMOTE"

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Work mode updated successfully",
  "data": {
    "userId": 1,
    "workMode": "REMOTE"
  }
}
```

---

## Attendance Endpoints

### 1. Punch In
**Endpoint:** `POST /api/attendance/punch-in`  
**Auth Required:** Yes  
**Description:** Record punch in with project and task

**Request Body:**
```json
{
  "projectId": 1,
  "taskId": 5,
  "latitude": 28.7041,
  "longitude": 77.1025,
  "punchType": "IN"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Punch in successful",
  "data": {
    "attendanceId": 101,
    "punchType": "IN",
    "punchTime": "2025-12-16T09:00:00",
    "message": "Punch in successful",
    "sessionId": null
  }
}
```

**Response (400 Bad Request):**
```json
{
  "success": false,
  "message": "You already have an active punch in. Please punch out first before punching in again."
}
```

### 2. Punch Out
**Endpoint:** `POST /api/attendance/punch-out`  
**Auth Required:** Yes  
**Description:** Record punch out

**Request Body:**
```json
{
  "latitude": 28.7041,
  "longitude": 77.1025,
  "punchType": "OUT"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Punch out successful",
  "data": {
    "attendanceId": 102,
    "punchType": "OUT",
    "punchTime": "2025-12-16T18:00:00",
    "message": "Punch out successful. Duration: 540 minutes",
    "sessionId": 50
  }
}
```

### 3. Get Attendance History
**Endpoint:** `GET /api/attendance/history`  
**Auth Required:** Yes  
**Description:** Get user's attendance history

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Attendance history retrieved",
  "data": [
    {
      "attendanceId": 102,
      "userId": 1,
      "projectId": 1,
      "taskId": 5,
      "punchType": "OUT",
      "punchTime": "2025-12-16T18:00:00",
      "latitude": 28.7041,
      "longitude": 77.1025
    },
    {
      "attendanceId": 101,
      "userId": 1,
      "projectId": 1,
      "taskId": 5,
      "punchType": "IN",
      "punchTime": "2025-12-16T09:00:00",
      "latitude": 28.7041,
      "longitude": 77.1025
    }
  ]
}
```

### 4. Get Work Sessions
**Endpoint:** `GET /api/attendance/work-sessions`  
**Auth Required:** Yes  
**Description:** Get user's work sessions

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Work sessions retrieved",
  "data": [
    {
      "sessionId": 50,
      "userId": 1,
      "projectId": 1,
      "taskId": 5,
      "punchInId": 101,
      "punchOutId": 102,
      "durationMinutes": 540
    }
  ]
}
```

### 5. Get Total Hours for Project
**Endpoint:** `GET /api/attendance/total-hours/project/{projectId}`  
**Auth Required:** Yes  
**Description:** Get total working hours for a specific project

**Path Parameters:**
- `projectId` (Long): Project ID

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Total working hours retrieved",
  "data": {
    "totalMinutes": 2700,
    "totalHours": 45.0,
    "formattedTime": "45h 0m"
  }
}
```

### 6. Get Total Hours for Day
**Endpoint:** `GET /api/attendance/total-hours/day?date=2025-12-16`  
**Auth Required:** Yes  
**Description:** Get total working hours for a specific day

**Query Parameters:**
- `date` (String, Optional): Date in ISO format (YYYY-MM-DD). Defaults to today.

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Total working hours for 2025-12-16 retrieved",
  "data": {
    "totalMinutes": 540,
    "totalHours": 9.0,
    "formattedTime": "9h 0m"
  }
}
```

### 7. Get Attendance by Date (Admin) ✨ NEW
**Endpoint:** `GET /api/attendance/date/{date}`  
**Auth Required:** Yes (Admin only)  
**Description:** Get all attendance records for a specific date

**Path Parameters:**
- `date` (String): Date in ISO format (YYYY-MM-DD)

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Attendance records for 2025-12-16 retrieved",
  "data": [
    {
      "attendanceId": 101,
      "userId": 1,
      "projectId": 1,
      "taskId": 5,
      "punchType": "IN",
      "punchTime": "2025-12-16T09:00:00",
      "latitude": 28.7041,
      "longitude": 77.1025
    },
    {
      "attendanceId": 103,
      "userId": 2,
      "projectId": 2,
      "taskId": 8,
      "punchType": "IN",
      "punchTime": "2025-12-16T08:30:00",
      "latitude": 28.7041,
      "longitude": 77.1025
    }
  ]
}
```

**Response (403 Forbidden):**
```json
{
  "success": false,
  "message": "Forbidden: Admin access required"
}
```

### 8. Get All Attendance Records (Admin) ✨ NEW
**Endpoint:** `GET /api/attendance/all`  
**Auth Required:** Yes (Admin only)  
**Description:** Get all attendance records across all dates

**Response (200 OK):**
```json
{
  "success": true,
  "message": "All attendance records retrieved",
  "data": [
    {
      "attendanceId": 101,
      "userId": 1,
      "projectId": 1,
      "taskId": 5,
      "punchType": "IN",
      "punchTime": "2025-12-16T09:00:00"
    }
  ]
}
```

---

## Project Management Endpoints

### 1. Create Project
**Endpoint:** `POST /api/projects`  
**Auth Required:** Yes  
**Description:** Create a new project

**Request Body:**
```json
{
  "projectName": "Mobile App Development",
  "description": "E-commerce mobile application",
  "startDate": "2025-01-01",
  "endDate": "2025-06-30"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Project created successfully",
  "data": {
    "projectId": 1,
    "projectName": "Mobile App Development",
    "description": "E-commerce mobile application",
    "startDate": "2025-01-01",
    "endDate": "2025-06-30",
    "createdAt": "2025-12-16T10:00:00"
  }
}
```

### 2. Get All Projects
**Endpoint:** `GET /api/projects`  
**Auth Required:** Yes  
**Description:** Get list of all projects

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Projects retrieved",
  "data": [
    {
      "projectId": 1,
      "projectName": "Mobile App Development",
      "description": "E-commerce mobile application",
      "startDate": "2025-01-01",
      "endDate": "2025-06-30"
    }
  ]
}
```

### 3. Get Assigned Projects
**Endpoint:** `GET /api/projects/assigned`  
**Auth Required:** Yes  
**Description:** Get projects assigned to authenticated user

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Assigned projects retrieved",
  "data": [
    {
      "projectId": 1,
      "projectName": "Mobile App Development"
    }
  ]
}
```

### 4. Assign Project to User
**Endpoint:** `POST /api/projects/{projectId}/assign/{userId}`  
**Auth Required:** Yes  
**Description:** Assign a project to a user

**Path Parameters:**
- `projectId` (Long): Project ID
- `userId` (Long): User ID

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Project assigned to user successfully"
}
```

### 5. Create Task
**Endpoint:** `POST /api/projects/tasks`  
**Auth Required:** Yes  
**Description:** Create a new task under a project

**Request Body:**
```json
{
  "projectId": 1,
  "taskName": "Design UI/UX",
  "description": "Create mockups and wireframes",
  "estimatedHours": 40.5
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Task created successfully",
  "data": {
    "taskId": 5,
    "projectId": 1,
    "taskName": "Design UI/UX",
    "description": "Create mockups and wireframes",
    "estimatedHours": 40.5
  }
}
```

### 6. Get Project Tasks (User)
**Endpoint:** `GET /api/projects/{projectId}/tasks`  
**Auth Required:** Yes  
**Description:** Get tasks for a project (only if user is assigned to the project)

**Path Parameters:**
- `projectId` (Long): Project ID

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Tasks retrieved",
  "data": [
    {
      "taskId": 5,
      "projectId": 1,
      "taskName": "Design UI/UX",
      "description": "Create mockups and wireframes",
      "estimatedHours": 40.5
    }
  ]
}
```

**Response (400 Bad Request):**
```json
{
  "success": false,
  "message": "User is not assigned to this project"
}
```

### 7. Get All Project Tasks (Admin) ✨ NEW
**Endpoint:** `GET /api/projects/{projectId}/tasks/all`  
**Auth Required:** Yes (Admin only)  
**Description:** Get all tasks for a project without assignment check

**Path Parameters:**
- `projectId` (Long): Project ID

**Response (200 OK):**
```json
{
  "success": true,
  "message": "All tasks retrieved",
  "data": [
    {
      "taskId": 5,
      "projectId": 1,
      "taskName": "Design UI/UX",
      "description": "Create mockups and wireframes",
      "estimatedHours": 40.5
    },
    {
      "taskId": 6,
      "projectId": 1,
      "taskName": "Backend Development",
      "description": "API development",
      "estimatedHours": 80.0
    }
  ]
}
```

**Response (403 Forbidden):**
```json
{
  "success": false,
  "message": "Forbidden"
}
```

---

## Leave Management Endpoints

### 1. Apply for Leave
**Endpoint:** `POST /api/leaves/apply`  
**Auth Required:** Yes  
**Description:** Apply for leave

**Request Body:**
```json
{
  "startDate": "2025-12-20",
  "endDate": "2025-12-22",
  "duration": 3,
  "reason": "Family function out of town"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Leave applied successfully",
  "data": {
    "leaveId": 10,
    "userId": 1,
    "startDate": "2025-12-20",
    "endDate": "2025-12-22",
    "duration": 3,
    "reason": "Family function out of town",
    "status": "PENDING",
    "appliedAt": "2025-12-16T10:00:00"
  }
}
```

### 2. Get My Leaves
**Endpoint:** `GET /api/leaves/my`  
**Auth Required:** Yes  
**Description:** Get authenticated user's leave applications

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Leaves retrieved",
  "data": [
    {
      "leaveId": 10,
      "userId": 1,
      "startDate": "2025-12-20",
      "endDate": "2025-12-22",
      "duration": 3,
      "reason": "Family function out of town",
      "status": "PENDING",
      "appliedAt": "2025-12-16T10:00:00",
      "approvedAt": null,
      "adminComment": null
    }
  ]
}
```

### 3. Get All Leaves (Admin)
**Endpoint:** `GET /api/leaves/all`  
**Auth Required:** Yes  
**Description:** Get all leave applications

**Response (200 OK):**
```json
{
  "success": true,
  "message": "All leaves retrieved",
  "data": [
    {
      "leaveId": 10,
      "userId": 1,
      "startDate": "2025-12-20",
      "endDate": "2025-12-22",
      "duration": 3,
      "reason": "Family function",
      "status": "PENDING"
    }
  ]
}
```

### 4. Approve/Reject Leave (Admin)
**Endpoint:** `PUT /api/leaves/{id}/approve`  
**Auth Required:** Yes (Admin)  
**Description:** Approve or reject leave application

**Path Parameters:**
- `id` (Long): Leave ID

**Request Body:**
```json
{
  "status": "APPROVED",
  "adminComment": "Leave approved as per company policy."
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Leave updated successfully",
  "data": {
    "leaveId": 10,
    "status": "APPROVED",
    "approvedAt": "2025-12-16T11:00:00",
    "adminComment": "Leave approved as per company policy."
  }
}
```

---

## Attendance Query Endpoints

### 1. Raise Attendance Query
**Endpoint:** `POST /api/attendance/query`  
**Auth Required:** Yes  
**Description:** Raise a query for missed attendance

**Request Body:**
```json
{
  "queryDate": "2025-12-10",
  "reason": "Forgot to punch in while working from home"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Query raised successfully",
  "data": {
    "queryId": 20,
    "userId": 1,
    "queryDate": "2025-12-10",
    "reason": "Forgot to punch in while working from home",
    "status": "PENDING",
    "createdAt": "2025-12-16T10:00:00"
  }
}
```

### 2. Get My Queries
**Endpoint:** `GET /api/attendance/query/my`  
**Auth Required:** Yes  
**Description:** Get authenticated user's attendance queries

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Queries retrieved",
  "data": [
    {
      "queryId": 20,
      "userId": 1,
      "queryDate": "2025-12-10",
      "reason": "Forgot to punch in",
      "status": "PENDING",
      "createdAt": "2025-12-16T10:00:00",
      "resolvedAt": null,
      "adminComment": null
    }
  ]
}
```

### 3. Get All Queries (Admin)
**Endpoint:** `GET /api/attendance/query/all`  
**Auth Required:** Yes  
**Description:** Get all attendance queries

**Response (200 OK):**
```json
{
  "success": true,
  "message": "All queries retrieved",
  "data": [
    {
      "queryId": 20,
      "userId": 1,
      "queryDate": "2025-12-10",
      "reason": "Forgot to punch in",
      "status": "PENDING"
    }
  ]
}
```

### 4. Approve/Reject Query (Admin)
**Endpoint:** `PUT /api/attendance/query/{id}/approve`  
**Auth Required:** Yes (Admin)  
**Description:** Approve or reject attendance query

**Path Parameters:**
- `id` (Long): Query ID

**Request Body:**
```json
{
  "status": "APPROVED",
  "adminComment": "Accepted. Please ensure timely attendance in future."
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Query updated successfully",
  "data": {
    "queryId": 20,
    "status": "APPROVED",
    "resolvedAt": "2025-12-16T11:00:00",
    "adminComment": "Accepted. Please ensure timely attendance in future."
  }
}
```

---

## Geofence Endpoints

### 1. Create Geofence
**Endpoint:** `POST /api/geofence/create`  
**Auth Required:** Yes  
**Description:** Create a new geofence location

**Request Body:**
```json
{
  "locationName": "Head Office",
  "latitude": 28.7041,
  "longitude": 77.1025,
  "radiusMeters": 200
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Geofence created successfully",
  "data": {
    "geofenceId": 1,
    "locationName": "Head Office",
    "latitude": 28.7041,
    "longitude": 77.1025,
    "radiusMeters": 200,
    "createdAt": "2025-12-16T10:00:00"
  }
}
```

### 2. Get All Geofences
**Endpoint:** `GET /api/geofence/list`  
**Auth Required:** Yes  
**Description:** Get list of all geofence locations

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Geofences retrieved",
  "data": [
    {
      "geofenceId": 1,
      "locationName": "Head Office",
      "latitude": 28.7041,
      "longitude": 77.1025,
      "radiusMeters": 200
    }
  ]
}
```

---

## Error Response Format

All endpoints return errors in a consistent format:

```json
{
  "success": false,
  "message": "Error description here"
}
```

### Common HTTP Status Codes

- **200 OK**: Request successful
- **400 Bad Request**: Invalid request data or business logic error
- **401 Unauthorized**: Missing or invalid authentication token
- **403 Forbidden**: User doesn't have permission (e.g., non-admin accessing admin endpoint)
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error

---

## Data Enums

### User Role
- `EMPLOYEE`
- `ADMIN`

### Work Mode
- `ONSITE`
- `REMOTE`

### Punch Type
- `IN`
- `OUT`

### Leave/Query Status
- `PENDING`
- `APPROVED`
- `REJECTED`

---

## Testing with Postman/cURL

### Example cURL for Login:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "password123"
  }'
```

### Example cURL with Authentication:
```bash
curl -X GET http://localhost:8080/api/attendance/history \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

## Summary of New Admin Endpoints ✨

1. **GET /api/projects/{projectId}/tasks/all** - View all tasks in a project (admin only)
2. **GET /api/attendance/date/{date}** - View all attendance for a specific date (admin only)
3. **GET /api/attendance/all** - View all attendance records (admin only)
4. **GET /api/leaves/all** - View all leave applications (admin only)
5. **GET /api/attendance/query/all** - View all attendance queries (admin only)

All admin endpoints check for `User.Role.ADMIN` and return 403 Forbidden if accessed by non-admin users.

