package com.example.headlines.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.headlines.data.model.MockData
import com.example.headlines.data.model.NewsDetail

class NewsDetailViewModel : ViewModel() {

    private val _newsDetail = MutableLiveData<NewsDetail?>()
    val newsDetail: LiveData<NewsDetail?> = _newsDetail

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadNewsDetail(newsId: String, type: String) {
        _isLoading.value = true
        _error.value = null

        // 使用Handler模拟网络延迟
        android.os.Handler().postDelayed({
            try {
                val detail = MockData.generateMockDetail(newsId, type)
                _newsDetail.value = detail
            } catch (e: Exception) {
                _error.value = "加载失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }, 800) // 800ms延迟
    }

    fun refresh() {
        val currentDetail = _newsDetail.value
        currentDetail?.let {
            loadNewsDetail(it.id, it.type.name)
        }
    }
}