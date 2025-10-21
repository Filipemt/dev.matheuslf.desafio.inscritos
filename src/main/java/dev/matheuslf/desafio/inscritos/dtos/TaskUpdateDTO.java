package dev.matheuslf.desafio.inscritos.dtos;

import dev.matheuslf.desafio.inscritos.model.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO Utilizado para atualização do status da task")
public record TaskUpdateDTO(
        @Schema(description = "Novo status da task", example = "DONE")
        Status status) {
}
