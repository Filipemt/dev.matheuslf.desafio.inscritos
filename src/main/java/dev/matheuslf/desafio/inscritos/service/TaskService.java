package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dtos.TaskRequestDTO;
import dev.matheuslf.desafio.inscritos.dtos.TaskResponseDTO;
import dev.matheuslf.desafio.inscritos.dtos.TaskUpdateDTO;
import dev.matheuslf.desafio.inscritos.exceptions.NotFoundException;
import dev.matheuslf.desafio.inscritos.model.entities.Project;
import dev.matheuslf.desafio.inscritos.model.entities.Task;
import dev.matheuslf.desafio.inscritos.model.enums.Priority;
import dev.matheuslf.desafio.inscritos.model.enums.Status;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import dev.matheuslf.desafio.inscritos.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public TaskResponseDTO saveTask(Long projectId, TaskRequestDTO dto) {
        // Todo: Tratar a exceção NotFoundException
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Projeto não encontrado!"));

        Task task = mapperToEntity(dto, project);

        Task savedTask = taskRepository.save(task);

        return mapperToResponseDTO(savedTask);
    }

    public List<TaskResponseDTO> getTasksByFilters(Status status, Priority priority, Long projectId) {
        return taskRepository.findByFilters(status, priority, projectId)
                .stream()
                .map(TaskService::mapperToResponseDTO)
                .toList();
    }

    public void updateTask(Long taskId, TaskUpdateDTO dto) {
        Task existTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task não encontrada"));

        existTask.setStatus(dto.status());
        taskRepository.save(existTask);
    }

    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new NotFoundException("Task não encontrada!");
        }
         taskRepository.deleteById(taskId);
    }

    private static TaskResponseDTO mapperToResponseDTO(Task savedTask) {
        return new TaskResponseDTO(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.getStatus(),
                savedTask.getPriority(),
                savedTask.getDueDate(),
                savedTask.getProjectId().getId()
        );
    }

    private static Task mapperToEntity(TaskRequestDTO dto, Project project) {
        Task task = new Task();
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setStatus(dto.status());
        task.setPriority(dto.priority());
        task.setDueDate(dto.dueDate());
        task.setProjectId(project);

        return task;
    }
}
