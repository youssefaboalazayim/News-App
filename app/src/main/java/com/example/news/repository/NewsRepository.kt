package com.example.news.repository

import com.example.news.api.ArticleRemoteDataSource
import com.example.news.database.ArticleDataBase
import com.example.news.models.Article
import com.example.news.models.NewsResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class NewsRepository(
    val myDataBase: ArticleDataBase
) {


    private val articleRemoteDataSource: ArticleRemoteDataSource = ArticleRemoteDataSource()

     fun getBreakingNews(countryCode: String, pageNumber: Int): Flow<NewsResponse> {
        return articleRemoteDataSource.getBreakingNews(countryCode, pageNumber)
    }

     fun searchNews(searchQuery: String, pageNumber: Int): Flow<NewsResponse>{
        return articleRemoteDataSource.searchNews(searchQuery, pageNumber)
    }

    suspend fun insert(article: Article) = myDataBase.getDao().insertPost(article)

    fun getSavedNews()= myDataBase.getDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = myDataBase.getDao().deleteArticle(article)


}