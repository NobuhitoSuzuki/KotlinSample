package com.example.mvvm.ui

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter

import com.example.mvvm.R
import com.example.mvvm.adapter.NewsSearchAdapter
import com.example.mvvm.databinding.NewsSearchFragmentBinding
import com.example.mvvm.model.NewsArticle
import com.example.mvvm.viewModel.NewsSearchViewModel
import com.example.mvvm.viewModel.NewsSearchViewModelFactory

class NewsSearchFragment : Fragment() {

    companion object {
        fun newInstance() = NewsSearchFragment()
    }

    private lateinit var viewModel: NewsSearchViewModel
    private lateinit var binding: NewsSearchFragmentBinding
    private lateinit var adapter : NewsSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.news_search_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, NewsSearchViewModelFactory(requireContext())).get(NewsSearchViewModel::class.java)
        binding.viewModel = viewModel
        adapter = NewsSearchAdapter(requireContext())
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerView.adapter = adapter
        viewModel.newsData.observe(this, Observer {response ->
            viewModel.setLoading(false)
            if (response != null) {
                adapter.submitArticles(response.articles)
            }
        })
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(query: String?): Boolean {
                adapter.submitArticles(listOf())
                viewModel.setLoading(true)
                viewModel.setQuery(query ?: "")
                return false
            }
        })
    }

}
