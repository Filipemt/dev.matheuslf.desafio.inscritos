package dev.matheuslf.desafio.inscritos.dtos;

import dev.matheuslf.desafio.inscritos.model.enums.Priority;
import dev.matheuslf.desafio.inscritos.model.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "DTO utilizado para criação de nova task")
public record TaskRequestDTO(

        @Schema(description = "Título/nome da task", example = "Atualizar documentação")
        @NotBlank(message = "É obrigatório definir um título")
        @Size(min = 3, max = 150, message = "Título deve ter no mínimo {min} e no máximo {max} caracteres")
        String title,

        @Schema(description = "Descrição detalhada da task", example = "Adicionar guia para rodar o projeto localmente")
        @Size(max = 150, message = "Descrição deve ter no no máximo {max} caracteres")
        String description,

        @Schema(description = "Status atual da task", example = "TODO")
        Status status,

        @Schema(description = "Prioridade atual da task", example = "LOW")
        Priority priority,

        @Schema(description = "Data final para concluir a task", example = "2025-12-10")
        LocalDate dueDate
) {
}
