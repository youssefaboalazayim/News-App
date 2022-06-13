package com.example.news.api

import com.example.news.models.NewsResponse
import retrofit2.Response


class ArticleRemoteDataSource {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<NewsResponse> {
        val instance = RetrofitInstance.retrofit.create(ArticlesEndpoint::class.java)
        return instance.getBreakingNews(countryCode, pageNumber)
    }

    suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse> {
        val instance = RetrofitInstance.retrofit.create(ArticlesEndpoint::class.java)
        return instance.newsSearch(searchQuery, pageNumber)
    }
}