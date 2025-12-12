package com.example.headlines.data.model

data class Task(
    val id: String,
    val title: String,
    val description: String,
    val iconRes: Int,
    val points: Int,
    var progress: Int,
    val total: Int,
    var completed: Boolean,
    val type: Type
) {
    enum class Type {
        DAILY,
        ACHIEVEMENT,
        NOVICE
    }

    fun getProgressText(): String {
        return if (total > 1) "$progress/$total" else ""
    }
}