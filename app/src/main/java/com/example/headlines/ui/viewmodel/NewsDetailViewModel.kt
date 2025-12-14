package com.example.headlines.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.headlines.data.model.MockData
import com.example.headlines.data.model.NewsDetail
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class NewsDetailViewModel : ViewModel() {

    private val _newsDetail = MutableLiveData<NewsDetail?>()
    val newsDetail: LiveData<NewsDetail?> = _newsDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLiked = MutableLiveData<Boolean>()
    val isLiked: LiveData<Boolean> = _isLiked

    private val _likeCount = MutableLiveData<Int>()
    val likeCount: LiveData<Int> = _likeCount

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _viewCount = MutableLiveData<Int>()
    val viewCount: LiveData<Int> = _viewCount

    private var loadedNewsId: Int? = null

    // 加载新闻详情
    fun loadNewsDetail(newsId: Int) {
        // 如果已经加载过相同的新闻，直接返回
        if (loadedNewsId == newsId && _newsDetail.value != null) {
            return
        }

        loadedNewsId = newsId
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                // 模拟网络延迟
                delay(800)

                // 从MockData获取新闻详情
                val detail = MockData.getNewsDetailById(newsId)

                if (detail != null) {
                    _newsDetail.value = detail
                    _likeCount.value = detail.likeCount
                    _viewCount.value = detail.viewCount

                    // 初始化用户交互状态（实际应用中应从本地存储读取）
                    _isLiked.value = false
                    _isFavorite.value = false
                } else {
                    _errorMessage.value = "新闻详情不存在"
                    _newsDetail.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = "加载失败：${e.message}"
                _newsDetail.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 刷新新闻详情
    fun refreshNewsDetail() {
        loadedNewsId?.let { loadNewsDetail(it) }
    }

    // 切换点赞状态
    fun toggleLike() {
        _isLiked.value?.let { currentLiked ->
            val newLiked = !currentLiked
            _isLiked.value = newLiked

            // 更新点赞数量
            _likeCount.value?.let { currentCount ->
                val change = if (newLiked) 1 else -1
                _likeCount.value = currentCount + change
            }
        }
    }

    // 切换收藏状态
    fun toggleFavorite() {
        _isFavorite.value?.let { currentFavorite ->
            _isFavorite.value = !currentFavorite
        }
    }

    // 增加阅读量
    fun incrementViewCount() {
        _viewCount.value?.let { currentCount ->
            _viewCount.value = currentCount + 1
        }
    }

    // 重置状态
    fun clearState() {
        loadedNewsId = null
        _newsDetail.value = null
        _isLiked.value = false
        _isFavorite.value = false
        _errorMessage.value = null
    }
}