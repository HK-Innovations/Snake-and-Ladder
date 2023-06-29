package com.ludo.Snake.and.Ladder

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.CorsRegistry


@Configuration
class CORSFilter implements WebMvcConfigurer {

    @Override
    void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*")
                .allowedHeaders("*")
        super.addCorsMappings(registry)
    }
}
