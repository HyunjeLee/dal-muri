package com.dalmuri.dalmuri.data.remote.datasource

import com.dalmuri.dalmuri.data.remote.api.OpenAiApi
import com.dalmuri.dalmuri.data.remote.model.ChartSummaryDto
import com.dalmuri.dalmuri.data.remote.model.JsonSchemaFormat
import com.dalmuri.dalmuri.data.remote.model.OpenAiRequestDto
import com.dalmuri.dalmuri.data.remote.model.OpenAiResponseDto
import com.dalmuri.dalmuri.data.remote.model.ResponseTextConfig
import com.dalmuri.dalmuri.domain.model.Emotion
import com.dalmuri.dalmuri.presentation.utils.emoji
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import java.util.Locale
import javax.inject.Inject

class ReportRemoteDataSourceImpl
    @Inject
    constructor(
        private val api: OpenAiApi,
    ) : ReportRemoteDataSource {
        private val json = Json { ignoreUnknownKeys = true }

        override suspend fun getChartSummary(
            totalTilCount: Int,
            averageEmotionScore: Float,
            emotionCounts: Map<Emotion, Int>,
        ): String {
            val prompt = createChartSummaryPrompt(emotionCounts, totalTilCount, averageEmotionScore)
            val schemaObject = createResponseSchema()

            val textConfig =
                ResponseTextConfig(
                    format = JsonSchemaFormat(name = "chart_summary", schema = schemaObject),
                )
            val request =
                OpenAiRequestDto(
                    input = prompt,
                    textConfig = textConfig,
                )

            val response = api.createResponse(request)
            return parseResponse(response)
        }

        private fun createChartSummaryPrompt(
            emotionCounts: Map<Emotion, Int>,
            totalTilCount: Int,
            averageEmotionScore: Float,
        ): String {
            val emotionStats =
                emotionCounts.entries.joinToString(", ") { "${it.key.emoji}(${it.value}회)" }

            return """
                당신은 학습 데이터 분석 전문가입니다. 사용자의 한 달간 TIL(Today I Learned) 데이터를 바탕으로 따뜻하고 격려하는 어조의 리포트를 작성해주세요.
                
                [데이터 요약]
                - 이번 달 총 학습 일수: ${totalTilCount}일
                - 평균 감정 점수: ${String.format(Locale.getDefault(), "%.1f", averageEmotionScore)}점
                - 감정별 횟수: $emotionStats
                
                [작성 가이드라인]
                1. 첫 문장은 학습 일수에 대한 칭찬으로 시작하세요.
                2. 평균 감정 점수와 적절한 이모지(예: 🤩, 😊, 😐)를 포함하여 전반적인 기분을 언급하세요.
                3. 데이터에서 보이는 특징적인 패턴(예: 특정 감정이 많을 때의 경향성 등)을 하나 짚어주세요. 
                4. 가독성을 위해 1~2문장 단위로 줄바꿈(\n\n)을 적절히 섞어서 작성하세요.
                5. 마지막은 다음 달을 위한 따뜻한 응원으로 마무리하세요.
                
                [응답 형식]
                반드시 아래 JSON 형식으로만 응답하세요:
                {
                    "summary": "분석 결과 요약 내용"
                }
                """.trimIndent()
        }

        private fun createResponseSchema(): JsonObject {
            val schemaString =
                """
                {
                  "type": "object",
                  "properties": {
                    "summary": { "type": "string" }
                  },
                  "required": ["summary"],
                  "additionalProperties": false
                }
                """.trimIndent()

            return json.parseToJsonElement(schemaString).jsonObject
        }

        private fun parseResponse(response: OpenAiResponseDto): String {
            val contentItem =
                response.output.flatMap { it.content ?: emptyList() }.firstOrNull {
                    it.type == "output_text" || it.type == "refusal"
                } ?: throw Exception("Empty AI Response")

            if (contentItem.type == "refusal") {
                throw Exception("AI Refusal: ${contentItem.refusal}")
            }

            val jsonString = contentItem.text ?: throw Exception("Empty content.text Response")
            return json.decodeFromString<ChartSummaryDto>(jsonString).summary
        }
    }
