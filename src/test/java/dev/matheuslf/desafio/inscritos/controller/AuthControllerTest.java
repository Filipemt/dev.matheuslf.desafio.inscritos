package dev.matheuslf.desafio.inscritos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.matheuslf.desafio.inscritos.config.SecurityConfig;
import dev.matheuslf.desafio.inscritos.dtos.auth.AuthResponseDTO;
import dev.matheuslf.desafio.inscritos.dtos.auth.LoginDTO;
import dev.matheuslf.desafio.inscritos.dtos.auth.RegisterDTO;
import dev.matheuslf.desafio.inscritos.jwt.JwtService;
import dev.matheuslf.desafio.inscritos.security.AuthenticationService;
import dev.matheuslf.desafio.inscritos.security.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class) // Importa a configuração de segurança
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    // Mocks para as dependências da SecurityConfig
    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private RegisterDTO registerDTO;
    private LoginDTO loginDTO;
    private AuthResponseDTO authResponseDTO;

    @BeforeEach
    void setUp() {
        registerDTO = new RegisterDTO("Test User", "test@example.com", "password");
        loginDTO = new LoginDTO("test@example.com", "password");
        authResponseDTO = new AuthResponseDTO("test-token");
    }

    @Test
    void register_ShouldReturnCreated() throws Exception {
        when(authenticationService.register(any(RegisterDTO.class))).thenReturn(authResponseDTO);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("test-token"));
    }

    @Test
    void login_ShouldReturnOk() throws Exception {
        when(authenticationService.login(any(LoginDTO.class))).thenReturn(authResponseDTO);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"));
    }
}
