package com.example.ace.data.model

// -- Data class that captures chat messages
data class ChatMessage(
    val sender: String = "",
    val timestamp: Long? = 0,
    val message: String? = "",
    val receiver: String? = ""
)

