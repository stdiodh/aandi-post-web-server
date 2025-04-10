package com.example.aandi_post_web_server.member.service

import com.example.aandi_post_web_server.common.authority.JwtTokenProvider
import com.example.aandi_post_web_server.member.dtos.*
import com.example.aandi_post_web_server.member.entity.Member
import com.example.aandi_post_web_server.member.enum.MemberRole
import com.example.aandi_post_web_server.member.repository.MemberRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class MemberService (
    private val memberRepository: MemberRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder
){
    private fun randomPassword(length: Int = 10): String {
        val char = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length).map {char.random()}.joinToString("")
    }

    fun registerMember(request: RegisterMemberRequest): Mono<MemberResponse> {
        return memberRepository.findByRoleOrderByUserIdDesc(MemberRole.MEMBER)
            .next() // 가장 최근 등록된 MEMBER 1명만 가져옴
            .defaultIfEmpty(Member(userId = "aandiUser00", nickName = "", password = "", role = MemberRole.MEMBER))
            .map { lastMember ->
                val num = lastMember.userId.removePrefix("aandiUser").toInt() + 1
                "aandiUser%02d".format(num)
            }
            .flatMap { newUserId ->
                val rawPassword = randomPassword()
                val encodedPassword = passwordEncoder.encode(rawPassword)
                val member = Member(
                    userId = newUserId,
                    nickName = request.nickname,
                    password = encodedPassword,
                    role = MemberRole.MEMBER
                )
                memberRepository.save(member).map {
                    MemberResponse(
                        userId = it.userId,
                        nickname = it.nickName,
                        role = it.role,
                        rawPassword = rawPassword
                    )
                }
            }
    }


    fun registerAdmin(request: RegisterAdminRequest): Mono<MemberResponse> {
        return memberRepository.findByUserId(request.userId)
            .flatMap<MemberResponse> {
                // 이미 존재하는 경우 예외 발생
                Mono.error(IllegalArgumentException("이미 존재하는 ID입니다."))
            }
            .switchIfEmpty(
                Mono.defer {
                    val encodedPassword = passwordEncoder.encode(request.password)
                    val admin = Member(
                        userId = request.userId,
                        nickName = request.nickname,
                        password = encodedPassword,
                        role = MemberRole.ADMIN
                    )
                    memberRepository.save(admin).map {
                        MemberResponse(
                            userId = it.userId,
                            nickname = it.nickName,
                            role = it.role
                        )
                    }
                }
            )
    }

    fun login(request: LoginRequest): Mono<TokenResponse> {
        return memberRepository.findByUserId(request.userId)
            .switchIfEmpty(Mono.error(IllegalArgumentException("유저를 찾을 수 없습니다")))
            .flatMap { user ->
                if (!passwordEncoder.matches(request.password, user.password)) {
                    Mono.error(IllegalArgumentException("비밀번호가 일치하지 않습니다"))
                } else {
                    val token = jwtTokenProvider.generateToken(user.userId, user.role)
                    Mono.just(TokenResponse(token))
                }
            }
    }

}