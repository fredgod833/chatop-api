package fr.fredgodard.chatop.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileStorageService {

    private static final Path rootPath = Path.of("/opt/files/oc2024/pictures");

    public void saveFile(MultipartFile file, String subPath) {
        try {
            Path filePath = rootPath.resolve(subPath);
            Files.createDirectories(filePath.getParent());
            file.transferTo(filePath);
        } catch (IOException e) {
            //TODO
            throw new RuntimeException(e);
        }
    }

}
