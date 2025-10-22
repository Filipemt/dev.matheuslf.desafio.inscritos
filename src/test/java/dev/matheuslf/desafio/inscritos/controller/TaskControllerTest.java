package dev.matheuslf.desafio.inscritos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.matheuslf.desafio.inscritos.config.SecurityConfig;
import dev.matheuslf.desafio.inscritos.dtos.TaskRequestDTO;
import dev.matheuslf.desafio.inscritos.dtos.TaskResponseDTO;
import dev.matheuslf.desafio.inscritos.dtos.TaskUpdateDTO;
import dev.matheuslf.desafio.inscritos.jwt.JwtService;
import dev.matheuslf.desafio.inscritos.model.enums.Priority;
import dev.matheuslf.desafio.inscritos.model.enums.Status;
import dev.matheuslf.desafio.inscritos.security.CustomUserDetailsService;
import dev.matheuslf.desafio.inscritos.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test") // Ativa o perfil de teste
@WebMvcTest(TaskController.class)
@Import(SecurityConfig.class) // Importa a configuração de segurança
@WithMockUser // Simula um usuário autenticado para os endpoints protegidos
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    // Mocks para as dependências da SecurityConfig
    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private TaskRequestDTO taskRequestDTO;
    private TaskResponseDTO taskResponseDTO;
    private TaskUpdateDTO taskUpdateDTO;

    @BeforeEach
    void setUp() {
        taskRequestDTO = new TaskRequestDTO("Test Task", "Description", Status.TODO, Priority.MEDIUM, LocalDate.now());
        taskResponseDTO = new TaskResponseDTO(1L, "Test Task", "Description", Status.TODO, Priority.MEDIUM, LocalDate.now(), 1L);
        taskUpdateDTO = new TaskUpdateDTO(Status.DONE);
    }

    @Test
    void saveTask_ShouldReturnCreated() throws Exception {
        when(taskService.saveTask(anyLong(), any(TaskRequestDTO.class))).thenReturn(taskResponseDTO);

        mockMvc.perform(post("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void getTaskByFilters_ShouldReturnOk() throws Exception {
        when(taskService.getTasksByFilters(any(), any(), any())).thenReturn(Collections.singletonList(taskResponseDTO));

        mockMvc.perform(get("/tasks")
                        .param("status", "TODO")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Task"));
    }

    @Test
    void updateTask_ShouldReturnNoContent() throws Exception {
        doNothing().when(taskService).updateTask(anyLong(), any(TaskUpdateDTO.class));

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskUpdateDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTask_ShouldReturnNoContent() throws Exception {
        doNothing().when(taskService).deleteTask(anyLong());

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }
}
