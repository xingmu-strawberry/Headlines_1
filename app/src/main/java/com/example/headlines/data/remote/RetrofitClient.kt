package com.example.headlines.data.remote

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://v.juhe.cn/"

    private lateinit var apiService: ApiService

    fun init() {  // 注意：移除了 Context 参数
        // 从 BuildConfig 获取 API 密钥
        val apiKey = com.example.headlines.BuildConfig.JUHE_API_KEY

        if (apiKey.isBlank()) {
            throw IllegalArgumentException("请在 local.properties 中配置 JUHE_API_KEY")
        }

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val original = chain.request()
                val url = original.url.newBuilder()
                    .addQueryParameter("key", apiKey)
                    .build()
                val request = original.newBuilder().url(url).build()
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    fun getApiService(): ApiService {
        if (!::apiService.isInitialized) {
            throw IllegalStateException("RetrofitClient must be initialized first")
        }
        return apiService
    }
}