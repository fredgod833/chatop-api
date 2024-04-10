package fr.fredgodard.chatop.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileStorageService {

    private final Environment env;

    private final Path rootPath;

    public FileStorageService(Environment env) throws URISyntaxException {
        this.env = env;
        URI rootUri = new URI(env.getProperty("spring.web.resources.static-locations"));
        Path p = Path.of(rootUri);
        this.rootPath = p.resolve("pictures");
    }

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
