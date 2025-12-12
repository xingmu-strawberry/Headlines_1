package com.example.headlines.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TaskHistory(
    val id: String,
    val type: Type,           // 记录类型
    val title: String,        // 记录标题
    val description: String,  // 详细描述
    val points: Int,          // 获得积分
    val timestamp: Long,      // 时间戳
    val status: Status = Status.COMPLETED
) {
    enum class Type {
        SIGN_IN,      // 签到
        TASK_COMPLETE, // 任务完成
        POINTS_EXCHANGE // 积分兑换
    }

    enum class Status {
        COMPLETED,
        FAILED,
        PENDING
    }

    fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun getFormattedDateShort(): String {
        val sdf = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}