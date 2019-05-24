package com.example.mvvm.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NewsSourceResponse(
    @Expose
    @SerializedName("status")
    val status: String,
    @SerializedName("sources")
    val sources: List<NewsSource>
)