package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dtos.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dtos.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.model.entities.Project;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project project;
    private ProjectRequestDTO projectRequestDTO;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Project Description");
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now().plusDays(10));

        projectRequestDTO = new ProjectRequestDTO("Test Project", "Project Description", LocalDate.now(), LocalDate.now().plusDays(10));
    }

    @Test
    void saveProject_Success() {
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponseDTO result = projectService.saveProject(projectRequestDTO);

        assertNotNull(result);
        assertEquals(project.getName(), result.name());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void getAllProjects_Success() {
        when(projectRepository.findAll()).thenReturn(Collections.singletonList(project));

        List<ProjectResponseDTO> result = projectService.getAllProjects();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(project.getName(), result.get(0).name());
        verify(projectRepository).findAll();
    }
}
