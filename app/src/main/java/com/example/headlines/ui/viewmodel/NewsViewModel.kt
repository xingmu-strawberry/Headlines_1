package com.example.headlines.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.headlines.data.model.News
import com.example.headlines.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()

    private val _newsList = MutableStateFlow<List<News>>(emptyList())
    val newsList: StateFlow<List<News>> = _newsList

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.IDLE)
    val loadingState: StateFlow<LoadingState> = _loadingState

    fun loadNews(refresh: Boolean = false) {
        viewModelScope.launch {
            _loadingState.value = LoadingState.LOADING
            try {
                val news = repository.getNews(refresh)
                _newsList.value = if (refresh) news else _newsList.value + news
                _loadingState.value = LoadingState.SUCCESS
            } catch (e: Exception) {
                _loadingState.value = LoadingState.ERROR(e.message ?: "加载失败")
            }
        }
    }
}

sealed class LoadingState {
    object IDLE : LoadingState()
    object LOADING : LoadingState()
    object SUCCESS : LoadingState()
    data class ERROR(val message: String) : LoadingState()
}