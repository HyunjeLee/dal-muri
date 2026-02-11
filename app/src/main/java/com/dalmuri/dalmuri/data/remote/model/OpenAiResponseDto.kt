package com.dalmuri.dalmuri.data.remote.model

import com.dalmuri.dalmuri.domain.model.Til
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenAiResponseDto(
    @SerialName("id") val id: String,
    @SerialName("output") val output: List<OutputItem>,
    @SerialName("status") val status: String,
)

@Serializable
data class OutputItem(
    @SerialName("type") val type: String,
    @SerialName("content") val content: List<ContentItem>? = null,
)

@Serializable
data class ContentItem(
    @SerialName("type") val type: String,
    @SerialName("text") val text: String? = null,
    @SerialName("refusal") val refusal: String? = null,
)

@Serializable
data class AiAnalysisDto(
    val emotion: String,
    val emotionScore: Int,
    val difficultyLevel: String,
    val comment: String,
) {
    fun toDomain(): Til.AiAnalysis =
        Til.AiAnalysis(
            emotion = this.emotion,
            emotionScore = this.emotionScore,
            difficultyLevel = this.difficultyLevel,
            comment = this.comment,
        )
}
