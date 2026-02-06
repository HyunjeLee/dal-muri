package com.dalmuri.dalmuri.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dalmuri.dalmuri.data.local.AppDatabase
import com.dalmuri.dalmuri.data.local.entity.MonthlyReportEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MonthlyReportDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var monthlyReportDao: MonthlyReportDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        monthlyReportDao = db.monthlyReportDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetMonthlyReport() =
        runBlocking {
            val report =
                MonthlyReportEntity(
                    yearMonth = "2025-02",
                    summaryComment = "Great month!",
                    learningSummary =
                        listOf(
                            "Room",
                            "Hilt",
                            "Jetpack Compose",
                            "Kotlinx Serialization",
                        ),
                    growthPoints =
                        listOf(
                            "안정적인 데이터 영속성 계층을 설계하기 위해 Room 라이브러리의 깊은 이해도를 확보함",
                            "Dagger Hilt를 활용하여 의존성 주입 구조를 개선하고 코드의 테스트 가능성을 높임",
                            "선언형 UI 프레임워크인 Jetpack Compose를 활용한 현대적인 앱 디자인 역량 강화",
                            "Reflection 대신 코드가 생성되는 방식인 Kotlinx Serialization을 도입하여 데이터 파싱 성능 최적화 달성",
                            "데이터베이스 인덱싱 설정을 통해 대량의 데이터 정렬 및 조회 성능을 고려한 설계 패턴 습득",
                        ),
                    nextMonthAdvice = "다음 달에는 이번에 구축한 DB 구조를 바탕으로 실시간 데이터 흐름(Flow) 처리를 심화 학습해보는 것을 추천합니다.",
                    createdAt = System.currentTimeMillis(),
                )

            monthlyReportDao.insertMonthlyReview(report)

            val loaded = monthlyReportDao.getReviewByMonth("2025-02")
            assertNotNull(loaded)
            assertEquals(report, loaded)

            assertEquals(report.yearMonth, loaded?.yearMonth)
            assertEquals(report.summaryComment, loaded?.summaryComment)
            assertEquals(report.learningSummary, loaded?.learningSummary)
            assertEquals(report.growthPoints, loaded?.growthPoints)
            assertEquals(report.nextMonthAdvice, loaded?.nextMonthAdvice)
            assertEquals(report.createdAt, loaded?.createdAt)
        }
}
