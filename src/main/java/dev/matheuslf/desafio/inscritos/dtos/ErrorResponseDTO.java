package dev.matheuslf.desafio.inscritos.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Objeto padrão de resposta para erros retornados pela API")
public record ErrorResponseDTO(
        @Schema(description = "Momento em que o erro ocorreu", example = "2025-10-20T18:35:12")
        LocalDateTime timestamp,

        @Schema(description = "Status code do erro ocorrido", example = "status_code")
        int status,

        @Schema(description = "Tipo de erro HTTP", example = "Tipo de erro")
        String error,

        @Schema(description = "Mensagem detalhada do erro", example = "Mensagem do erro")
        String message,

        @Schema(description = "Caminho do endpoint onde ocorreu o erro", example = "/localhost:port/**")
        String path
) {

    public ErrorResponseDTO(int status, String error, String message, String path) {
        this(LocalDateTime.now(), status, error, message, path);
    }
}
