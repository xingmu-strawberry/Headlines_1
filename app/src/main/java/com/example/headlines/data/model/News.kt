package com.example.headlines.data.model

// 新闻数据类型枚举
enum class NewsType {
    TEXT,          // 纯文字
    IMAGE,         // 带图片
    VIDEO,         // 带视频
    LONG_IMAGE     // 带长图
}

// 新闻数据类 - 添加 content 字段
data class News(
    val id: Int,
    val title: String,
    val content: String,           // 新增：新闻内容
    val type: NewsType,
    val source: String = "头条新闻",
    val commentCount: Int = 0,
    val publishTime: String = "2小时前",
    val imageUrl: String? = null,    // 图片URL
    val videoUrl: String? = null,    // 视频URL
    val videoDuration: String? = null, // 视频时长
    val isTop: Boolean = false       // 是否置顶
)