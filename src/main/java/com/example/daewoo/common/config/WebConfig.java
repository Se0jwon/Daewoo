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
            String hotelLocation = resolveResourceLocation(hotelResourceLocation);
            registry.addResourceHandler(hotelResourceHandler)
                    .addResourceLocations(hotelLocation);
            log.info("Hotel image resource handler registered: {} -> {}", hotelResourceHandler, hotelLocation);
                    
            // 사용자 이미지 리소스 핸들러 등록
            String userLocation = resolveResourceLocation(userResourceLocation);
            registry.addResourceHandler(userResourceHandler)
                    .addResourceLocations(userLocation)
                    .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
            log.info("User image resource handler registered: {} -> {}", userResourceHandler, userLocation);
                    
            // 개발 환경에서의 리소스 핸들러 (선택사항)
            registry.addResourceHandler("/**")
                    .addResourceLocations("classpath:/static/");
                    
        } catch (Exception e) {
            log.error("리소스 핸들러 설정 중 오류 발생", e);
        }
    }
    
    /**
     * 리소스 경로를 Spring이 이해할 수 있는 형태로 변환
     */
    private String resolveResourceLocation(String location) {
        // classpath: 프로토콜이 이미 있으면 그대로 반환
        if (location.startsWith("classpath:")) {
            return location;
        }
        
        // 상대 경로 처리 (./ 또는 .\로 시작)
        if (location.startsWith("./") || location.startsWith(".\\")) {
            String projectRoot = System.getProperty("user.dir");
            String relativePath = location.substring(2).replace("\\", "/");
            String absolutePath = projectRoot.replace("\\", "/") + "/" + relativePath;
            // 끝에 / 추가 (Spring ResourceHandler 요구사항)
            if (!absolutePath.endsWith("/")) {
                absolutePath += "/";
            }
            return "file:" + absolutePath;
        }
        
        // 절대 경로 처리
        String path = location.replace("\\", "/");
        if (!path.endsWith("/")) {
            path += "/";
        }
        return "file:" + path;
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