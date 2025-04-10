package com.example.aandi_post_web_server.common.config

import com.example.aandi_post_web_server.common.Token.JwtAuthenticatedToken
import com.example.aandi_post_web_server.common.Token.JwtAuthenticationToken
import com.example.aandi_post_web_server.common.authority.JwtTokenProvider
import io.jsonwebtoken.Jwts
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import reactor.core.publisher.Mono

@Configuration
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .authorizeExchange {
                // 공개 API
                it.pathMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/webjars/**",
                    "/api/member/login"
                ).permitAll()

                // 관리자만 가능
                it.pathMatchers(HttpMethod.POST, "/api/report").hasRole("ADMIN")
                it.pathMatchers(HttpMethod.PUT, "/api/report/{id}").hasRole("ADMIN")
                it.pathMatchers(HttpMethod.DELETE, "/api/report/{id}").hasRole("ADMIN")

                it.pathMatchers(HttpMethod.POST, "/api/member/register/member").hasRole("ADMIN")
                it.pathMatchers(HttpMethod.GET, "/api/member").hasRole("ADMIN")
                it.pathMatchers(HttpMethod.DELETE, "/api/member/{userId}").hasRole("ADMIN")
                // 나머지 report는 인증만 필요
                it.pathMatchers("/api/report/**").authenticated()

                // 그 외 모두 허용
                it.anyExchange().permitAll()
            }
            .addFilterAt(bearerAuthFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
            .build()
    }

    private fun bearerAuthFilter(): AuthenticationWebFilter {
        val filter = AuthenticationWebFilter(jwtAuthenticationManager())
        filter.setServerAuthenticationConverter(bearerConverter())
        filter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        filter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/api/report/**",
            "/api/member/register/member",
            "/api/member",
            "/api/member/{userId}"))
        return filter
    }

    private fun bearerConverter(): ServerAuthenticationConverter {
        return ServerAuthenticationConverter { exchange ->
            val authHeader = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                val token = authHeader.substring(7)
                Mono.just(JwtAuthenticationToken(token))
            } else {
                Mono.empty()
            }
        }
    }

    private fun jwtAuthenticationManager(): ReactiveAuthenticationManager {
        return ReactiveAuthenticationManager { auth ->
            val jwtToken = (auth as JwtAuthenticationToken).token

            val claims = Jwts.parser()
                .verifyWith(jwtTokenProvider.secretKey)
                .build()
                .parseSignedClaims(jwtToken)
                .payload

            val userId = claims.subject
            val roles = claims["roles"] as? List<*> ?: emptyList<Any>()

            val authorities = roles.mapNotNull {
                (it as? String)?.let { role -> SimpleGrantedAuthority(role) }
            }

            Mono.just(JwtAuthenticatedToken(userId, authorities))
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return object : PasswordEncoder {
            override fun encode(rawPassword: CharSequence): String = rawPassword.toString()
            override fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean =
                rawPassword.toString() == encodedPassword
        }
    }
}
