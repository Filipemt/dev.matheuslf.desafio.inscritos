package dev.matheuslf.desafio.inscritos.dtos;

import dev.matheuslf.desafio.inscritos.model.enums.Priority;
import dev.matheuslf.desafio.inscritos.model.enums.Status;

import java.time.LocalDate;

public record TaskResponseDTO(Long id, String title, String description, Status status, Priority priority, LocalDate dueDate, Long projectId) {
}
