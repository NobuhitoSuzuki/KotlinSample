package com.example.mvvm.adapter

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mvvm.R
import com.example.mvvm.databinding.NewsSearchItemBinding
import com.example.mvvm.model.NewsArticle

class NewsSearchAdapter(private val context: Context)
    : ListAdapter<NewsArticle, NewsSearchAdapter.NewsSearchViewHolder>(ItemCallback()) {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NewsSearchViewHolder {
        return NewsSearchViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.news_search_item, p0, false))
    }

    override fun onBindViewHolder(p0: NewsSearchViewHolder, p1: Int) {
        getItem(p1)?.let {
            p0.bind(it)
        }
    }

    fun submitArticles(articles :List<NewsArticle>?) {
        this.submitList(articles)
    }

    inner class NewsSearchViewHolder(private val binding: NewsSearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: NewsArticle) {
            with(binding) {
                text.text = article.title
            }
        }
    }

    class ItemCallback : DiffUtil.ItemCallback<NewsArticle>() {
        override fun areItemsTheSame(p0: NewsArticle, p1: NewsArticle): Boolean {
            return (p0.author == p1.author) &&
                    (p0.title == p1.title) &&
                    (p0.publishedAt == p1.publishedAt)
        }

        override fun areContentsTheSame(p0: NewsArticle, p1: NewsArticle): Boolean {
            return p0 == p1
        }
    }

}