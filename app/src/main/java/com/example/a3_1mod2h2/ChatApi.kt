package com.example.a3_1mod2h2

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApi {

    @POST("chat")
    suspend fun sendMessage(
        @Body request: ChatRequest
    ): Response<ChatResponse>

    @POST("chat/clear")
    suspend fun clearChat(
        @Body request: Map<String, String>
    ): Response<ChatResponse>
}