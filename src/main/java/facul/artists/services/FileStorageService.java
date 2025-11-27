package facul.artists.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final String uploadDir = "uploads/";
    private final Path root = Paths.get("uploads");

    public String saveImage(MultipartFile file) throws IOException {
        validateImage(file);
        return saveFile(file);
    }

    public String saveMedia(MultipartFile file) throws IOException {
        validateMedia(file);
        return saveFile(file);
    }

    private String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("Arquivo vazio.");
        }

        File directory = new File(uploadDir);
        if (!directory.exists())
            directory.mkdirs();

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir + fileName);

        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    private void validateImage(MultipartFile file) {
        String type = file.getContentType();
        if (type == null || !type.startsWith("image/")) {
            throw new RuntimeException("Apenas imagens são permitidas.");
        }
    }

    private void validateMedia(MultipartFile file) {
        String type = file.getContentType();
        if (type == null ||
                !(type.startsWith("image/") || type.startsWith("video/"))) {
            throw new RuntimeException("Apenas imagens ou vídeos são permitidos.");
        }
    }

    public void deleteFile(String filePath) {
        if (filePath == null || filePath.isBlank())
            return;
        try {
            Path fileName = Paths.get(filePath).getFileName();
            Path path = root.resolve(fileName.toString());
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
