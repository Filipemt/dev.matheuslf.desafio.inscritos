package dev.matheuslf.desafio.inscritos.dtos;

import dev.matheuslf.desafio.inscritos.model.enums.Priority;
import dev.matheuslf.desafio.inscritos.model.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TaskRequestDTO(

        @NotBlank(message = "É obrigatório definir um título")
        @Size(min = 3, max = 150, message = "Título deve ter no mínimo {min} e no máximo {max} caracteres")
        String title,

        @Size(max = 150, message = "Título deve ter no no máximo {max} caracteres")
        String description,

        Status status,
        Priority priority,
        LocalDate dueDate
) {
}
