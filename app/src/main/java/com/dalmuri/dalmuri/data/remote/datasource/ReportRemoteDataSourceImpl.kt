package com.dalmuri.dalmuri.data.remote.datasource

import com.dalmuri.dalmuri.data.remote.api.OpenAiApi
import com.dalmuri.dalmuri.data.remote.model.ChartSummaryDto
import com.dalmuri.dalmuri.data.remote.model.JsonSchemaFormat
import com.dalmuri.dalmuri.data.remote.model.OpenAiRequestDto
import com.dalmuri.dalmuri.data.remote.model.OpenAiResponseDto
import com.dalmuri.dalmuri.data.remote.model.ResponseTextConfig
import com.dalmuri.dalmuri.data.remote.prompt.generateChartSummaryPrompt
import com.dalmuri.dalmuri.domain.model.Emotion
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
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
            return parseResponse(response)
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
