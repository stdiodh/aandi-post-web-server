package com.example.aandi_post_web_server.post.service

import com.example.aandi_post_web_server.post.dtos.PostRequestDTO
import com.example.aandi_post_web_server.post.entity.Post
import com.example.aandi_post_web_server.post.repository.PostRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class PostService(
    private val postRepository: PostRepository,
) {
    //게시글 생성 메소드
    suspend fun createPost(postRequestDTO: PostRequestDTO): Mono<Post>{
        val post = postRequestDTO.toEntity()
        return postRepository.save(post)
    }

    //Id로 하나의 게시글을 가져오는 메소드
    suspend fun getOnePost(id: String): Mono<Post>{
        return postRepository.findById(id)
    }

    //전체 게시글 가져오는 메소드
    suspend fun getAllPost() : Flux<Post>{
        return postRepository.findAll()
    }
}