package com.example.mvvm.viewModel

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.mvvm.model.NewsArticle
import com.example.mvvm.network.NewsApi
import com.example.mvvm.network.NewsDataSourceFactory
import com.example.mvvm.network.RetrofitService

class NewsSearchPagingViewModel(val context: Context) : ViewModel() {
    private val newsApi: NewsApi = RetrofitService.create(NewsApi::class.java)
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading = _isLoading as LiveData<Boolean>
    private val query: MutableLiveData<String> = MutableLiveData()

    var newsSearchPagedList: LiveData<PagedList<NewsArticle>> = Transformations.switchMap(query) {
        val factory = NewsDataSourceFactory(newsApi, it)
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(20)
            .setPageSize(20)
            .build()
       LivePagedListBuilder(factory, config).build()
    }

    fun setQuery(query: String?) {
        this.query.postValue(query)
    }
}