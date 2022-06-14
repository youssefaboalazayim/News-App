package com.example.news.ui.search

import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.QUERY_PAGE_SIZE
import com.example.news.R
import com.example.news.SEARCH_NEWS_TIME_DELAY
import com.example.news.adapter.NewsAdapter
import com.example.news.ui.MainActivity
import com.example.news.ui.NewsViewModel
import com.example.news.util.PaginationListener
import com.example.news.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_favorites_news.*
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment() {

//    private var _binding: FragmentNotificationsBinding? = null
//    private val binding get() = _binding!!

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    val TAG = "SearchNewsFragment"

    var isLoading = false
    var isLastPage = false

    var paginationListener: PaginationListener = object : PaginationListener(){
        override fun loadMore() {
            if(!isLoading && !isLastPage) {
                viewModel.getSearchNews(etSearch.text.toString())
            }
        }
        override fun cantLoadMore() {
            rvSearchNews.setPadding(0, 0, 0, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?= inflater.inflate(R.layout.fragment_search_news, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initRecyclerview()
        initEditText()
        click()

    }

    private fun click(){
        newsAdapter.setItemListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchFragment_to_articleFragment3, bundle
            )
        }
    }

    fun initEditText(){
        val editText = view?.findViewById<EditText>(R.id.etSearch)
        var job: Job? = null
        editText?.addTextChangedListener{
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                it.let {
                    if (it.toString().isNotEmpty()){
                        viewModel.getSearchNews(it.toString())
                    }
                }
            }

        }
    }


    private fun initRecyclerview(){
        val rv = view?.findViewById<RecyclerView>(R.id.rvSearchNews)
        newsAdapter = NewsAdapter()
        rv?.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.paginationListener)
        }
    }


    private fun initViewModel() {
        viewModel = (activity as MainActivity).viewModel
        viewModel.searchNewsLiveData.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Success ->{
                    hideProgressbar()
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                        val totalPages = it.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.searchNewsPage == totalPages
                    }

                }
                is Resource.Error-> {
                    showProgressbar()
                    response.message?.let {
                        Toast.makeText(activity, "An error occured: $it", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading->{showProgressbar()}
            }
        })
    }

    private fun hideProgressbar(){
        val progressBar = view?.findViewById<ProgressBar>(R.id.paginationProgressBarSearch)
        progressBar?.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressbar(){
        val progressBar = view?.findViewById<ProgressBar>(R.id.paginationProgressBarSearch)
        progressBar?.visibility = View.GONE
        isLoading = true
    }

}