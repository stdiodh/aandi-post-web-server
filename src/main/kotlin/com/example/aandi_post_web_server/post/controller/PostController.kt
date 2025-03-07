package com.example.aandi_post_web_server.post.controller

import com.example.aandi_post_web_server.common.dtos.BaseResponse
import com.example.aandi_post_web_server.post.dtos.PostRequestDTO
import com.example.aandi_post_web_server.post.entity.Post
import com.example.aandi_post_web_server.post.service.PostService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Tag(name = "Post API", description = "공지 게시글 API")
@RestController
@RequestMapping("/api/post")
class PostController(
    private val postService: PostService,
) {
    @Operation(summary = "공지 생성", description = "공지를 생성하는 API입니다.")
    @PostMapping
    private suspend fun createPost(@Valid @RequestBody postRequestDTO: PostRequestDTO): Mono<BaseResponse<Post>> {
        return postService.createPost(postRequestDTO)
            .map { post -> BaseResponse(data = post) }
    }

    @Operation(summary = "ID 별로 조회", description = "ID로 하나의 공지를 가져오는 API입니다.")
    @GetMapping("/{id}")
    private suspend fun getPostById(@Parameter(description = "공지 ID") @PathVariable id: String): Mono<BaseResponse<Post>>{
        return postService.getOnePost(id).map { post ->
            BaseResponse(data = post)
        }
    }

    @Operation(summary = "전체조회", description = "전체의 공지를 가져오는 API입니다.")
    @GetMapping
    private suspend fun getAllPost() : Flux<BaseResponse<Post>> {
        return postService.getAllPost()
            .map { post -> BaseResponse(data = post) }
    }
}