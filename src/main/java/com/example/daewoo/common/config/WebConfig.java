package com.example.daewoo.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // yml의 file 그룹 밑에 있는 resource-handler 값을 가져옴
    @Value("${file.resource-handler}")
    private String resourceHandler; // "/images/**" 값이 주입됨

    // yml의 file 그룹 밑에 있는 upload-dir 값을 가져옴
    @Value("${file.upload-dir}")
    private String resourceLocation; // "D:/image/" 값이 주입됨

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourceHandler)
                .addResourceLocations("file:///" + resourceLocation);
    }
}