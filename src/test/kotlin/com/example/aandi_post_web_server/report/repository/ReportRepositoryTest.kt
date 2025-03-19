package com.example.aandi_post_web_server.report.repository

import com.example.aandi_post_web_server.common.config.TestMongoConfig
import com.example.aandi_post_web_server.report.entity.*
import com.example.aandi_post_web_server.report.enum.Level
import com.example.aandi_post_web_server.report.enum.ReportType
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.Import
import reactor.test.StepVerifier
import java.time.Instant
import java.time.temporal.ChronoUnit

@DataMongoTest
@Import(TestMongoConfig::class)
class ReportRepositoryTest @Autowired constructor(
    private val reportRepository: ReportRepository
) : StringSpec({

    beforeTest {
        StepVerifier.create(reportRepository.deleteAll()).verifyComplete()
    }

    "Report 저장이 형식에 맞게 된다면 성공한다." {
        val report = Report(
            week = 1,
            seq = 100,
            title = "Test Report",
            content = "This is a test report",
            requirement = listOf(SeqString(1, "Requirement 1"), SeqString(2, "Requirement 2")),
            objects = listOf(SeqString(1, "Object 1"), SeqString(2, "Object 2")),
            exampleIO = listOf(ExampleIO(1, "input1", "output1")),
            reportType = ReportType.CS,
            startAt = Instant.now().minus(1, ChronoUnit.DAYS),
            endAt = Instant.now().plus(1, ChronoUnit.DAYS),
            level = Level.MEDIUM
        )

        StepVerifier.create(reportRepository.save(report))
            .assertNext {
                it.title shouldBe "Test Report"
                it.content shouldBe "This is a test report"
                it.requirement.size shouldBe 2
                it.objects.size shouldBe 2
                it.exampleIO.size shouldBe 1
                it.isAvailable shouldBe true
            }
            .verifyComplete()
    }

    "레포트가 종료되면 isAvailable 값이 false가 된다." {
        val expiredReport = Report(
            week = 2,
            seq = 200,
            title = "Expired Report",
            content = "This report is expired",
            requirement = listOf(),
            objects = listOf(),
            exampleIO = listOf(),
            reportType = ReportType.BASIC,
            startAt = Instant.now().minus(5, ChronoUnit.DAYS),
            endAt = Instant.now().minus(1, ChronoUnit.DAYS),
            level = Level.LOW
        )

        StepVerifier.create(reportRepository.save(expiredReport))
            .assertNext {
                it.isAvailable shouldBe false
            }
            .verifyComplete()
    }

})

