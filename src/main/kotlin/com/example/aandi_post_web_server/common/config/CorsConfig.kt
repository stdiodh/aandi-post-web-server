package com.example.aandi_post_web_server.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class CorsConfig : WebFluxConfigurer {
    @Bean
    override fun addCorsMappings(registry: CorsRegistry) {

        registry.addMapping("/**")
            .allowedOriginPatterns("https://aandi-report-web.firebaseapp.com/*")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowCredentials(true)
    }
}