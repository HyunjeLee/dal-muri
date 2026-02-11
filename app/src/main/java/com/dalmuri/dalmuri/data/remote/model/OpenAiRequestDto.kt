package com.dalmuri.dalmuri.data.remote.model

import com.dalmuri.dalmuri.data.remote.api.OpenAiConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class OpenAiRequestDto(
    @SerialName("model") val model: String = OpenAiConstants.GPT_MODEL,
    @SerialName("input") val input: String,
    @SerialName("instructions") val instructions: String = OpenAiConstants.DEFAULT_INSTRUCTIONS.trimIndent(),
    @SerialName("text") val textConfig: ResponseTextConfig,
)

@Serializable
data class ResponseTextConfig(
    @SerialName("format") val format: JsonSchemaFormat,
)

@Serializable
data class JsonSchemaFormat(
    @SerialName("type") val type: String = "json_schema",
    @SerialName("name") val name: String,
    @SerialName("strict") val strict: Boolean = true,
    @SerialName("schema") val schema: JsonObject,
)
