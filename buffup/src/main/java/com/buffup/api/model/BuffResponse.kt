package com.buffup.api.model

data class BuffResponse(
    val id: Long,
    val timeToShow: Int, // seconds
    val priority: Int,
    val author: Author,
    val question: Question,
    val answers: List<Answer>,
    val language: String
)