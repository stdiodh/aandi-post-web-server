package com.example.aandi_post_web_server.common.authority

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider {

    private val secret = "aandi-super-secret-key-aandi-super-secret-key" // 최소 32바이트 이상
    private val secretKey: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray())
    private val expiration = 1000 * 60 * 60 // 1시간

    fun generateToken(userId: String): String {
        val now = Date()

        return Jwts.builder()
            .subject(userId) // 최신 방식: claim("sub", userId) 대신 subject()
            .issuedAt(now)
            .expiration(Date(now.time + expiration))
            .signWith(secretKey)
            .compact()
    }
}
