package com.example.aandi_post_web_server.report.repository

import com.example.aandi_post_web_server.report.entity.ExampleIO
import com.example.aandi_post_web_server.report.entity.Report
import com.example.aandi_post_web_server.report.entity.SeqString
import com.example.aandi_post_web_server.report.enum.Level
import com.example.aandi_post_web_server.report.enum.ReportType
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import reactor.test.StepVerifier
import java.time.Instant
import java.time.temporal.ChronoUnit

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReportRepositoryTest(
    private val reportRepository: ReportRepository
) : StringSpec({

    beforeSpec {
        StepVerifier.create(reportRepository.deleteAll()).verifyComplete()
    }

    "Report 저장이 성공적으로 수행된다" {
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
                it.isAvailable shouldBe true
            }
            .verifyComplete()
    }

    "종료된 레포트는 isAvailable이 false가 되어야 한다" {
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

}) {
    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun configureMongoDbProperties(registry: DynamicPropertyRegistry) {
            val mongoUri = System.getenv("MONGO_DB_URL") ?: "mongodb://localhost:27017/aandi_test_db"
            registry.add("spring.data.mongodb.uri") { mongoUri }
        }
    }
}
