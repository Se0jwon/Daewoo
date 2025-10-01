package com.example.daewoo.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
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

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // /signup/additional-info 요청을 index.html로 명확하게 포워딩합니다.
        registry.addViewController("/signup/additional-info")
                .setViewName("forward:/index.html");

        // 그 외 Vue History Mode 지원을 위한 규칙 (API 경로는 자동 제외됨)
        registry.addViewController("/{spring:\\w+}")
                .setViewName("forward:/index.html");
        registry.addViewController("/{spring:\\w+}/**")
                .setViewName("forward:/index.html");
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("*")
//                .allowedOrigins("*")
//                ;
//    }
}