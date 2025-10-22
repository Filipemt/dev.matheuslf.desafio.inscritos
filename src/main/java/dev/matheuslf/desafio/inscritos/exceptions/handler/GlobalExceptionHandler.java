package dev.matheuslf.desafio.inscritos.exceptions.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import dev.matheuslf.desafio.inscritos.dtos.ErrorResponseDTO;
import dev.matheuslf.desafio.inscritos.exceptions.DuplicatedRegisterException;
import dev.matheuslf.desafio.inscritos.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private String extractPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }

    private ResponseEntity<ErrorResponseDTO> buildError(HttpStatus status, String message, String path) {
        return ResponseEntity.status(status)
                .body(new ErrorResponseDTO(
                        LocalDateTime.now(),
                        status.value(),
                        status.getReasonPhrase(),
                        message,
                        path
                ));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(NotFoundException ex, WebRequest request) {
        String path = extractPath(request);
        logger.warn("Recurso não encontrado: {} | Path: {}", ex.getMessage(), path);
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), path);
    }

    @ExceptionHandler(DuplicatedRegisterException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicatedRegisterException(DuplicatedRegisterException ex, WebRequest request) {
        String path = extractPath(request);
        logger.warn("Registro duplicado: {} | Path: {}", ex.getMessage(), path);
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), path);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        String path = extractPath(request);
        logger.warn("Senha inválida: {} | Path: {}", ex.getMessage(), path);
        return buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), path);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        String path = extractPath(request);

        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining("; "));

        logger.warn("Erro de validação: {} | Path: {}", errors, path);
        return buildError(HttpStatus.BAD_REQUEST, errors, path);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        String path = extractPath(request);
        String message = "Corpo da requisição inválido.";

        if (ex.getCause() instanceof InvalidFormatException invalidFormat &&
                invalidFormat.getTargetType().isEnum()) {

            String invalidValue = String.valueOf(invalidFormat.getValue());
            String enumType = invalidFormat.getTargetType().getSimpleName();

            message = String.format("Valor inválido para enum %s: %s", enumType, invalidValue);
            logger.warn("Enum inválido recebido: {} | Path: {}", invalidValue, path);
        } else {
            logger.warn("Corpo da requisição inválido: {} | Path: {}", ex.getMessage(), path);
        }

        return buildError(HttpStatus.BAD_REQUEST, message, path);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception ex, WebRequest request) {
        String path = extractPath(request);
        logger.error("Erro inesperado: {} | Path: {}", ex.getMessage(), path, ex);
        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno do servidor. Tente novamente mais tarde.",
                path
        );
    }
}
