package com.example.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import android.content.Context
import com.example.mvvm.model.NewsResponse
import com.example.mvvm.network.NewsRepository

class NewsViewModel(context: Context) : ViewModel() {
    private val newsRepository: NewsRepository = NewsRepository.getInstance(context)

    private val source: MutableLiveData<String> = MutableLiveData()

    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    val newsData: LiveData<NewsResponse> = Transformations.switchMap(source) {
        newsRepository.getNews(it)
    }

    fun setSource(source: String) {
        this.source.postValue(source)
    }

    fun setLoading(isLoading: Boolean) {
        this.isLoading.postValue(isLoading)
    }
}