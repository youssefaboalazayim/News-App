package com.example.news.ui.breakingnews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.QUERY_PAGE_SIZE
import com.example.news.R
import com.example.news.adapter.NewsAdapter
import com.example.news.ui.MainActivity
import com.example.news.ui.NewsViewModel
import com.example.news.util.PaginationListener
import com.example.news.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class BreakingNewsFragment : Fragment() {


    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    var isLoading = false
    var isLastPage = false
    var paginationListener: PaginationListener = object : PaginationListener(){
        override fun loadMore() {
            if(!isLoading && !isLastPage) {
                viewModel.getBreakingNews("us")
            }
        }
        override fun cantLoadMore() {
            rvNews.setPadding(0, 0, 0, 0)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?= inflater.inflate(R.layout.fragment_breaking_news, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initRecyclerview()
        click()
        viewModel.getBreakingNews("us")
    }

    private fun initRecyclerview(){
        val rv = view?.findViewById<RecyclerView>(R.id.rvNews)
        newsAdapter = NewsAdapter()
        rv?.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.paginationListener)
        }
    }

    private  fun initViewModel() {
        viewModel = (activity as MainActivity).viewModel
        lifecycleScope.launch {
                viewModel.breakingNews.collect {
                        response->
                    when(response){
                        is Resource.Success ->{
                            hideProgressbar()
                            response.data?.let {
                                newsAdapter.differ.submitList(it.articles.toList())
                                val totalPages = it.totalResults / QUERY_PAGE_SIZE + 2
                                isLastPage = viewModel.newsPage == totalPages
                            }
                        }
                        is Resource.Error-> {
                            showProgressbar()
                            response.message?.let {
                                Toast.makeText(activity, String.format(requireContext().getString(R.string.An_error_occured), it) , Toast.LENGTH_LONG).show()
                            }
                        }
                        is Resource.Loading->{showProgressbar()}
                    }
                }
        }

    }

    private fun click(){
        newsAdapter.setItemListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_navigation_breakingNews_to_articleFragment3, bundle
            )
        }
    }

    private fun hideProgressbar(){
        val progressBar = view?.findViewById<ProgressBar>(R.id.paginationProgressBarBreaking)
        progressBar?.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressbar(){
        val progressBar = view?.findViewById<ProgressBar>(R.id.paginationProgressBarBreaking)
        progressBar?.visibility = View.GONE
        isLoading = true
    }
}