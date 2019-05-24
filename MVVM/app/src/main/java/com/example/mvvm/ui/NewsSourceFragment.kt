package com.example.mvvm.ui

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import androidx.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI

import com.example.mvvm.R
import com.example.mvvm.adapter.NewsSourceAdapter
import com.example.mvvm.databinding.FragmentNewsSourceBinding
import com.example.mvvm.viewModel.NewsSourceViewModel
import com.example.mvvm.viewModel.NewsSourceViewModelFactory

class NewsSourceFragment : Fragment() {

    lateinit var binding: FragmentNewsSourceBinding
    lateinit var viewModel: NewsSourceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_source, container, false)
        viewModel = ViewModelProviders.of(this, NewsSourceViewModelFactory(requireContext())).get(NewsSourceViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        viewModel.setLoading(true)

        viewModel.newsSource.observe(this, Observer {response ->
            if (response != null) {
                val adapter = NewsSourceAdapter(requireContext(), response.sources, object : NewsSourceAdapter.OnNewsSourceItemClickListener {
                    override fun OnSourceClick(name: String) {
                        Navigation.findNavController(requireActivity(), R.id.content_frame)
                            .navigate(R.id.action_navigation_home_to_newsFragment, NewsFragment.createBundle(name))
                    }
                })
                binding.recyclerView.adapter = adapter
                viewModel.setLoading(false)
            }
        })

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            NewsSourceFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
