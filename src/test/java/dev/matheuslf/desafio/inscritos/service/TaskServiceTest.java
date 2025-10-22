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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskService taskService;

    private Project project;
    private Task task;
    private TaskRequestDTO taskRequestDTO;
    private TaskUpdateDTO taskUpdateDTO;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.setId(1L);
        project.setName("Test Project");

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(Status.TODO);
        task.setPriority(Priority.MEDIUM);
        task.setDueDate(LocalDate.now());
        task.setProjectId(project);

        taskRequestDTO = new TaskRequestDTO("Test Task", "Test Description", Status.TODO, Priority.MEDIUM, LocalDate.now());
        taskUpdateDTO = new TaskUpdateDTO(Status.DONE);
    }

    @Test
    void saveTask_Success() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDTO result = taskService.saveTask(1L, taskRequestDTO);

        assertNotNull(result);
        assertEquals(task.getTitle(), result.title());
        verify(projectRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void saveTask_ProjectNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.saveTask(1L, taskRequestDTO));

        verify(projectRepository).findById(1L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void getTasksByFilters_Success() {
        when(taskRepository.findByFilters(Status.TODO, Priority.MEDIUM, 1L)).thenReturn(Collections.singletonList(task));

        List<TaskResponseDTO> result = taskService.getTasksByFilters(Status.TODO, Priority.MEDIUM, 1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(task.getTitle(), result.get(0).title());
        verify(taskRepository).findByFilters(Status.TODO, Priority.MEDIUM, 1L);
    }

    @Test
    void updateTask_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.updateTask(1L, taskUpdateDTO);

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(task);
        assertEquals(Status.DONE, task.getStatus());
    }

    @Test
    void updateTask_TaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.updateTask(1L, taskUpdateDTO));

        verify(taskRepository).findById(1L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_Success() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepository).existsById(1L);
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void deleteTask_TaskNotFound() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> taskService.deleteTask(1L));

        verify(taskRepository).existsById(1L);
        verify(taskRepository, never()).deleteById(1L);
    }
}
