package com.example.aandi_post_web_server.report.controller

import com.example.aandi_post_web_server.common.dtos.BaseResponse
import com.example.aandi_post_web_server.report.dtos.ReportRequestDTO
import com.example.aandi_post_web_server.report.entity.Report
import com.example.aandi_post_web_server.report.service.ReportService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Tag(name = "REPORT API", description = "레포트 공지 게시글 API")
@RestController
@RequestMapping("/api/report")
class ReportController(
    private val reportService: ReportService,
) {
    @Operation(summary = "공지 생성", description = "공지를 생성하는 API입니다.")
    @PostMapping
    private suspend fun createReport(@Valid @RequestBody reportRequestDTO: ReportRequestDTO): Mono<BaseResponse<Report>> {
        return reportService.createReport(reportRequestDTO)
            .map { report -> BaseResponse(data = report) }
    }

    @Operation(summary = "ID 별로 조회", description = "ID로 하나의 공지를 가져오는 API입니다.")
    @GetMapping("/{id}")
    private suspend fun getReportById(@Parameter(description = "공지 ID") @PathVariable id: String): Mono<BaseResponse<Report>>{
        return reportService.getOneReport(id).map { report ->
            BaseResponse(data = report)
        }
    }

    @Operation(summary = "전체 조회", description = "전체의 공지를 가져오는 API입니다.")
    @GetMapping
    private suspend fun getAllReport(): BaseResponse<Flux<Report>> {
        val result: Flux<Report> = reportService.getAllReport()
        return BaseResponse(data = result)
    }

    @Operation(summary = "리포트 수정", description = "리포트 ID를 통해 해당 리포트를 수정합니다.")
    @PutMapping("/{id}")
    private suspend fun updateReport(
        @Parameter(description = "레포트 ID") @PathVariable id: String,
        @RequestBody @Valid reportRequestDTO: ReportRequestDTO
    ): Mono<BaseResponse<Report>> {
        return reportService.updateReport(id, reportRequestDTO).map { updateReport ->
            BaseResponse(data = updateReport)
        }
    }

    @Operation(summary = "리포트 삭제", description = "리포트 ID를 통해 해당 레포트를 삭제합니다.")
    @DeleteMapping("/{id}")
    private suspend fun deleteReport(
        @Parameter(description = "레포트 ID") @PathVariable id: String
    ): Mono<BaseResponse<String>> {
        return reportService.deleteReport(id).map {
            BaseResponse(data = "리포트 삭제 완료")
        }
    }
}