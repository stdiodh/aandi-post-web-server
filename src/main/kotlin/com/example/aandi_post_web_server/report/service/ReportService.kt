package com.example.aandi_post_web_server.report.service

import com.example.aandi_post_web_server.report.dtos.ReportRequestDTO
import com.example.aandi_post_web_server.report.entity.Report
import com.example.aandi_post_web_server.report.repository.ReportRepository
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ReportService(
    private val reportRepository: ReportRepository,
) {
    //게시글 생성 메소드
    suspend fun createReport(postRequestDTO: ReportRequestDTO): Mono<Report>{
        val post = postRequestDTO.toEntity()
        return reportRepository.save(post)
    }

    //Id로 하나의 게시글을 가져오는 메소드
    suspend fun getOneReport(id: String): Mono<Report>{
        return reportRepository.findById(id)
    }

    //전체 게시글 가져오는 메소드
    suspend fun getAllReport() : Flux<Report>{
        return reportRepository.findAll()
    }

    //Id로 하나의 게시글을 업데이트 하는 메소드
    suspend fun updateReport(id: String, reportRequestDTO: ReportRequestDTO) : Mono<Report>{
        val existingReport = reportRepository.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "리포트를 찾을 수 없습니다: $id")

        val updatedReport = Report(
            id = id,  // 기존 ID 유지
            week = reportRequestDTO.week,
            title = reportRequestDTO.title,
            content = reportRequestDTO.content,
            requirement = reportRequestDTO.requirement,
            objects = reportRequestDTO.objects,
            exampleIO = reportRequestDTO.exampleIO,
            reportType = reportRequestDTO.reportType
        )

        return reportRepository.save(updatedReport)
    }

    //Id로 하나의 게시글을 삭제하는 메소드
    suspend fun deleteReport(id: String): Mono<Void>{
        return reportRepository.deleteById(id)
    }
}