package com.example.mvvm.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("totalResults")
    val totalResults: String,
    @SerializedName("articles")
    val articles: List<NewsArticle>?
)