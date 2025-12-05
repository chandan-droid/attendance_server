package com.devdroid.give_your_attendance_backend.controller;

import com.devdroid.give_your_attendance_backend.dto.ApiResponse;
import com.devdroid.give_your_attendance_backend.entity.Project;
import com.devdroid.give_your_attendance_backend.entity.Task;
import com.devdroid.give_your_attendance_backend.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody Project project) {
        try {
            Project created = projectService.createProject(project);
            return ResponseEntity.ok(new ApiResponse(true, "Project created successfully", created));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProjects() {
        try {
            List<Project> projects = projectService.getAllProjects();
            return ResponseEntity.ok(new ApiResponse(true, "Projects retrieved", projects));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/assigned")
    public ResponseEntity<?> getUserAssignedProjects(Authentication authentication) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            List<Project> projects = projectService.getUserAssignedProjects(userId);
            return ResponseEntity.ok(new ApiResponse(true, "Assigned projects retrieved", projects));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/{projectId}/assign/{userId}")
    public ResponseEntity<?> assignProjectToUser(@PathVariable Long projectId,
                                                 @PathVariable Long userId) {
        try {
            projectService.assignProjectToUser(projectId, userId);
            return ResponseEntity.ok(new ApiResponse(true, "Project assigned to user successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/tasks")
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        try {
            Task created = projectService.createTask(task);
            return ResponseEntity.ok(new ApiResponse(true, "Task created successfully", created));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<?> getProjectTasks(@PathVariable Long projectId,
                                             Authentication authentication) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            List<Task> tasks = projectService.getUserAssignedTasks(userId, projectId);
            return ResponseEntity.ok(new ApiResponse(true, "Tasks retrieved", tasks));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
}

