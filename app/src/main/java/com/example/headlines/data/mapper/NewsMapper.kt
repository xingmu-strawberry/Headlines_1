package com.example.headlines.data.mapper

import com.example.headlines.data.model.ApiNewsItem
import com.example.headlines.data.model.News
import com.example.headlines.data.model.NewsType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object NewsMapper {

    fun mapApiToDomain(apiNews: ApiNewsItem): News {
        return News(
            id = apiNews.uniqueKey.hashCode(),
            title = apiNews.title,
            type = determineNewsType(apiNews),
            source = apiNews.authorName.ifEmpty { "未知来源" },
            commentCount = 0, // 聚合数据API不提供评论数
            publishTime = formatPublishTime(apiNews.date),
            imageUrl = getMainImageUrl(apiNews),
            videoUrl = null, // 聚合数据API不提供视频
            videoDuration = null,
            isTop = apiNews.isTop == "1"
        )
    }

    private fun determineNewsType(apiNews: ApiNewsItem): NewsType {
        // 根据图片数量判断新闻类型
        val imageUrls = listOfNotNull(
            apiNews.thumbnailPicS,
            apiNews.thumbnailPicS02,
            apiNews.thumbnailPicS03
        )

        return when {
            imageUrls.size >= 3 -> NewsType.LONG_IMAGE
            imageUrls.size == 1 -> NewsType.IMAGE
            apiNews.url.contains("video") || apiNews.url.contains(".mp4") -> NewsType.VIDEO
            else -> NewsType.TEXT
        }
    }

    private fun getMainImageUrl(apiNews: ApiNewsItem): String? {
        return apiNews.thumbnailPicS ?: apiNews.thumbnailPicS02 ?: apiNews.thumbnailPicS03
    }

    private fun formatPublishTime(dateStr: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val date = inputFormat.parse(dateStr) ?: return dateStr

            val currentTime = System.currentTimeMillis()
            val publishTime = date.time
            val diff = currentTime - publishTime

            when {
                diff < TimeUnit.MINUTES.toMillis(1) -> "刚刚"
                diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)}分钟前"
                diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)}小时前"
                diff < TimeUnit.DAYS.toMillis(7) -> "${TimeUnit.MILLISECONDS.toDays(diff)}天前"
                else -> outputFormat.format(date)
            }
        } catch (e: Exception) {
            dateStr
        }
    }
}