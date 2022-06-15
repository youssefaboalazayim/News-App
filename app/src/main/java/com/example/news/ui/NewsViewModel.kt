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
import com.example.news.R
import com.example.news.models.Article
import com.example.news.models.NewsResponse
import com.example.news.repository.NewsRepository
import com.example.news.util.MyApplicationContext
import com.example.news.util.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    private val newsRepository: NewsRepository,
    val app: Application
) :  AndroidViewModel(app) {

    val breakingNews by lazy { MutableStateFlow<Resource<NewsResponse>>(Resource.Loading()) }
    val searchNews by lazy { MutableStateFlow<Resource<NewsResponse>>(Resource.Loading()) }

    var newsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    // Breaking News
     fun getBreakingNews(countryCode: String) =
        viewModelScope.launch {
            safeBreakingNewsCall(countryCode)

        }


    private suspend fun handleBreakingNewsResponse(response: Flow<NewsResponse>){
        response.catch { breakingNews.value =   Resource.Error(it.message?: "")}
            .collect {
                    newsPage++
                    if(breakingNewsResponse==null){
                        breakingNewsResponse = it
                    }
                    else{
                        val oldArticles = breakingNewsResponse?.articles
                        val newArticles = it.articles
                        oldArticles?.addAll(newArticles)
                    }
                breakingNews.value = Resource.Success(breakingNewsResponse ?: it)
        }
    }

    // Search News

    fun getSearchNews(searchQuery: String) =
        viewModelScope.launch {
            safeSearchCall(searchQuery)
        }

    private suspend fun handleSearchNewsResponse(response: Flow<NewsResponse>){
        response.catch { searchNews.value =  Resource.Error(it.message?: "")}
            .collect {
                newsPage++
                if(searchNewsResponse==null){
                    searchNewsResponse = it
                }
                else{
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = it.articles
                    oldArticles?.addAll(newArticles)
                }
                searchNews.value = Resource.Success(searchNewsResponse ?: it)
            }
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
        breakingNews.value = (Resource.Loading())
       try {
           if (hasInternetConnection()){
               val response  = newsRepository.getBreakingNews(countryCode, newsPage)
               handleBreakingNewsResponse(response)
           }
           else{
               breakingNews.value = Resource.Error(NewsApplication.appContext.getString(R.string.No_Internet_Connection))
           }

       }
       catch (t: Throwable){
           when(t){
               is IOException -> breakingNews.value = Resource.Error(NewsApplication.appContext.getString(R.string.Network_Failure))
               else -> breakingNews.value = Resource.Error(NewsApplication.appContext.getString(R.string.Conversion_Error))
           }
       }
    }

    private suspend fun safeSearchCall(searchQuery: String){
        searchNews.value = (Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response  = newsRepository.searchNews(searchQuery, searchNewsPage)
                (handleSearchNewsResponse(response))
            }
            else{
                searchNews.value=  Resource.Error(NewsApplication.appContext.getString(R.string.No_Internet_Connection))
            }

        }
        catch (t: Throwable){
            when(t){
                is IOException -> searchNews.value = Resource.Error(NewsApplication.appContext.getString(R.string.Network_Failure))
                else -> searchNews.value = Resource.Error(NewsApplication.appContext.getString(R.string.Conversion_Error))
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