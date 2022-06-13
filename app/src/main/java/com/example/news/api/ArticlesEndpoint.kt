package com.example.news.api

import com.example.news.API_KEY
import com.example.news.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticlesEndpoint {

    @GET("/v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber: Int  = 1,
        @Query("apikey")
        apiKey: String = API_KEY
    ):Response<NewsResponse>

    @GET("/v2/everything")
    suspend fun newsSearch(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int  = 1,
        @Query("apikey")
        apiKey: String = API_KEY
    ):Response<NewsResponse>
}