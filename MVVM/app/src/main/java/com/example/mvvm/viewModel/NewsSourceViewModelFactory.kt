package com.example.mvvm.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import android.content.Context
import java.lang.IllegalArgumentException

class NewsSourceViewModelFactory(val context: Context): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass != NewsSourceViewModel::class.java) throw IllegalArgumentException("Unexpected class.")
        return NewsSourceViewModel(context) as T
    }
}