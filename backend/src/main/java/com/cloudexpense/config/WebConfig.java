package com.cloudexpense.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ClassName: WebConfig
 * Package: com.cloudexpense.config
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/30 21:24
 * @Version: v1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.access-path}")
    private String accessPath;

    @Value("${file.storage-path}")
    private String storagePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){

        registry
                .addResourceHandler(accessPath + "/**")
                .addResourceLocations(
                        "file:" + storagePath + "/"
                );
    }
}
