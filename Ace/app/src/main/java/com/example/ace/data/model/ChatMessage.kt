package com.example.ace.data.model

// -- Data class that captures chat messages
data class ChatMessage(
    val topic : String,
    val sender : ChatUser,
    val timestamp: Long,
    val message: String
)
