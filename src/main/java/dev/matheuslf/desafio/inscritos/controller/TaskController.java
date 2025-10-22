package dev.matheuslf.desafio.inscritos.controller;

import dev.matheuslf.desafio.inscritos.dtos.ErrorResponseDTO;
import dev.matheuslf.desafio.inscritos.dtos.TaskRequestDTO;
import dev.matheuslf.desafio.inscritos.dtos.TaskResponseDTO;
import dev.matheuslf.desafio.inscritos.dtos.TaskUpdateDTO;
import dev.matheuslf.desafio.inscritos.model.enums.Priority;
import dev.matheuslf.desafio.inscritos.model.enums.Status;
import dev.matheuslf.desafio.inscritos.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tasks", description = "Controller para gerenciamento de tarefas")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(
            summary = "Listar tarefas com filtros opcionais",
            description = "Retorna uma lista de tarefas filtradas por status, prioridade e/ou projeto. Todos os filtros são opcionais."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarefas listadas com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TaskResponseDTO.class))
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
    public ResponseEntity<List<TaskResponseDTO>> getTaskByFilters(
            @Parameter(description = "Status atual da tarefa (TODO, IN_PROGRESS, DONE)", example = "TODO")
            @RequestParam(required = false) Status status,

            @Parameter(description = "Nível de prioridade da tarefa (LOW, MEDIUM, HIGH)", example = "HIGH")
            @RequestParam(required = false) Priority priority,

            @Parameter(description = "Identificador do projeto associado à tarefa", example = "1")
            @RequestParam(required = false) Long projectId
    ) {
        List<TaskResponseDTO> tasks = taskService.getTasksByFilters(status, priority, projectId);
        return ResponseEntity.ok(tasks);
    }


    @PostMapping("/{projectId}")
    @Operation(
            summary = "Salvar uma task",
            description = "Cria uma task relacionada a algum projeto já existente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Task salva com sucesso",
                    content = @Content(
                            schema = @Schema(implementation = TaskResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida — erro de validação ou formato incorreto de dados",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Projeto para vincular a task não foi encontrado",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    public ResponseEntity<TaskResponseDTO> saveTask(@Parameter(description = "Identificador do projeto", example = "1", required = true) @PathVariable Long projectId,
                                                    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados da task a ser criada",
                                                            required = true,
                                                            content = @Content(schema = @Schema(implementation = TaskRequestDTO.class)))
                                                    @RequestBody @Valid TaskRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.saveTask(projectId, dto));
    }

    @PutMapping("/{taskId}")
    @Operation(
            summary = "Atualiza uma task",
            description = "Atualiza uma task baseado no seu ID informado"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Task atualizada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida — erro de validação ou formato incorreto de dados",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task para ser atualizada não encontrada",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    public ResponseEntity<Void> updateTask(@Parameter(description = "Identificador da task", example = "1", required = true) @PathVariable Long taskId,
                                           @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados da task para ser atualizada",
                                                   required = true,
                                                   content = @Content(schema = @Schema(implementation = TaskUpdateDTO.class)))
                                           @Valid @RequestBody TaskUpdateDTO dto) {

        taskService.updateTask(taskId, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{taskId}")
    @Operation(
            summary = "Deleta uma task",
            description = "Deleta uma task baseado no ID informado"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Task deletada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida — erro de validação ou formato incorreto de dados",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task para ser deletada não encontrada",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
