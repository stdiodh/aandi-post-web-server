package com.example.aandi_post_web_server.post.repository

import com.example.aandi_post_web_server.post.entity.Post
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface PostRepository : ReactiveMongoRepository<Post, String>