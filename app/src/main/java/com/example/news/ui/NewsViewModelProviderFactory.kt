package com.example.newsapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.news.repository.NewsRepository
import com.example.news.ui.NewsViewModel

class NewsViewModelProviderFactory (
    val newsRepository: NewsRepository
        ): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository) as T
    }
}