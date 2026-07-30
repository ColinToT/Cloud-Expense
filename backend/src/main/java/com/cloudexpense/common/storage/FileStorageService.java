package com.cloudexpense.common.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * ClassName: FileStorageService
 * Package: com.cloudexpense.common.storage
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/30 21:11
 * @Version: v1.0
 */
public interface FileStorageService {

    String store(MultipartFile file);

    void delete(String fileUrl);
}
