package com.example.WebStore.Service;

import com.example.WebStore.model.Users;
import com.example.WebStore.repository.UserRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ProfileImageService {

    private final UserRepository userRepository;

    public ProfileImageService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String uploadProfileImage(Long userId, MultipartFile file) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado.")
                );

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma imagem foi enviada.");
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                !contentType.matches("image/(jpeg|png|webp)")) {

            throw new IllegalArgumentException(
                    "Formato inválido. Envie uma imagem JPEG, PNG ou WEBP.");
        }

        long maxSize = 5 * 1024 * 1024;

        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException(
                    "A imagem não pode ultrapassar 5 MB.");
        }

        Path uploadDir = Paths.get("uploads", "profile")
                .toAbsolutePath()
                .normalize();

        String extension = switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> throw new IllegalArgumentException("Formato inválido.");
        };

        String fileName = UUID.randomUUID() + extension;

        Path filePath = uploadDir.resolve(fileName);

        try (var inputStream = file.getInputStream()) {

            Files.createDirectories(uploadDir);

            Files.copy(inputStream, filePath);


        } catch (IOException e) {

            throw new RuntimeException(
                    "Erro ao salvar a imagem de perfil.", e
            );
        }

        user.setProfileImage(fileName);

        userRepository.save(user);

        return fileName;
    }

    public Resource getProfileImage(Long userId) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado."));

        String fileName = user.getProfileImage();

        if (fileName == null || fileName.isBlank()) {
            throw new RuntimeException("Usuário ainda não possui foto de perfil.");
        }

        Path uploadDir = Paths.get("uploads", "profile")
                .toAbsolutePath()
                .normalize();

        Path filePath = uploadDir.resolve(fileName).normalize();

        if (!filePath.startsWith(uploadDir)) {
            throw new IllegalArgumentException("Caminho de imagem inválido.");
        }

        try {

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("Imagem não encontrada.");
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Erro ao localizar a imagem.", e);
        }
    }
}
