package dev.matheuslf.desafio.inscritos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.matheuslf.desafio.inscritos.config.SecurityConfig;
import dev.matheuslf.desafio.inscritos.dtos.ProjectRequestDTO;
import dev.matheuslf.desafio.inscritos.dtos.ProjectResponseDTO;
import dev.matheuslf.desafio.inscritos.jwt.JwtService;
import dev.matheuslf.desafio.inscritos.security.CustomUserDetailsService;
import dev.matheuslf.desafio.inscritos.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
@Import(SecurityConfig.class) // Importa a configuração de segurança
@WithMockUser // Simula um usuário autenticado para os endpoints protegidos
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectService projectService;

    // Mocks para as dependências da SecurityConfig
    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private ProjectRequestDTO projectRequestDTO;
    private ProjectResponseDTO projectResponseDTO;

    @BeforeEach
    void setUp() {
        projectRequestDTO = new ProjectRequestDTO("Test Project", "Description", LocalDate.now(), LocalDate.now().plusDays(10));
        projectResponseDTO = new ProjectResponseDTO(1L, "Test Project", "Description", LocalDate.now(), LocalDate.now().plusDays(10));
    }

    @Test
    void saveProject_ShouldReturnCreated() throws Exception {
        when(projectService.saveProject(any(ProjectRequestDTO.class))).thenReturn(projectResponseDTO);

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Project"));
    }

    @Test
    void getAllProjects_ShouldReturnOk() throws Exception {
        when(projectService.getAllProjects()).thenReturn(Collections.singletonList(projectResponseDTO));

        mockMvc.perform(get("/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test Project"));
    }
}
