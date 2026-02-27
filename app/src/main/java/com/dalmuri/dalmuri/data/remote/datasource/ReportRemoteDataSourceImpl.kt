package com.dalmuri.dalmuri.data.remote.datasource

import com.dalmuri.dalmuri.data.remote.api.OpenAiApi
import com.dalmuri.dalmuri.data.remote.model.ChartSummaryDto
import com.dalmuri.dalmuri.data.remote.model.JsonSchemaFormat
import com.dalmuri.dalmuri.data.remote.model.MonthlyReviewDto
import com.dalmuri.dalmuri.data.remote.model.OpenAiRequestDto
import com.dalmuri.dalmuri.data.remote.model.OpenAiResponseDto
import com.dalmuri.dalmuri.data.remote.model.ResponseTextConfig
import com.dalmuri.dalmuri.data.remote.prompt.generateChartSummaryPrompt
import com.dalmuri.dalmuri.data.remote.prompt.generateReviewPrompt
import com.dalmuri.dalmuri.domain.model.Emotion
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import okhttp3.internal.format
import javax.inject.Inject

class ReportRemoteDataSourceImpl
    @Inject
    constructor(
        private val api: OpenAiApi,
    ) : ReportRemoteDataSource {
        private val json = Json { ignoreUnknownKeys = true }

        override suspend fun generateChartSummary(
            totalTilCount: Int,
            averageEmotionScore: Float,
            emotionCounts: Map<Emotion, Int>,
        ): String {
            val prompt = generateChartSummaryPrompt(emotionCounts, totalTilCount, averageEmotionScore)
            val schemaObject = generateResponseSchema()

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
            return parseChartSummaryResponse(response)
        }

        override suspend fun generateMonthlyReview(
            totalCount: Int,
            averageEmotionScore: Float,
            emotionDistribution: String,
            tilDetails: String,
        ): MonthlyReviewDto {
            val prompt =
                generateReviewPrompt(
                    totalCount = totalCount,
                    averageEmotionScore = averageEmotionScore,
                    emotionDistribution = emotionDistribution,
                    tilDetails = tilDetails,
                )

            val schemaObject = generateMonthlyReviewSchema()
            val textConfig =
                ResponseTextConfig(
                    format = JsonSchemaFormat(name = "monthly_review", schema = schemaObject),
                )

            val request = OpenAiRequestDto(input = prompt, textConfig = textConfig)
            val response = api.createResponse(request)

            return parseMonthlyReviewResponse(response)
        }

        private fun generateMonthlyReviewSchema(): JsonObject {
            val schemaString =
                """
                {
                  "type": "object",
                  "properties": {
                    "learningKeywords": { "type": "array", "items": { "type": "string" } },
                    "overallMood": { "type": "string" },
                    "challengeDate": { "type": "string" },
                    "growthPoints": { "type": "array", "items": { "type": "string" } },
                    "nextMonthAdvice": { "type": "string" }
                  },
                  "required": ["learningKeywords", "overallMood", "challengeDate", "growthPoints", "nextMonthAdvice"],
                  "additionalProperties": false
                }
                """.trimIndent()

            return json.parseToJsonElement(schemaString).jsonObject
        }

        private fun parseMonthlyReviewResponse(response: OpenAiResponseDto): MonthlyReviewDto {
            val jsonString = extractTextFromResponse(response)
            return json.decodeFromString(jsonString)
        }

        private fun parseChartSummaryResponse(response: OpenAiResponseDto): String {
            val jsonString = extractTextFromResponse(response)
            return json.decodeFromString<ChartSummaryDto>(jsonString).summary
        }

        private fun extractTextFromResponse(response: OpenAiResponseDto): String {
            val contentItem =
                response.output.flatMap { it.content ?: emptyList() }.firstOrNull {
                    it.type == "output_text" || it.type == "refusal"
                }
                    ?: throw Exception("Empty AI Response")

            if (contentItem.type == "refusal") {
                throw Exception("AI Refusal: ${contentItem.refusal}")
            }

            return contentItem.text ?: throw Exception("Empty content.text Response")
        }

        private fun generateResponseSchema(): JsonObject {
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
    }
