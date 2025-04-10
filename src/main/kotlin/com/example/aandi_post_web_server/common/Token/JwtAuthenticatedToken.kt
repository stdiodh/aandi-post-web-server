package com.example.aandi_post_web_server.common.Token

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticatedToken(
    private val userId: String,
    authorities: Collection<GrantedAuthority>
) : AbstractAuthenticationToken(authorities) {
    override fun getCredentials(): Any = ""
    override fun getPrincipal(): Any = userId

    init {
        isAuthenticated = true
    }
}
