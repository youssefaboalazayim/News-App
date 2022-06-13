package com.example.news.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.models.Article
import com.example.news.models.NewsResponse
import com.example.news.repository.NewsRepository
import com.example.news.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository
) :  ViewModel() {

    val breakingNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var newsPage = 1

    val searchgNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1

    init {
        getBreakingNews("us")
    }
    // Breaking News
    fun getBreakingNews(countryCode: String) =
        viewModelScope.launch {
            breakingNewsLiveData.postValue(Resource.Loading())
            val response  = newsRepository.getBreakingNews(countryCode, newsPage)
            breakingNewsLiveData.postValue(handleBreakingNewsResponse(response))
        }


    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    // Search News



    fun getSearchNews(searchQuery: String) =
        viewModelScope.launch {
            searchgNewsLiveData.postValue(Resource.Loading())
            val response  = newsRepository.searchNews(searchQuery, newsPage)
            searchgNewsLiveData.postValue(handleSearchNewsResponse(response))
        }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article){
        viewModelScope.launch {
            newsRepository.insert(article)
        }
    }

    fun getSaveNews()=newsRepository.getSavedNews()

    fun deleteArticle(article: Article){
        viewModelScope.launch {
            newsRepository.deleteArticle(article)
        }
    }


}