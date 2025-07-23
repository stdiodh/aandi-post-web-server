package com.example.aandi_post_web_server.common.Token

import org.springframework.security.authentication.AbstractAuthenticationToken

class JwtAuthenticationToken(val token: String) : AbstractAuthenticationToken(null) {
    override fun getCredentials(): Any = token
    override fun getPrincipal(): Any = token
}