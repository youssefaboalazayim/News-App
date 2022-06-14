package com.example.news.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.news.NewsApplication
import com.example.news.models.Article
import com.example.news.models.NewsResponse
import com.example.news.repository.NewsRepository
import com.example.news.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    private val newsRepository: NewsRepository,
    val app: Application
) :  AndroidViewModel(app) {

    val breakingNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var newsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("us")
    }
    // Breaking News
     fun getBreakingNews(countryCode: String) =
        viewModelScope.launch {
            safeBreakingNewsCall(countryCode)

        }


    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {
                newsPage++
                if(breakingNewsResponse==null){
                    breakingNewsResponse = it
                }
                else{
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = it.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    // Search News



    fun getSearchNews(searchQuery: String) =
        viewModelScope.launch {
            safeSearchCall(searchQuery)
        }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if(searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
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

    private suspend fun safeBreakingNewsCall(countryCode: String){
        breakingNewsLiveData.postValue(Resource.Loading())
       try {
           if (hasInternetConnection()){
               val response  = newsRepository.getBreakingNews(countryCode, newsPage)
               breakingNewsLiveData.postValue(handleBreakingNewsResponse(response))
           }
           else{
               breakingNewsLiveData.postValue(Resource.Error("No Internet Connection"))
           }

       }
       catch (t: Throwable){
           when(t){
               is IOException -> breakingNewsLiveData.postValue(Resource.Error("Network Failure"))
               else -> breakingNewsLiveData.postValue(Resource.Error("Conversion Error"))
           }
       }
    }

    private suspend fun safeSearchCall(searchQuery: String){
        searchNewsLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response  = newsRepository.searchNews(searchQuery, searchNewsPage)
                searchNewsLiveData.postValue(handleSearchNewsResponse(response))
            }
            else{
                searchNewsLiveData.postValue(Resource.Error("No Internet Connection"))
            }

        }
        catch (t: Throwable){
            when(t){
                is IOException -> searchNewsLiveData.postValue(Resource.Error("Network Failure"))
                else -> searchNewsLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun hasInternetConnection(): Boolean{
        val connectManager = getApplication<NewsApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectManager.activeNetwork?: return false
            val capabilities = connectManager.getNetworkCapabilities(activeNetwork)?: return false
            return when{
                capabilities.hasTransport(TRANSPORT_WIFI)-> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) ->true
                capabilities.hasTransport(TRANSPORT_ETHERNET) ->true
                else -> false
            }
        }
        else{
            connectManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }

        return false
    }


}