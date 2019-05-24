package com.example.mvvm.adapter

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mvvm.R
import com.example.mvvm.databinding.NewsItemBinding
import com.example.mvvm.databinding.NewsSourceItemBinding
import com.example.mvvm.model.NewsArticle

class NewsAdapter(private val context: Context, private var articles: List<NewsArticle>?)
    : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NewsViewHolder {
        val binding : NewsItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.news_item, p0, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        articles?.let { return it.size }
        return 0
    }

    override fun onBindViewHolder(p0: NewsViewHolder, p1: Int) {
        articles?.let {
            p0.bind(it[p1])
        }
    }

    inner class NewsViewHolder(private val binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: NewsArticle) {
            with(binding) {
                this.article = article
                executePendingBindings()
            }
        }
    }
}