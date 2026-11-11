package com.example.WebStore.Service;

import com.example.WebStore.DTO.LoginDTO;
import com.example.WebStore.DTO.LoginResponseDTO;
import com.example.WebStore.DTO.UserDTO;
import com.example.WebStore.DTO.UserResponseDTO;
import com.example.WebStore.Mapper.UserMapper;
import com.example.WebStore.model.Users;
import com.example.WebStore.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            JwtEncoder jwtEncoder
    ) {

        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    private String generateToken(Users user) {

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("WebStore")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(7200))
                .subject(String.valueOf(user.getId()))
                .claim("email", user.getEmail())
                .build();

        JwsHeader header = JwsHeader
                .with(MacAlgorithm.HS256)
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue();
    }

    public UserResponseDTO createUser(UserDTO userDTO) {

        if (userRepository.existsByEmail(userDTO.email())) {
            throw new IllegalArgumentException(
                    "Este email já está cadastrado."
            );

        }

        Users user = userMapper.toEntity(userDTO);

        user.setSenha(
                passwordEncoder.encode(userDTO.senha())
        );

        Users savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }

    public UserResponseDTO findUserById(Long id) {

        Users user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado")
                );

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public LoginResponseDTO login(LoginDTO loginDTO) {

        Users user = userRepository.findByEmail(loginDTO.email())
                .orElseThrow(() ->
                        new RuntimeException("Email ou senha inválidos."));

        if (!passwordEncoder.matches(
                loginDTO.senha(),
                user.getSenha()
        )) {
            throw new RuntimeException(("Email ou senha inválidos."));
        }

        String token = generateToken(user);

        UserResponseDTO userResponse = new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );

        return new LoginResponseDTO(token, userResponse);
    }
}
