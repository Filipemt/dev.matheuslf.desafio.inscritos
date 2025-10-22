package dev.matheuslf.desafio.inscritos.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO utilizado para receber os dados de registro do usuário")
public record RegisterDTO(
        @Schema(description = "Nome do usuário a ser registrado", example = "nome_do_usuario") String name,
        @Schema(description = "E-mail do usuário a ser registrado", example = "user@email.com") String email,
        @Schema(description = "Senha cadastrada pelo novo usuário", example = "SenhaFort&123") String password) {
}
