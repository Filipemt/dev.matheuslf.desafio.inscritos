package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dtos.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dtos.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.model.entities.Project;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    // Todo: Adicionar paginação
    // Todo: Tratar exceção de erro nos campos obrigatórios

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectResponseDTO saveProject(ProjectRequestDTO dto) {
        Project project = mapperToEntity(dto);
        Project savedProject = projectRepository.save(project);

        return mapperToResponseDTO(savedProject);

    }

    public List<ProjectResponseDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();

        return projects.stream()
                .map(ProjectService::mapperToResponseDTO)
                .toList();
    }


    private static Project mapperToEntity(ProjectRequestDTO dto) {
        Project project = new Project();
        project.setName(dto.name());
        project.setDescription(dto.description());
        project.setStartDate(dto.startDate());
        project.setEndDate(dto.endDate());

        return project;
    }

    private static ProjectResponseDTO mapperToResponseDTO(Project savedProject) {
        return new ProjectResponseDTO(
                savedProject.getId(),
                savedProject.getName(),
                savedProject.getDescription(),
                savedProject.getStartDate(),
                savedProject.getEndDate()
        );
    }
}
