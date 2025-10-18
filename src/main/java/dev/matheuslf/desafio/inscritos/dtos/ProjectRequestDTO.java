package dev.matheuslf.desafio.inscritos.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;


public record ProjectRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 100, message = "Nome deve ter no mínimo {min} e no máximo {max} caracteres")
        String name,

        @Size(max = 100, message = "Nome deve ter no máximo {max} caracteres")
        String description,

        @NotNull(message = "Data de início do projeto é obrigatório")
        LocalDate startDate,
        LocalDate endDate


) {
}
