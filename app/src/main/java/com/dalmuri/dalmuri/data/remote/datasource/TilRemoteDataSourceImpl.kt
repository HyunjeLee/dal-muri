package com.dalmuri.dalmuri.data.remote.datasource

import com.dalmuri.dalmuri.data.remote.api.OpenAiApi
import com.dalmuri.dalmuri.data.remote.model.AiAnalysisDto
import com.dalmuri.dalmuri.data.remote.model.JsonSchemaFormat
import com.dalmuri.dalmuri.data.remote.model.OpenAiRequestDto
import com.dalmuri.dalmuri.data.remote.model.OpenAiResponseDto
import com.dalmuri.dalmuri.data.remote.model.ResponseTextConfig
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import javax.inject.Inject

class TilRemoteDataSourceImpl
    @Inject
    constructor(
        private val api: OpenAiApi,
    ) : TilRemoteDataSource {
        private val json = Json { ignoreUnknownKeys = true }

        override suspend fun getTilAnalysis(
            title: String,
            learned: String,
            difficulty: String?,
            tomorrow: String?,
        ): AiAnalysisDto {
            val prompt = createAnalysisPrompt(title, learned, difficulty, tomorrow)
            val schemaObject = createResponseSchema()

            val textConfig = ResponseTextConfig(format = JsonSchemaFormat(name = "til_analysis", schema = schemaObject))
            val request =
                OpenAiRequestDto(
                    input = prompt,
                    textConfig = textConfig,
                )

            val response = api.createResponse(request)
            return parseResponse(response)
        }

        private fun createAnalysisPrompt(
            title: String,
            learned: String,
            difficulty: String?,
            tomorrow: String?,
        ): String =
            """
            당신은 개발자 학습 코치입니다. 아래 TIL(Today I Learned) 내용을 분석해주세요.

            [TIL 내용]
            제목: $title
            오늘 배운 것: $learned
            어려웠던 점: ${difficulty ?: "없음"}
            내일 할 일: ${tomorrow ?: "없음"}

            [분석 요청]
            다음 형식의 JSON으로만 응답해주세요:
            {
                "emotion": "성취감/만족/평범/어려움/좌절 중 하나",
                "emotionScore": 1-5 사이 정수,
                "difficultyLevel": "쉬움/보통/어려움/매우 어려움 중 하나",
                "comment": "격려나 조언 한 문장 (20자 이내)"
            }
            """.trimIndent()

        private fun createResponseSchema(): JsonObject {
            val schemaString =
                """
                {
                  "type": "object",
                  "properties": {
                    "emotion": { 
                      "type": "string", 
                      "enum": ["성취감", "만족", "평범", "어려움", "좌절"] 
                    },
                    "emotionScore": { 
                      "type": "integer",
                      "minimum": 1,
                      "maximum": 5
                    },
                    "difficultyLevel": { 
                      "type": "string", 
                      "enum": ["쉬움", "보통", "어려움", "매우 어려움"] 
                    },
                    "comment": { 
                      "type": "string" 
                    }
                  },
                  "required": ["emotion", "emotionScore", "difficultyLevel", "comment"],
                  "additionalProperties": false
                }
                """.trimIndent()
            return json.parseToJsonElement(schemaString).jsonObject
        }

        private fun parseResponse(response: OpenAiResponseDto): AiAnalysisDto {
            val contentItem =
                response.output.flatMap { it.content ?: emptyList() }.firstOrNull {
                    it.type == "output_text" || it.type == "refusal"
                } ?: throw Exception("Empty AI Response")

            if (contentItem.type == "refusal") {
                throw Exception("AI Refusal: ${contentItem.refusal}")
            }

            val jsonString = contentItem.text ?: throw Exception("Empty content.text Response")
            return json.decodeFromString<AiAnalysisDto>(jsonString)
        }
    }
