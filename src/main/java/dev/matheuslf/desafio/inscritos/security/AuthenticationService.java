package dev.matheuslf.desafio.inscritos.security;

import dev.matheuslf.desafio.inscritos.dtos.auth.AuthResponseDTO;
import dev.matheuslf.desafio.inscritos.dtos.auth.LoginDTO;
import dev.matheuslf.desafio.inscritos.dtos.auth.RegisterDTO;
import dev.matheuslf.desafio.inscritos.exceptions.DuplicatedRegisterException;
import dev.matheuslf.desafio.inscritos.exceptions.NotFoundException;
import dev.matheuslf.desafio.inscritos.jwt.JwtService;
import dev.matheuslf.desafio.inscritos.model.entities.User;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager,
                                 CustomUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    public AuthResponseDTO register(RegisterDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new DuplicatedRegisterException("Email já cadastrado");
        }

        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.email());

        String token = jwtService.generateToken(userDetails);

        return new AuthResponseDTO(token);
    }

    public AuthResponseDTO login(LoginDTO dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new BadCredentialsException("Senha inválida");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.email());
        String token = jwtService.generateToken(userDetails);

        return new AuthResponseDTO(token);
    }
}
