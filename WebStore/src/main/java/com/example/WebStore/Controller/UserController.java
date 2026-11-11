package com.example.WebStore.Controller;

import com.example.WebStore.DTO.LoginDTO;
import com.example.WebStore.DTO.LoginResponseDTO;
import com.example.WebStore.DTO.UserDTO;
import com.example.WebStore.DTO.UserResponseDTO;
import com.example.WebStore.Service.ProfileImageService;
import com.example.WebStore.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ProfileImageService profileImageService;

    public UserController(
            UserService userService,
            ProfileImageService profileImageService
    ) {
        this.userService = userService;
        this.profileImageService = profileImageService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserDTO userDTO) {

        UserResponseDTO response = userService.createUser(userDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginDTO loginDTO)
    {
        LoginResponseDTO responseDTO
                = userService.login(loginDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping(
            value = "/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> uploadProfileImage(
            @AuthenticationPrincipal Jwt jwt,
            @RequestPart("file") MultipartFile file
    ) {
        Long userId = Long.valueOf(jwt.getSubject());

        String fileName = profileImageService.uploadProfileImage(
                userId,
                file
        );

        return ResponseEntity.ok(fileName);
    }

    @GetMapping("/{id}")
    public ResponseEntity findUserById(
            @PathVariable Long id) {

        UserResponseDTO user = userService.findUserById(id);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/profile-image")
    public ResponseEntity<Resource> getProfileImage(
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long userId = Long.valueOf(jwt.getSubject());

        Resource image = profileImageService.getProfileImage(userId);

        MediaType mediaType = MediaTypeFactory
                .getMediaType(image)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(image);
    }
}
