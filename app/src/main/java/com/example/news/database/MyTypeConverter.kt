package com.example.news.database

import androidx.room.TypeConverter
import com.example.news.models.Source


class MyTypeConverter {

    @TypeConverter
    fun fromDataToString(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun fromStringToData(name: String): Source {
        return Source(name, name )
    }
}