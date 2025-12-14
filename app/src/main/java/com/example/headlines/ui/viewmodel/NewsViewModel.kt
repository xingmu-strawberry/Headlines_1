package com.example.headlines.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.headlines.data.model.MockData // 引入 MockData
import com.example.headlines.data.model.News
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    // LiveData 变量
    private val _newsList = MutableLiveData<List<News>>()
    val newsList: LiveData<List<News>> = _newsList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // 计数器和类别状态保持不变
    private var refreshCount = 0
    private var currentCategory = "推荐"

    init {
        // 初始化时加载推荐新闻
        fetchNews("推荐")
    }

    fun fetchNews(category: String) {
        currentCategory = category
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            // 🚨 核心修改 1: 递增 refreshCount 并在获取数据时传入
            refreshCount++

            // 🚨 核心修改 2: 调用 MockData 提供的统一接口获取列表
            _newsList.value = MockData.getNewsListByCategory(category, refreshCount)

            _isLoading.value = false
        }
    }

    fun refreshNews() {
        // 直接调用 fetchNews，它会递增 refreshCount 并重新加载当前类别
        fetchNews(currentCategory)
    }

    // 搜索功能
    fun searchNews(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            // 🚨 核心修改 3: 调用 MockData 获取用于搜索的模拟数据
            // 注意：这里 search 的 refreshCount 可能需要和 fetchNews 同步
            val searchData = MockData.getSearchNewsData(refreshCount)

            val filteredNews = searchData.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.content.contains(query, ignoreCase = true)
            }

            _newsList.value = filteredNews
            _isLoading.value = false

            if (filteredNews.isEmpty()) {
                _errorMessage.value = "未找到相关新闻"
            }
        }
    }

    // 🚨 删除了所有数据生成相关的私有函数
}