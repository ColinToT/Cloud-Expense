package com.cloudexpense.common.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * ClassName: LocalFileStorageService
 * Package: com.cloudexpense.common.storage
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/30 21:14
 * @Version: v1.0
 */
@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path storagePath;
    private final String accessPath;

    public LocalFileStorageService(
            @Value("${file.storage-path}") String storagePath,
            @Value("${file.access-path}") String accessPath
    ) {
        this.storagePath = Paths.get(storagePath )
                        .toAbsolutePath()
                        .normalize();
        this.accessPath = accessPath;

        try {
            Files.createDirectories(this.storagePath);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not create upload directory",
                    e
            );
        }
    }

    @Override
    public String store(MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            String filename = UUID.randomUUID() + "-" + originalName;
            Path target = storagePath.resolve(filename);

            Files.copy(
                    file.getInputStream(),
                    target,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return accessPath + "/" + filename;

        } catch (IOException e) {
            throw new RuntimeException(
                    "File upload failed",
                    e
            );
        }
    }

    @Override
    public void delete(String fileUrl) {
        try {
            String filename =
                    Paths.get(fileUrl)
                            .getFileName()
                            .toString();

            Files.deleteIfExists(storagePath.resolve(filename));

        } catch (IOException e) {
            throw new RuntimeException(
                    "File delete failed",
                    e
            );
        }
    }
}
