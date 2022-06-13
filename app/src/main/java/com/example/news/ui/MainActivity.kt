package com.example.news.ui

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.news.R
import com.example.news.database.ArticleDataBase
import com.example.news.repository.NewsRepository
import com.example.newsapp.NewsViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    //private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_breakingNews,
                R.id.navigation_favoritesNews,
                R.id.navigation_searchNews
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val newsRepository = NewsRepository(ArticleDataBase(this))
        val newsViewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, newsViewModelProviderFactory).get(NewsViewModel::class.java)
    }
}