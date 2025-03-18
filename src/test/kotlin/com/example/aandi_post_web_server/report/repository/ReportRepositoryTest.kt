package com.example.aandi_post_web_server.report.repository

import com.example.aandi_post_web_server.report.entity.*
import com.example.aandi_post_web_server.report.enum.Level
import com.example.aandi_post_web_server.report.enum.ReportType
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import reactor.test.StepVerifier
import java.time.Instant
import java.time.temporal.ChronoUnit

@DataMongoTest
class ReportRepositoryTest @Autowired constructor(
    private val reportRepository: ReportRepository
) : StringSpec({

    // 테스트 실행 전 데이터 삭제
    beforeTest {
        reportRepository.deleteAll().block()
    }

    "Report 저장이 형식에 맞게 된다면 성공한다." {
        // given
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

        // when
        val savedReport = reportRepository.save(report)

        // then - StepVerifier 활용
        StepVerifier.create(savedReport)
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
        // given - 현재 시간보다 과거에 종료된 Report
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

        // when - 저장 후 조회
        val savedReport = reportRepository.save(expiredReport)

        // then - StepVerifier 활용
        StepVerifier.create(savedReport)
            .assertNext {
                it.isAvailable shouldBe false // 종료된 report는 isAvailable이 false
            }
            .verifyComplete()
    }

})
