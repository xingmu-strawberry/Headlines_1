package com.example.headlines.data.repository

import com.example.headlines.data.model.News
import com.example.headlines.data.model.NewsType

// data/repository/NewsRepository.kt
class NewsRepository {
    // 模拟数据源，后续可替换为网络请求
    fun getNews(refresh: Boolean = false): List<News> {
        // 生成模拟数据
        return generateMockNews()
    }

    private fun generateMockNews(): List<News> {
        // 生成4种类型的新闻数据
        return listOf(
            News(
                id = 1,
                title = "习近平主席关于中非合作重要论述",
                type = NewsType.TEXT,
                source = "海外网",
                commentCount = 30,
                publishTime = "2小时前",
                isTop = true
            ),
            // 更多模拟数据...
        )
    }
}