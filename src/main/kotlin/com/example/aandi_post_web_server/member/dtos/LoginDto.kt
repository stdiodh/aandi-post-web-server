package com.example.aandi_post_web_server.member.dtos

import com.example.aandi_post_web_server.member.enum.MemberRole

data class MemberResponse(
    val userId: String,
    val nickname: String,
    val role: MemberRole,
    val rawPassword: String? = null
)

data class LoginRequest(
    val userId: String,
    val password: String
)

data class TokenResponse(
    val accessToken: String
)

data class RegisterMemberRequest(
    val nickname: String
)

data class RegisterAdminRequest(
    val userId: String,
    val nickname: String,
    val password: String
)
