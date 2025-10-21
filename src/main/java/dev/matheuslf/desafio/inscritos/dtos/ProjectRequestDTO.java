package dev.matheuslf.desafio.inscritos.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "DTO utilizado para criação de um novo projeto")
public record ProjectRequestDTO(

        @Schema(description = "Nome do projeto", example = "Paper-Trading")
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 100, message = "Nome deve ter no mínimo {min} e no máximo {max} caracteres")
        String name,

        @Schema(description = "Descrição detalhada do projeto", example = "Simulação de transações financeiras")
        @Size(max = 255, message = "Nome deve ter no máximo {max} caracteres")
        String description,

        @Schema(description = "Data de início para o projeto", example = "2025-03-01")
        @NotNull(message = "Data de início do projeto é obrigatório")
        LocalDate startDate,

        @Schema(description = "Data final do projeto", example = "2025-12-10")
        @Future(message = "A data de término deve ser uma data futura")
        LocalDate endDate


) {
}
