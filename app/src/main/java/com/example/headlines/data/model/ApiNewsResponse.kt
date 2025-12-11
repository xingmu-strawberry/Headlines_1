package com.example.headlines.data.model

import com.google.gson.annotations.SerializedName

/**
 * 聚合数据API返回的响应结构
 * 根据实际API文档调整字段
 */
data class ApiNewsResponse(
    @SerializedName("error_code") val errorCode: Int,
    @SerializedName("reason") val reason: String,
    @SerializedName("result") val result: NewsResult?
)

data class NewsResult(
    @SerializedName("stat") val stat: String,
    @SerializedName("data") val data: List<ApiNewsItem>?
)

data class ApiNewsItem(
    @SerializedName("uniquekey") val uniqueKey: String,
    @SerializedName("title") val title: String,
    @SerializedName("date") val date: String,
    @SerializedName("category") val category: String,
    @SerializedName("author_name") val authorName: String,
    @SerializedName("url") val url: String,
    @SerializedName("thumbnail_pic_s") val thumbnailPicS: String?,
    @SerializedName("thumbnail_pic_s02") val thumbnailPicS02: String?,
    @SerializedName("thumbnail_pic_s03") val thumbnailPicS03: String?,
    @SerializedName("is_content") val isContent: String?,
    @SerializedName("is_top") val isTop: String? = "0"
)