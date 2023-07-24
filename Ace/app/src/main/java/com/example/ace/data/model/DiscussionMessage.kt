package com.example.ace.data.model

data class DiscussionMessage(
    val course: String = "",
    val message: String = "",
    val sender: String = "",
    val timestamp: Long? = 0,
)
