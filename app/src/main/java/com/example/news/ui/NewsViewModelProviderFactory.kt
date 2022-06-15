package com.example.newsapp

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.news.repository.NewsRepository
import com.example.news.ui.NewsViewModel

class NewsViewModelProviderFactory (
    private val newsRepository: NewsRepository,
    val app: Application
        ): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository, app) as T
    }
}