package com.example.mvvm.network

import androidx.paging.DataSource
import com.example.mvvm.model.NewsArticle


class NewsDataSourceFactory(newsApi: NewsApi, query: String)
    : DataSource.Factory<Int, NewsArticle>() {
    private val dataSource = NewsSearchResultDataSource(newsApi, query)
    override fun create(): DataSource<Int, NewsArticle> {
        return dataSource
    }
}