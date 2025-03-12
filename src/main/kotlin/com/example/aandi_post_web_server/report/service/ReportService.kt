package com.example.aandi_post_web_server.report.service

import com.example.aandi_post_web_server.report.dtos.ReportRequestDTO
import com.example.aandi_post_web_server.report.dtos.ReportSummaryDTO
import com.example.aandi_post_web_server.report.entity.Report
import com.example.aandi_post_web_server.report.repository.ReportRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant
import java.time.ZoneId

@Service
class ReportService(
    private val reportRepository: ReportRepository,
) {
    private fun getSeoulTimePlus9Hours(): Instant {
        val now = Instant.now()
        val seoulTime = now.atZone(ZoneId.of("Asia/Seoul"))
        return seoulTime.plusHours(9).toInstant()
    }

    suspend fun createReport(reportRequestDTO: ReportRequestDTO): Mono<Report> {
        val report = Report(
            week = reportRequestDTO.week,
            seq = reportRequestDTO.seq,
            title = reportRequestDTO.title,
            content = reportRequestDTO.content,
            requirement = reportRequestDTO.requirement,
            objects = reportRequestDTO.objects,
            exampleIO = reportRequestDTO.exampleIO,
            reportType = reportRequestDTO.reportType,
            availableAt = reportRequestDTO.availableAt.toInstant(),
            expirationAt = reportRequestDTO.expirationAt.toInstant(),
            level = reportRequestDTO.level
        )
        return reportRepository.save(report)
    }

    // 특정 ID의 Report 조회 (공개 시간이 지나야 조회 가능)
    suspend fun getOneReport(id: String): Mono<Report> {
        val now = getSeoulTimePlus9Hours()

        return reportRepository.findById(id)
            .flatMap { report ->
                if (report.availableAt.isAfter(now)) {
                    Mono.error(ResponseStatusException(HttpStatus.FORBIDDEN, "이 리포트는 ${report.availableAt} 이후에 조회 가능합니다."))
                } else if (report.expirationAt.isBefore(now)) {
                    Mono.error(ResponseStatusException(HttpStatus.FORBIDDEN, "이 리포트는 종료된 상태입니다."))
                } else {
                    Mono.just(report)
                }
            }
    }

    // 설정한 시간에 공개된 Report만 조회
    suspend fun getAvailableReports(): Flux<Report> {
        val now = getSeoulTimePlus9Hours()
        return reportRepository.findAll()
            .filter { it.availableAt.isBefore(now) && it.expirationAt.isAfter(now) }
    }

    // 종료된 시간이 지난 Report만 조회
    suspend fun getExpiredReports(): Flux<Report> {
        val now = getSeoulTimePlus9Hours()
        return reportRepository.findAll()
            .filter { it.expirationAt.isBefore(now) }
    }

    // 모든 Report 조회 (시간 제한 없이)
    suspend fun getAllReports(): Flux<Report> {
        return reportRepository.findAll()
    }

    // Report 업데이트
    suspend fun updateReport(id: String, reportRequestDTO: ReportRequestDTO): Mono<Report> {
        return reportRepository.findById(id)
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND, "리포트를 찾을 수 없습니다: $id")))
            .flatMap { existingReport ->
                val updatedReport = Report(
                    id = id,
                    week = reportRequestDTO.week,
                    seq = reportRequestDTO.seq,
                    title = reportRequestDTO.title,
                    content = reportRequestDTO.content,
                    requirement = reportRequestDTO.requirement,
                    objects = reportRequestDTO.objects,
                    exampleIO = reportRequestDTO.exampleIO,
                    reportType = reportRequestDTO.reportType,
                    availableAt = reportRequestDTO.availableAt.toInstant(),
                    expirationAt = reportRequestDTO.expirationAt.toInstant(),
                    level = reportRequestDTO.level
                )
                reportRepository.save(updatedReport)
            }
    }

    // Report 삭제
    suspend fun deleteReport(id: String): Mono<String> {
        return reportRepository.findById(id)
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND, "리포트를 찾을 수 없습니다: $id")))
            .flatMap { report ->
                reportRepository.delete(report).thenReturn("리포트 삭제 완료")
            }
    }

    // 전체 Report에서 필요한 필드(id, week, title, level)만 가져오기
    suspend fun getAllReportSummaries(): Flux<ReportSummaryDTO> {
        return reportRepository.findAll()
            .map { report ->
                ReportSummaryDTO(
                    id = report.id ?: "",
                    week = report.week,
                    title = report.title,
                    level = report.level
                )
            }
    }
}
