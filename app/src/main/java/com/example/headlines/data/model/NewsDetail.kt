package com.example.headlines.data.model

enum class NewsDetailType {
    TEXT,       // 纯文本
    IMAGE,      // 单图/多图
    VIDEO,      // 视频
    LONG_IMAGE  // 3张横向排列图片
}

data class NewsDetail(
    val id: Int,
    val type: NewsDetailType,
    val title: String,
    val author: String,
    val authorAvatar: String = "", // 作者头像URL
    val publishTime: Long,  // 时间戳
    val viewCount: Int,
    val commentCount: Int,
    val likeCount: Int,
    val content: String = "",     // 文本内容
    val videoUrl: String? = null, // 视频链接
    val images: List<String> = emptyList() // 图片URL列表
)

// 扩展函数：格式化时间
fun NewsDetail.formatPublishTime(): String {
    val currentTime = System.currentTimeMillis()
    val diff = currentTime - publishTime

    return when {
        diff < 60000 -> "刚刚" // 1分钟内
        diff < 3600000 -> "${diff / 60000}分钟前" // 1小时内
        diff < 86400000 -> "${diff / 3600000}小时前" // 24小时内
        diff < 604800000 -> "${diff / 86400000}天前" // 7天内
        else -> {
            val date = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            date.format(java.util.Date(publishTime))
        }
    }
}

// 扩展函数：格式化数字
fun NewsDetail.formatCount(count: Int): String {
    return when {
        count >= 10000 -> "${count / 10000}万"
        count >= 1000 -> "${count / 1000}k"
        else -> count.toString()
    }
}