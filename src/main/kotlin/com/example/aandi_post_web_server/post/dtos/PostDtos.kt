package com.example.aandi_post_web_server.post.dtos

import com.example.aandi_post_web_server.post.entity.Post
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotEmpty

data class PostRequestDTO (
    @field:NotEmpty(message = "이름을 입럭해주세요")
    @JsonProperty("name")
    private val _name: String,

    @field:NotEmpty(message = "제목을 입럭해주세요")
    @JsonProperty("title")
    private val _title: String,

    @field:NotEmpty(message = "내용을 입럭해주세요")
    @JsonProperty("content")
    private val _content: String,
) {
    val name: String
        get() = _name!!
    val title: String
        get() = _title!!
    val content: String
        get() = _content!!

    fun toEntity(): Post {
        return Post(
            id = null,
            name = name,
            title = title,
            content = content
        )
    }
}

data class PostResponseDTO(
    val id: String?,
    val name: String,
    val title: String,
    val content: String,
)