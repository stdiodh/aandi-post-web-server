package com.example.aandi_post_web_server.member.entity

import com.example.aandi_post_web_server.member.enum.MemberRole
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "member")
class Member (
    @Id
    val id : String? = null,
    val userId : String,
    val password : String,
    val nickName : String,
    val role : MemberRole
)