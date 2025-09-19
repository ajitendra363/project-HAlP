package com.MinorProject.VideoStreamingHALP.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Directory on disk where uploaded video files are stored
    // e.g. application.properties: video.upload.dir=uploads/videos
    @Value("${video.upload.dir:uploads/videos}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map /videos/** to physical folder specified by video.upload.dir
        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();

        registry.addResourceHandler("/videos/**")
                .addResourceLocations(dir.toUri().toString())
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());

        // Note: Spring Boot already serves classpath:/static/** automatically.
        // No need to re-map static unless customizing paths.
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Open CORS globally; tighten as needed for security
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Content-Disposition");
    }
}
