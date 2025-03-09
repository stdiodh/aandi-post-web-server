package com.example.aandi_post_web_server.report.dtos

import com.example.aandi_post_web_server.common.annotation.ValidEnum
import com.example.aandi_post_web_server.report.entity.ExampleIO
import com.example.aandi_post_web_server.report.entity.Report
import com.example.aandi_post_web_server.report.entity.SeqString
import com.example.aandi_post_web_server.report.enum.ReportType
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotEmpty

data class ReportRequestDTO (
    @field:NotEmpty(message = "주차를 입럭해주세요.")
    @JsonProperty("week")
    private val _week: Int,

    @field:NotEmpty(message = "제목을 입럭해주세요.")
    @JsonProperty("title")
    private val _title: String,

    @field:NotEmpty(message = "내용을 입럭해주세요.")
    @JsonProperty("content")
    private val _content: String,

    @field:NotEmpty(message = "요구 사항을 입력해주세요.")
    @JsonProperty("requirement")
    private val _requirement: List<SeqString>,

    @field:NotEmpty(message = "학습 목적을 입력해주세요.")
    @JsonProperty("objects")
    private val _objects: List<SeqString>,

    @field:NotEmpty(message = "예시 I/O을 입력해주세요.")
    @JsonProperty("exampleIO")
    private val _exampleIO: List<ExampleIO>,

    @field:NotEmpty(message = "리포트 타입을 입력해주세요.")
    @field:ValidEnum(enumClass = ReportType::class, message = "잘못된 리포트 타입입니다.")
    @JsonProperty("reportType")
    private val _reportType: String
) {
    val week: Int
        get() = _week
    val title: String
        get() = _title
    val content: String
        get() = _content
    val requirement: List<SeqString>
        get() = _requirement
    val objects: List<SeqString>
        get() = _objects
    val exampleIO: List<ExampleIO>
        get() = _exampleIO
    val reportType: ReportType
        get() = ReportType.valueOf(_reportType!!)


    fun toEntity(): Report {
        return Report(
            id = null,
            week = week,
            title = title,
            content = content,
            requirement = requirement,
            objects = objects,
            exampleIO = exampleIO,
            reportType = reportType
        )
    }
}

data class ReportResponseDTO(
    val id: String?,
    val week: Int,
    val title: String,
    val content: String,
    val requirement: List<SeqString>,
    val objects: List<SeqString>,
    val exampleIO: List<ExampleIO>,
    val reportType: ReportType
)