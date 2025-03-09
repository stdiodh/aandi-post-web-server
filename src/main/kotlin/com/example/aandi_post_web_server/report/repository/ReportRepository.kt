package com.example.aandi_post_web_server.report.repository

import com.example.aandi_post_web_server.report.entity.Report
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface ReportRepository : ReactiveMongoRepository<Report, String>