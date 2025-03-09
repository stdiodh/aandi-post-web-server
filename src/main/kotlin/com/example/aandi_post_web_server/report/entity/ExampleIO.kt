package com.example.aandi_post_web_server.report.entity

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "ExampleIO")
data class ExampleIO(
    val seq: Int,
    val input: String,
    val output: String,
)