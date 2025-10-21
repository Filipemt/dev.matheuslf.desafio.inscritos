package dev.matheuslf.desafio.inscritos.dtos;

import dev.matheuslf.desafio.inscritos.model.enums.Priority;
import dev.matheuslf.desafio.inscritos.model.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "DTO Utilizado para devolver uma resposta sobre a Task para o usuário")
public record TaskResponseDTO(
        @Schema(description = "Id da task", example = "1")
        Long id,

        @Schema(description = "Título/nome da task", example = "Atualizar documentação")
        String title,

        @Schema(description = "Descrição detalhada da task", example = "Adicionar guia para rodar o projeto localmente")
        String description,

        @Schema(description = "Status atual da task", example = "TODO")
        Status status,

        @Schema(description = "Prioridade atual da task", example = "LOW")
        Priority priority,

        @Schema(description = "Data final para concluir a task", example = "2025-12-10")
        LocalDate dueDate,

        @Schema(description = "Projeto a qual a task pertence", example = "2")
        Long projectId
) {
}
