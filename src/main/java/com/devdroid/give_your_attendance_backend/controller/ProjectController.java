// java
package com.devdroid.give_your_attendance_backend.controller;

import com.devdroid.give_your_attendance_backend.dto.ApiResponse;
import com.devdroid.give_your_attendance_backend.entity.Project;
import com.devdroid.give_your_attendance_backend.entity.Task;
import com.devdroid.give_your_attendance_backend.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST controller that manages projects and tasks.
 *
 * <p>Base path: {@code /api/projects}</p>
 *
 * <p>Endpoints:
 * <ul>
 *   <li>POST /api/projects - create a new project</li>
 *   <li>GET /api/projects - list all projects</li>
 *   <li>GET /api/projects/assigned - list projects assigned to the authenticated user</li>
 *   <li>POST /api/projects/{projectId}/assign/{userId} - assign a project to a user</li>
 *   <li>POST /api/projects/tasks - create a new task</li>
 *   <li>GET /api/projects/{projectId}/tasks - list tasks for a project visible to the authenticated user</li>
 * </ul>
 * </p>
 *
 * <p>Authentication: endpoints expect a valid authenticated user (via Spring Security).
 * The controller casts {@code Authentication.getPrincipal()} to {@code Long} and uses it as the user id.</p>
 */
@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    private final ProjectService projectService;

    /**
     * Constructor injection for the {@link ProjectService}.
     *
     * @param projectService service handling project and task business logic
     */
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Create a new project.
     *
     * <p>Request body: a {@link Project} entity containing project fields. The service is responsible
     * for validation and persistence.</p>
     *
     * @param project the project to create
     * @return 200 OK with an {@link ApiResponse} containing the created project on success,
     *         or 400 Bad Request with an {@link ApiResponse} containing the error message on failure
     */
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

    /**
     * Retrieve all projects.
     *
     * @return 200 OK with an {@link ApiResponse} containing a list of {@link Project} on success,
     *         or 400 Bad Request with an {@link ApiResponse} containing the error message on failure
     */
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

    /**
     * Retrieve projects assigned to the authenticated user.
     *
     * <p>The method expects {@code Authentication.getPrincipal()} to return the current user's id
     * as a {@link Long}.</p>
     *
     * @param authentication the Spring Security authentication object
     * @return 200 OK with an {@link ApiResponse} containing a list of assigned {@link Project},
     *         or 400 Bad Request with an {@link ApiResponse} containing the error message on failure
     */
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

    /**
     * Assign a project to a user.
     *
     * @param projectId id of the project to assign
     * @param userId id of the user to assign the project to
     * @return 200 OK with an {@link ApiResponse} on success,
     *         or 400 Bad Request with an {@link ApiResponse} containing the error message on failure
     */
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

    /**
     * Create a new task.
     *
     * <p>Request body: a {@link Task} entity. The service handles validation and persistence.</p>
     *
     * @param task task to create
     * @return 200 OK with an {@link ApiResponse} containing the created {@link Task} on success,
     *         or 400 Bad Request with an {@link ApiResponse} containing the error message on failure
     */
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

    /**
     * Retrieve tasks for a project that are visible/assigned to the authenticated user.
     *
     * <p>The method expects {@code Authentication.getPrincipal()} to return the current user's id
     * as a {@link Long}.</p>
     *
     * @param projectId id of the project whose tasks are requested
     * @param authentication the Spring Security authentication object
     * @return 200 OK with an {@link ApiResponse} containing a list of {@link Task} on success,
     *         or 400 Bad Request with an {@link ApiResponse} containing the error message on failure
     */
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
