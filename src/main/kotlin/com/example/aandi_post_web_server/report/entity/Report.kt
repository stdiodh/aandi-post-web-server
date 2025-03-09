package com.example.aandi_post_web_server.report.entity

import com.example.aandi_post_web_server.report.enum.ReportType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "Report")
data class Report (
    @Id
    val id: String? = null,
    val week : Int,
    val title : String,
    val content : String,
    val requirement: List<SeqString>,
    val objects: List<SeqString>,
    val exampleIO: List<ExampleIO>,
    val reportType: ReportType
)