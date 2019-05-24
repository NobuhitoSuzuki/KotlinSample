package com.example.mvvm.ui

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import androidx.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.mvvm.R
import com.example.mvvm.adapter.NewsAdapter
import com.example.mvvm.databinding.FragmentNewsBinding
import com.example.mvvm.viewModel.NewsViewModel
import com.example.mvvm.viewModel.NewsViewModelFactory

private const val ARG_SOURCE_NAME = "source_name"

class NewsFragment : Fragment() {

    private var sourceName : String? = null

    private lateinit var binding: FragmentNewsBinding
    private lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sourceName = it.getString(ARG_SOURCE_NAME, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false)
        viewModel = ViewModelProviders.of(this, NewsViewModelFactory(requireContext())).get(NewsViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.setLoading(true)
        sourceName?.let {
            viewModel.setSource(it)
        }

        viewModel.newsData.observe(this, Observer {response ->
            viewModel.setLoading(false)
            binding.recyclerView.adapter = NewsAdapter(requireContext(), response?.articles)
        })
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(sourceName : String) =
            NewsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SOURCE_NAME, sourceName)
                }
            }

        @JvmStatic
        fun createBundle(sourceName: String): Bundle {
            return Bundle().apply {
                putString(ARG_SOURCE_NAME, sourceName)
            }
        }
    }
}
