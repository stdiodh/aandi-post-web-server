package com.example.aandi_post_web_server.common.authority

import com.example.aandi_post_web_server.member.enum.MemberRole
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider{
    private val secret = "aandi-super-secret-key-aandi-super-secret-key"
    val secretKey: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray())
    private val expiration = 1000 * 60 * 60

    fun generateToken(userId: String, roles: MemberRole): String {
        val now = Date()
        return Jwts.builder()
            .subject(userId)
            .claim("roles", listOf("ROLE_${roles.name}"))
            .issuedAt(now)
            .expiration(Date(now.time + expiration))
            .signWith(secretKey)
            .compact()
    }

    fun getUserIdFromToken(token: String): String {
        val claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload

        return claims.subject
    }
}
