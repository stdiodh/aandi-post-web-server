package com.example.aandi_post_web_server.report.service

import com.example.aandi_post_web_server.report.dtos.ReportRequestDTO
import com.example.aandi_post_web_server.report.entity.Report
import com.example.aandi_post_web_server.report.repository.ReportRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ReportService(
    private val reportRepository: ReportRepository,
) {
    //게시글 생성 메소드
    suspend fun createPost(postRequestDTO: ReportRequestDTO): Mono<Report>{
        val post = postRequestDTO.toEntity()
        return reportRepository.save(post)
    }

    //Id로 하나의 게시글을 가져오는 메소드
    suspend fun getOnePost(id: String): Mono<Report>{
        return reportRepository.findById(id)
    }

    //전체 게시글 가져오는 메소드
    suspend fun getAllPost() : Flux<Report>{
        return reportRepository.findAll()
    }
}