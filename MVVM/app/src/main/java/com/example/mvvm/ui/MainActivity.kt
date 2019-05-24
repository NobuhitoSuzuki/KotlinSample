package com.example.mvvm.ui

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Resources
import androidx.databinding.DataBindingUtil
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm.R
import com.example.mvvm.adapter.NewsAdapter
import com.example.mvvm.databinding.ActivityMainBinding
import com.example.mvvm.ui.navagation.MyNavigator
import com.example.mvvm.viewModel.NewsViewModel
import com.example.mvvm.viewModel.NewsViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navController = findNavController(R.id.content_frame)
        val navHost = supportFragmentManager.findFragmentById(R.id.content_frame)
        navHost?.let {

        }
        binding.navView.setupWithNavController(navController)
        NavigationUI.setupWithNavController(binding.navView, navController)
    }

}
