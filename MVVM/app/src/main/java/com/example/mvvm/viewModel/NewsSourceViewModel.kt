package com.example.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.content.Context
import com.example.mvvm.model.NewsSource
import com.example.mvvm.model.NewsSourceResponse
import com.example.mvvm.network.NewsRepository

class NewsSourceViewModel(context: Context) : ViewModel() {
    private val newsRepository: NewsRepository = NewsRepository.getInstance(context)
    val newsSource : LiveData<NewsSourceResponse>
    private val _isLoading : MutableLiveData<Boolean> = MutableLiveData()

    val isLoading = _isLoading as LiveData<Boolean>

    init {
        newsSource = newsRepository.getNewsSource()
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.postValue(isLoading)
    }
}