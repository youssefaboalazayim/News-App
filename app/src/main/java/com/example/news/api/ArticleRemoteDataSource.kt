package com.example.news.api

import com.example.news.models.NewsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class ArticleRemoteDataSource {
    fun getBreakingNews(countryCode: String, pageNumber: Int): Flow<NewsResponse> {
        val instance = RetrofitInstance.retrofit.create(ArticlesEndpoint::class.java)
        return flow { emit(instance.getBreakingNews(countryCode, pageNumber))  }
    }

     fun searchNews(searchQuery: String, pageNumber: Int): Flow<NewsResponse> {
        val instance = RetrofitInstance.retrofit.create(ArticlesEndpoint::class.java)
        return flow { emit(instance.newsSearch(searchQuery, pageNumber))  }

    }
}