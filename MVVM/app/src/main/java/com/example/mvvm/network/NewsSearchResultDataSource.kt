package com.example.mvvm.network

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.mvvm.model.NewsArticle
import com.example.mvvm.model.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsSearchResultDataSource(private val newsApi: NewsApi,private val query: String) : PageKeyedDataSource<Int, NewsArticle>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, NewsArticle>) {
        searchNews(1, params.requestedLoadSize) { articles, next ->
            callback.onResult(articles, null, next)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, NewsArticle>) {
        searchNews(params.key, params.requestedLoadSize) { articles, next ->
            callback.onResult(articles, next)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, NewsArticle>) {

    }

    private fun searchNews(page: Int, pageSize: Int, callback: (articles: List<NewsArticle>, next: Int) -> Unit) {
        newsApi.pagingSearchNews(if(TextUtils.isEmpty(query)) "" else query, page, pageSize, NewsRepository.API_KEY)
            .enqueue(object : Callback<NewsResponse> {
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                callback(listOf(), 0)
            }
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    response.body()?.articles?.also {
                        callback(it, page+1)
                    } ?: kotlin.run {
                        callback(listOf(), 0)
                    }
                } else {
                    callback(listOf(), 0)
                }
            }
        })
    }
}