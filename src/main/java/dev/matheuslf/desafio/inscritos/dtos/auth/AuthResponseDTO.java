package dev.matheuslf.desafio.inscritos.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO utilizado para devolver uma token de acesso para o usuário")
public record AuthResponseDTO(
        @Schema(description = "Token de autenticação", example = "eyJhbGciOiJI.eyJzdWIiOiJmaWxpcGVAZ21.xJmIoLsE5reHU") String token
) {
}
