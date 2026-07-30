package com.cloudexpense.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

import java.util.List;

/**
 * ClassName: FileProperties
 * Package: com.cloudexpense.config
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/30 22:12
 * @Version: v1.0
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "file")
public class FileProperties {
    private String storagePath;

    private String accessPath;

    private DataSize maxSize;

    private Integer maxCountPerExpense;

    private List<String> allowedTypes;
}
