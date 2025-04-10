package com.example.aandi_post_web_server.member.controller

import com.example.aandi_post_web_server.member.dtos.*
import com.example.aandi_post_web_server.member.service.MemberService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@Tag(name = "MEMBER API", description = "A&I 멤버 관리 API입니다.")
@RestController
@RequestMapping("/api/member")
class UserController(
    private val memberService: MemberService
) {
    @Operation(summary = "멤버 생성 API", description = "A&I에 접근하는 멤버를 생성하는 API입니다.")
    @PostMapping("/register/member")
    fun registerMember(@RequestBody request: RegisterMemberRequest): Mono<MemberResponse> {
        return memberService.registerMember(request)
    }

    @Operation(summary = "관리자 생성 API", description = "A&I에 접근하는 멤버를 생성하는 API입니다.")
    @PostMapping("/register/admin")
    fun registerAdmin(@RequestBody request: RegisterAdminRequest): Mono<MemberResponse> {
        return memberService.registerAdmin(request)
    }

    @Operation(summary = "로그인 API", description = "A&I에 접근하는 멤버의 아이디와 패스워드가 맞다면 토큰을 내보내는 API입니다.")
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): Mono<TokenResponse> {
        return memberService.login(request)
    }
}
