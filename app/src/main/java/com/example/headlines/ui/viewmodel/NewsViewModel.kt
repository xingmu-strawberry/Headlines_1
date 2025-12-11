package com.example.headlines.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.headlines.data.repository.NewsRepository
import com.example.headlines.data.model.News
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    private val repository = NewsRepository()

    private val _newsList = MutableLiveData<List<News>>()
    val newsList: LiveData<List<News>> = _newsList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private var currentCategory = "推荐"

    fun fetchNews(category: String = currentCategory) {
        currentCategory = category
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            val result = repository.getNewsByCategory(category)

            if (result.isSuccess) {
                _newsList.value = result.getOrNull() ?: emptyList()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "未知错误"
                // 发生错误时清空列表，显示空状态
                _newsList.value = emptyList()
            }

            _isLoading.value = false
        }
    }

    fun refreshNews() {
        fetchNews(currentCategory)
    }
}