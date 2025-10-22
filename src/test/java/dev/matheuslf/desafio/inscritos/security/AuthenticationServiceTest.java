package dev.matheuslf.desafio.inscritos.security;

import dev.matheuslf.desafio.inscritos.dtos.auth.AuthResponseDTO;
import dev.matheuslf.desafio.inscritos.dtos.auth.LoginDTO;
import dev.matheuslf.desafio.inscritos.dtos.auth.RegisterDTO;
import dev.matheuslf.desafio.inscritos.exceptions.DuplicatedRegisterException;
import dev.matheuslf.desafio.inscritos.exceptions.NotFoundException;
import dev.matheuslf.desafio.inscritos.jwt.JwtService;
import dev.matheuslf.desafio.inscritos.model.entities.User;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User user;
    private RegisterDTO registerDTO;
    private LoginDTO loginDTO;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        registerDTO = new RegisterDTO("Test User", "test@example.com", "password");
        loginDTO = new LoginDTO("test@example.com", "password");

        userDetails = org.springframework.security.core.userdetails.User
                .withUsername("test@example.com")
                .password("encodedPassword")
                .authorities(Collections.emptyList())
                .build();
    }

    @Test
    void register_Success() {
        when(userRepository.existsByEmail(registerDTO.email())).thenReturn(false);
        when(passwordEncoder.encode(registerDTO.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userDetailsService.loadUserByUsername(registerDTO.email())).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("test-token");

        AuthResponseDTO result = authenticationService.register(registerDTO);

        assertNotNull(result);
        assertEquals("test-token", result.token());
        verify(userRepository).existsByEmail(registerDTO.email());
        verify(passwordEncoder).encode(registerDTO.password());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_EmailAlreadyExists() {
        when(userRepository.existsByEmail(registerDTO.email())).thenReturn(true);

        assertThrows(DuplicatedRegisterException.class, () -> authenticationService.register(registerDTO));

        verify(userRepository).existsByEmail(registerDTO.email());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_Success() {
        when(userRepository.findByEmail(loginDTO.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDTO.password(), user.getPassword())).thenReturn(true);
        when(userDetailsService.loadUserByUsername(loginDTO.email())).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("test-token");

        AuthResponseDTO result = authenticationService.login(loginDTO);

        assertNotNull(result);
        assertEquals("test-token", result.token());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void login_UserNotFound() {
        when(userRepository.findByEmail(loginDTO.email())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authenticationService.login(loginDTO));

        verify(userRepository).findByEmail(loginDTO.email());
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void login_InvalidPassword() {
        when(userRepository.findByEmail(loginDTO.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDTO.password(), user.getPassword())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authenticationService.login(loginDTO));

        verify(userRepository).findByEmail(loginDTO.email());
        verify(passwordEncoder).matches(loginDTO.password(), user.getPassword());
        verify(authenticationManager, never()).authenticate(any());
    }
}
