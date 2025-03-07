package com.example.aandi_post_web_server.post.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "Post")
data class Post (
    @Id
    val id: String? = null,
    val name : String,
    val title : String,
    val content : String,
)