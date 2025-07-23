package com.example.aandi_post_web_server.member.repository

import com.example.aandi_post_web_server.member.entity.Member
import com.example.aandi_post_web_server.member.enum.MemberRole
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface MemberRepository : ReactiveMongoRepository<Member, String> {
    fun findByUserId(userId: String): Mono<Member>
    fun findByRoleOrderByUserIdDesc(role: MemberRole): Flux<Member>
}