package dev.matheuslf.desafio.inscritos.controller;

import dev.matheuslf.desafio.inscritos.dtos.TaskRequestDTO;
import dev.matheuslf.desafio.inscritos.dtos.TaskResponseDTO;
import dev.matheuslf.desafio.inscritos.dtos.TaskUpdateDTO;
import dev.matheuslf.desafio.inscritos.model.enums.Priority;
import dev.matheuslf.desafio.inscritos.model.enums.Status;
import dev.matheuslf.desafio.inscritos.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getTaskByFilters(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Long projectId) {

        List<TaskResponseDTO> tasks = taskService.getTasksByFilters(status, priority, projectId);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/{projectId}")
    public ResponseEntity<TaskResponseDTO> saveTask(@PathVariable Long projectId, @RequestBody @Valid TaskRequestDTO dto) {
        return ResponseEntity.ok(taskService.saveTask(projectId, dto));
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<Void> updateTask(@PathVariable Long projectId, @Valid @RequestBody TaskUpdateDTO dto) {
        taskService.updateTask(projectId, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
