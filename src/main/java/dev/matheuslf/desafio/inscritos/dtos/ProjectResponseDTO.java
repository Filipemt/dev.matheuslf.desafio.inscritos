package dev.matheuslf.desafio.inscritos.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "DTO utilizado para devolver uma resposta sobre o Projeto para o usuário")
public record ProjectResponseDTO(
        @Schema(description = "Id do projeto", example = "1")
        Long id,

        @Schema(description = "Nome do projeto", example = "Paper-Trading")
        String name,

        @Schema(description = "Descrição detalhada do projeto", example = "Simulação de transações financeiras")
        String description,

        @Schema(description = "Data de início do projeto", example = "2025-03-01")
        LocalDate startDate,

        @Schema(description = "Data final do projeto", example = "2025-12-10")
        LocalDate endDate) {
}
