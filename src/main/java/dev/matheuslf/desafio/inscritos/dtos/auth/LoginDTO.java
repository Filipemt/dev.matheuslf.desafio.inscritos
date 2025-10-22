package dev.matheuslf.desafio.inscritos.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(description = "DTO utilizado para receber os dados de login do usuário")
public record LoginDTO(
        @Schema(description = "E-mail do usuário", example = "user@email.com") @Email String email,
        @Schema(description = "Senha do usuário", example = "SenhaFort&123") String password) {
}
