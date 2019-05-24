package com.example.mvvm.network

import com.example.mvvm.model.NewsResponse
import com.example.mvvm.model.NewsSourceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("top-headlines")
    fun getNewsList(@Query("sources") newsSource: String,
                    @Query("apiKey") apiKey: String) : Call<NewsResponse>

    @GET("sources")
    fun getNewsSource(@Query("apiKey") apiKey: String) : Call<NewsSourceResponse>


    @GET("everything")
    fun searchNews(@Query("q") query: String,
                   @Query("apiKey") apiKey: String) : Call<NewsResponse>

    @GET("everything")
    fun pagingSearchNews(@Query("q") query: String,
                         @Query("page") page: Int,
                         @Query("pageSize") pageSize: Int,
                         @Query("apiKey") apiKey: String) : Call<NewsResponse>
}