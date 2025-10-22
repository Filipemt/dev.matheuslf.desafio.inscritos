package dev.matheuslf.desafio.inscritos.controller;

import dev.matheuslf.desafio.inscritos.dtos.ErrorResponseDTO;
import dev.matheuslf.desafio.inscritos.dtos.auth.AuthResponseDTO;
import dev.matheuslf.desafio.inscritos.dtos.auth.LoginDTO;
import dev.matheuslf.desafio.inscritos.dtos.auth.RegisterDTO;
import dev.matheuslf.desafio.inscritos.security.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Controller para autenticação de usuários")
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    @Operation(
            summary = "Registro de um novo usuário",
            description = "Cria um novo usuário no sistema e retorna um token JWT de autenticação"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário registrado com sucesso",
                    content = @Content(
                            schema = @Schema(implementation = AuthResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida — erro de validação ou formato incorreto de dados",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email já cadastrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterDTO dto) {
        AuthResponseDTO authResponseDTO = authenticationService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponseDTO);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login de usuário",
            description = "Login de um usuário no sistema e retorna um token JWT de autenticação"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário autenticado com sucesso",
                    content = @Content(
                            schema = @Schema(implementation = AuthResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida — erro de validação ou formato incorreto de dados",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não Autorizado — Senha incorreta",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO dto) {
        AuthResponseDTO authResponseDTO = authenticationService.login(dto);
        return ResponseEntity.ok(authResponseDTO);
    }
}