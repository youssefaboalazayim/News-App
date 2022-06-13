package com.example.news.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.news.models.Article

@Dao
interface ArticleDao {
    @Query("select * from articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(article: Article): Long

    @Delete
    suspend fun deleteArticle(article: Article)
}