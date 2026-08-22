package com.example.a3_1mod2h2

data class ChatRequest(
    val session_id: String,
    val message: String
)

data class ChatResponse(
    val success: Boolean = false,
    val session_id: String? = null,
    val reply: String? = null,
    val error: String? = null
)

data class ChatMessage(
    val message: String,
    val isUser: Boolean,
    val isError: Boolean = false
)