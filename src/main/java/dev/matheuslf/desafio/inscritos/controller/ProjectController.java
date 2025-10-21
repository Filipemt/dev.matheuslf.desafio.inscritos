package dev.matheuslf.desafio.inscritos.controller;

import dev.matheuslf.desafio.inscritos.dtos.*;
import dev.matheuslf.desafio.inscritos.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@Tag(name = "Projects", description = "Controller para gerenciamento de projetos")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @Operation(
            summary = "Salva um projeto",
            description = "Cria um projeto baseado nos dados passados no corpo da requisição"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Projeto salvo com sucesso",
                    content = @Content(
                            schema = @Schema(implementation = TaskResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida — erro de validação ou formato incorreto de dados",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    public ResponseEntity<ProjectResponseDTO> saveProject(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do projeto a ser criado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProjectRequestDTO.class)))
            @RequestBody @Valid ProjectRequestDTO dto) {
        ProjectResponseDTO savedProject = projectService.saveProject(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
    }

    @GetMapping
    @Operation(
            summary = "Lista todos os projetos",
            description = "Retorna uma lista com todos os projetos"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Projetos listados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProjectResponseDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida — erro de validação ou formato incorreto de dados",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }
}
