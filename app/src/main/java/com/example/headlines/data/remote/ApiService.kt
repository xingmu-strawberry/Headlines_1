package com.example.headlines.data.remote

import com.example.headlines.data.model.ApiNewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // 聚合数据新闻头条接口
    @GET("toutiao/index")
    suspend fun getNews(
        @Query("key") apiKey: String,
        @Query("type") type: String = "top" // 默认类型：top(头条), shehui(社会), guonei(国内)等
    ): ApiNewsResponse
}