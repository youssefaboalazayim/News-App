package com.example.news.repository

import com.example.news.api.ArticleRemoteDataSource
import com.example.news.database.ArticleDataBase
import com.example.news.models.Article
import com.example.news.models.NewsResponse
import retrofit2.Response

class NewsRepository(
    val myDataBase: ArticleDataBase
) {


    private val articleRemoteDataSource: ArticleRemoteDataSource = ArticleRemoteDataSource()

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<NewsResponse>{
        return articleRemoteDataSource.getBreakingNews(countryCode, pageNumber)
    }

    suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse>{
        return articleRemoteDataSource.searchNews(searchQuery, pageNumber)
    }

    suspend fun insert(article: Article) = myDataBase.getDao().insertPost(article)

    fun getSavedNews()= myDataBase.getDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = myDataBase.getDao().deleteArticle(article)


}