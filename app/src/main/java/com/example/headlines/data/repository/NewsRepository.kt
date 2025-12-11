package com.example.headlines.data.repository

import com.example.headlines.data.mapper.NewsMapper
import com.example.headlines.data.remote.RetrofitClient
import com.example.headlines.data.model.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRepository {

    private val apiService = RetrofitClient.getApiService()

    suspend fun getNewsByCategory(category: String): Result<List<News>> {
        return withContext(Dispatchers.IO) {
            try {
                // 映射分类到API的type参数
                val apiType = mapCategoryToApiType(category)

                // 调用API
                val response = apiService.getNews(
                    apiKey = "", // 密钥已经在Interceptor中添加
                    type = apiType
                )

                // 检查响应
                if (response.errorCode == 0 && response.result?.data != null) {
                    val newsList = response.result.data.map { apiNews ->
                        NewsMapper.mapApiToDomain(apiNews)
                    }
                    Result.success(newsList)
                } else {
                    Result.failure(Exception("API错误: ${response.reason}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun mapCategoryToApiType(category: String): String {
        return when (category) {
            "推荐" -> "top"          // 头条
            "热榜" -> "top"          // 头条作为热榜
            "关注" -> "shehui"       // 社会新闻
            "新时代" -> "guonei"     // 国内新闻
            "小说" -> "yule"         // 娱乐新闻
            "视频" -> "shishang"     // 时尚（API可能没有专门的视频分类）
            else -> "top"
        }
    }
}