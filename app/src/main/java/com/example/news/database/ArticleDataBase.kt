package com.example.news.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.news.models.Article

@Database(entities = arrayOf(Article::class), version = 1, exportSchema = false)

@TypeConverters(MyTypeConverter::class)

abstract class ArticleDataBase: RoomDatabase() {
    abstract fun getDao(): ArticleDao

    companion object{
        @Volatile
        private var instance : ArticleDataBase?= null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context)=
            Room.databaseBuilder( context.applicationContext,
                ArticleDataBase::class.java,
                "articles_database"
            ).build()
    }
}

