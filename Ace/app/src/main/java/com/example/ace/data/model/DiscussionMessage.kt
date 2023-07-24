package com.example.ace.data.model

data class DiscussionMessage(
    val course: String = "",
    val users: MutableList<String> = mutableListOf<String>(),
    val message: ChatMessage
)
