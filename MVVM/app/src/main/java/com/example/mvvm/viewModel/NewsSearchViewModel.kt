package com.example.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel;
import android.content.Context
import com.example.mvvm.model.NewsResponse
import com.example.mvvm.network.NewsRepository

class NewsSearchViewModel(context: Context) : ViewModel() {
    private val newsRepository: NewsRepository = NewsRepository.getInstance(context)

    private val query: MutableLiveData<String> = MutableLiveData()

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()

    val isLoading = _isLoading as LiveData<Boolean>

    val newsData: LiveData<NewsResponse> = Transformations.switchMap(query) {
        newsRepository.searchNews(it)
    }

    fun setQuery(source: String) {
        this.query.postValue(source)
    }

    fun setLoading(isLoading : Boolean) {
        _isLoading.postValue(isLoading)
    }
}
