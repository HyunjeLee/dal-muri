package com.dalmuri.dalmuri.data.remote.datasource

import com.dalmuri.dalmuri.data.remote.api.OpenAiApi
import com.dalmuri.dalmuri.data.remote.model.AiAnalysisDto
import com.dalmuri.dalmuri.data.remote.model.JsonSchemaFormat
import com.dalmuri.dalmuri.data.remote.model.OpenAiRequestDto
import com.dalmuri.dalmuri.data.remote.model.OpenAiResponseDto
import com.dalmuri.dalmuri.data.remote.model.ResponseTextConfig
import com.dalmuri.dalmuri.data.remote.prompt.generateTilAnalysisPrompt
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
            val prompt = generateTilAnalysisPrompt(title, learned, difficulty, tomorrow)
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

        private fun createResponseSchema(): JsonObject {
            val schemaString =
                """
                {
                  "type": "object",
                  "properties": {
                    "emotion": { 
                      "type": "string", 
                      "enum": ["뿌듯함이 가득한 하루", "기분 좋게 만족스러운 하루", "별일 없이 평온한 하루", "조금은 고단했던 하루하루", "마음이 속상했던 하루"] 
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
