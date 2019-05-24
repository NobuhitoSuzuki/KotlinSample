package com.example.mvvm.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import java.lang.IllegalArgumentException

class NewsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass != NewsViewModel::class.java) throw IllegalArgumentException("Unknown Class!!")
        return NewsViewModel(context) as T
    }
}