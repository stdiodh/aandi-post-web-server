package com.example.aandi_post_web_server.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .csrf { it.disable() }
            .authorizeExchange {
                it.pathMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/webjars/**"
                ).permitAll()
                    .anyExchange().permitAll() // 모든 API도 인증 없이 열려 있음 (필요시 수정)
            }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return object : PasswordEncoder {
            override fun encode(rawPassword: CharSequence): String {
                return rawPassword.toString() // 그냥 문자열 그대로 저장
            }

            override fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean {
                return rawPassword.toString() == encodedPassword
            }
        }
    }
}