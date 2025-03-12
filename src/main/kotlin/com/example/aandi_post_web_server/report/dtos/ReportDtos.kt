package com.example.aandi_post_web_server.report.dtos

import com.example.aandi_post_web_server.report.entity.ExampleIO
import com.example.aandi_post_web_server.report.entity.Report
import com.example.aandi_post_web_server.report.entity.SeqString
import com.example.aandi_post_web_server.report.enum.Level
import com.example.aandi_post_web_server.report.enum.ReportType
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

data class ReportRequestDTO(
    @JsonProperty("week")
    private val _week: Int,

    @JsonProperty("seq")
    private val _seq: Int,

    @JsonProperty("title")
    private val _title: String,

    @JsonProperty("content")
    private val _content: String,

    @JsonProperty("requirement")
    private val _requirement: List<SeqString>,

    @JsonProperty("objects")
    private val _objects: List<SeqString>,

    @JsonProperty("exampleIO")
    private val _exampleIO: List<ExampleIO>,

    @JsonProperty("reportType")
    private val _reportType: String,

    @JsonProperty("startAt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private val _startAt: LocalDateTime,

    @JsonProperty("endAt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private val _endAt: LocalDateTime,

    @JsonProperty("level")
    private val _level: String
) {
    val week: Int
        get() = _week
    val seq: Int
        get() = _seq
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
        get() = ReportType.valueOf(_reportType)

    val startAt: ZonedDateTime
        get() = _startAt.atZone(ZoneId.of("Asia/Seoul")).plusHours(9)

    val endAt: ZonedDateTime
        get() = _endAt.atZone(ZoneId.of("Asia/Seoul")).plusHours(9)

    val level: Level
        get() = Level.valueOf(_level)

    fun toEntity(): Report {
        return Report(
            id = null,
            week = week,
            seq = seq,
            title = title,
            content = content,
            requirement = requirement,
            objects = objects,
            exampleIO = exampleIO,
            reportType = reportType,
            startAt = startAt.toInstant(),
            endAt = endAt.toInstant(),
            level = level
        )
    }
}

data class ReportSummaryDTO(
    val id: String,
    val week: Int,
    val seq: Int,
    val title: String,
    val level: Level,
    val reportType: ReportType,
    val endAt: ZonedDateTime
)

data class ReportDetailDTO(
    val id: String,
    val week: Int,
    val title: String,
    val content: String,
    val requirement: List<SeqString>,
    val objects: List<SeqString>,
    val exampleIo: List<ExampleIO>,
    val reportType: ReportType,
    val endAt: ZonedDateTime,
    val level: Level
)

