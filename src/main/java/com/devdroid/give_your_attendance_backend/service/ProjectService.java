package com.devdroid.give_your_attendance_backend.service;

import com.devdroid.give_your_attendance_backend.entity.Project;
import com.devdroid.give_your_attendance_backend.entity.ProjectAssignment;
import com.devdroid.give_your_attendance_backend.entity.Task;
import com.devdroid.give_your_attendance_backend.repository.ProjectAssignmentRepository;
import com.devdroid.give_your_attendance_backend.repository.ProjectRepository;
import com.devdroid.give_your_attendance_backend.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ProjectAssignmentRepository projectAssignmentRepository;

    public ProjectService(ProjectRepository projectRepository,
                         TaskRepository taskRepository,
                         ProjectAssignmentRepository projectAssignmentRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.projectAssignmentRepository = projectAssignmentRepository;
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public Task createTask(Task task) {
        // Validate project exists
        projectRepository.findById(task.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return taskRepository.save(task);
    }

    public List<Task> getTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public void assignProjectToUser(Long projectId, Long userId) {
        // Validate project exists
        projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        ProjectAssignment assignment = new ProjectAssignment();
        assignment.setProjectId(projectId);
        assignment.setUserId(userId);
        projectAssignmentRepository.save(assignment);
    }

    public List<Project> getUserAssignedProjects(Long userId) {
        List<ProjectAssignment> assignments = projectAssignmentRepository.findByUserId(userId);
        return assignments.stream()
                .map(assignment -> projectRepository.findById(assignment.getProjectId()).orElse(null))
                .filter(project -> project != null)
                .collect(Collectors.toList());
    }

    public List<Task> getUserAssignedTasks(Long userId, Long projectId) {
        // Check if user is assigned to the project
        List<ProjectAssignment> assignments = projectAssignmentRepository.findByUserId(userId);
        boolean isAssigned = assignments.stream()
                .anyMatch(assignment -> assignment.getProjectId().equals(projectId));

        if (!isAssigned) {
            throw new RuntimeException("User is not assigned to this project");
        }

        return taskRepository.findByProjectId(projectId);
    }

    public List<Task> getAllTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }
}
