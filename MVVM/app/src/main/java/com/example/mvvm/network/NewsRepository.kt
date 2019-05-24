package com.example.mvvm.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.content.Context
import android.text.TextUtils
import com.example.mvvm.model.NewsResponse
import com.example.mvvm.model.NewsSource
import com.example.mvvm.model.NewsSourceResponse
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class NewsRepository {
    private val newsApi: NewsApi = RetrofitService.create(NewsApi::class.java)

    fun getNews(source: String) : MutableLiveData<NewsResponse> {
        val newsData: MutableLiveData<NewsResponse>  = MutableLiveData()
        newsApi.getNewsList(source, API_KEY).enqueue(object : Callback<NewsResponse> {
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                newsData.postValue(null)
            }
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    newsData.postValue(response.body())
                }
            }
        })
        return newsData;
    }

    // 引数にliveDataを渡せば良いのでは？
    fun getNewsSource() : MutableLiveData<NewsSourceResponse> {
        val newsSource : MutableLiveData<NewsSourceResponse> = MutableLiveData()
        newsApi.getNewsSource(API_KEY).enqueue(object : Callback<NewsSourceResponse> {
            override fun onFailure(call: Call<NewsSourceResponse>, t: Throwable) {
                newsSource.postValue(null)
            }
            override fun onResponse(call: Call<NewsSourceResponse>, response: Response<NewsSourceResponse>) {
                if (response.isSuccessful) {
                    newsSource.postValue(response.body())
                }
            }
        })
        return newsSource
    }

    fun searchNews(query: String) : MutableLiveData<NewsResponse> {
        val newsData: MutableLiveData<NewsResponse>  = MutableLiveData()
        newsApi.searchNews(if(TextUtils.isEmpty(query)) "" else query, API_KEY).enqueue(object : Callback<NewsResponse> {
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                 newsData.postValue(null)
            }

            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    newsData.postValue(response.body())
                } else {
                    newsData.postValue(null)
                }
            }
        })
        return newsData
    }

    companion object {
        private var instance: NewsRepository? = null
        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: NewsRepository().also {
                instance = it
            }
        }

        public const val API_KEY = "d347d9c546ff41e5926a19b6bbaed7cf"
    }
}