package com.example.daewoo.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 호텔 이미지 리소스 핸들러
    @Value("${file.upload.hotel.resourceHandler}")
    private String hotelResourceHandler;
    
    @Value("${file.upload.hotel.path}")
    private String hotelResourceLocation;
    
    // 사용자 이미지 리소스 핸들러
    @Value("${file.upload.user.resourceHandler}")
    private String userResourceHandler;
    
    @Value("${file.upload.user.path}")
    private String userResourceLocation;
    
    @Value("${file.upload.user.webPath}")
    private String userWebPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {
            // 호텔 이미지 리소스 핸들러 등록
            registry.addResourceHandler(hotelResourceHandler)
                    .addResourceLocations(hotelResourceLocation);
                    
            // 사용자 이미지 리소스 핸들러 등록 (classpath:static/userimage/)
            registry.addResourceHandler(userResourceHandler)
                    .addResourceLocations("classpath:static/userimage/", "file:./src/main/resources/static/userimage/")
                    .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
                    
            // 개발 환경에서의 리소스 핸들러 (선택사항)
            registry.addResourceHandler("/**")
                    .addResourceLocations("classpath:/static/");
                    
        } catch (Exception e) {
            log.error("리소스 핸들러 설정 중 오류 발생", e);
        }
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


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }

}