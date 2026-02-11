package com.dalmuri.dalmuri.data.remote.api

import com.dalmuri.dalmuri.data.remote.model.OpenAiRequestDto
import com.dalmuri.dalmuri.data.remote.model.OpenAiResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenAiApi {
    @POST("v1/responses")
    suspend fun createResponse(
        @Body request: OpenAiRequestDto,
    ): OpenAiResponseDto
}
