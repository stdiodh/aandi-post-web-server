package com.example.aandi_post_web_server.report.entity

import com.example.aandi_post_web_server.report.enum.Level
import com.example.aandi_post_web_server.report.enum.ReportType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.time.ZoneId

@Document(collection = "Report")
data class Report(
    @Id
    val id: String? = null,
    val week: Int,
    val seq: Int,
    val title: String,
    val content: String,
    val requirement: List<SeqString>,
    val objects: List<SeqString>,
    val exampleIO: List<ExampleIO>,
    val reportType: ReportType,
    val startAt: Instant,
    val endAt : Instant,
    val level: Level
) {
    val isAvailable: Boolean
        get() {
            val now = Instant.now().atZone(ZoneId.of("UTC"))
            return now.isAfter(startAt.atZone(ZoneId.of("UTC"))) && now.isBefore(endAt.atZone(ZoneId.of("UTC")))
        }
}