package com.example.mvvm.ui


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.mvvm.R
import com.example.mvvm.adapter.NewsSearchResultPagingAdapter
import com.example.mvvm.databinding.FragmentNewsSearchPagingBinding
import com.example.mvvm.model.NewsArticle
import com.example.mvvm.viewModel.NewsSearchPagingViewModel
import com.example.mvvm.viewModel.NewsSearchPagingViewModelFactory

class NewsSearchPagingFragment : Fragment() {

    private lateinit var binding: FragmentNewsSearchPagingBinding
    private lateinit var viewModel: NewsSearchPagingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_search_paging, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, NewsSearchPagingViewModelFactory(requireContext())).get(NewsSearchPagingViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val adapter =  NewsSearchResultPagingAdapter()
        binding.recyclerView.adapter = adapter
        viewModel.newsSearchPagedList.observe(this, Observer<PagedList<NewsArticle>> {
            Log.d(this::class.java.name, it.toString())
            adapter.submitList(it)
        })
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setQuery(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            NewsSearchPagingFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
