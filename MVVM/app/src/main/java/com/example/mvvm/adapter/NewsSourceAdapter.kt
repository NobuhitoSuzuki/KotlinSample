package com.example.mvvm.adapter

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mvvm.R
import com.example.mvvm.databinding.NewsSourceItemBinding
import com.example.mvvm.model.NewsSource

class NewsSourceAdapter(val context: Context,var sources: List<NewsSource>?,val itemClickListener: OnNewsSourceItemClickListener?) : RecyclerView.Adapter<NewsSourceAdapter.NewsSourceViewHolder>() {

    interface OnNewsSourceItemClickListener {
        fun OnSourceClick(name: String)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NewsSourceViewHolder {
        val binding = DataBindingUtil.inflate<NewsSourceItemBinding>(LayoutInflater.from(context), R.layout.news_source_item, p0, false)
        return NewsSourceViewHolder(binding)
    }

    override fun getItemCount(): Int {
        sources?.also { return it.size }
        return 0
    }

    override fun onBindViewHolder(holder: NewsSourceViewHolder, position: Int) {
        sources?.let {
            holder.bind(it[position], createItemClickListener(it[position].id))
        }
    }

    private fun createItemClickListener(name: String): View.OnClickListener {
        return View.OnClickListener {
            itemClickListener?.OnSourceClick(name)
        }
    }

    inner class NewsSourceViewHolder(private val binding: NewsSourceItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(source: NewsSource, clickListener: View.OnClickListener) {
            with(binding) {
                itemClickListener = clickListener
                this.source = source
                executePendingBindings()
            }
        }
    }
}