package com.example.news.ui.breakingnews

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.adapter.NewsAdapter
import com.example.news.ui.MainActivity
import com.example.news.ui.NewsViewModel
import com.example.news.util.Resource


class BreakingNewsFragment : Fragment() {

//    private var _binding: FragmentHomeBinding? = null
//    private val binding get() = _binding!!

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    val TAG = "BreakingNewsFragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?= inflater.inflate(R.layout.fragment_breaking_news, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initRecyclerview()
        click()
    }

    private fun initRecyclerview(){
        val rv = view?.findViewById<RecyclerView>(R.id.rvNews)
        newsAdapter = NewsAdapter()
        rv?.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun initViewModel() {
        viewModel = (activity as MainActivity).viewModel
        viewModel.breakingNewsLiveData.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success ->{
                    hideProgressbar()
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles)
                    }

                }
                is Resource.Error-> {
                    showProgressbar()
                    response.message?.let {
                        Log.e(TAG, "An error occured: $it")
                    }
                }
                is Resource.Loading->{showProgressbar()}
            }
        })
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
    }

    private fun showProgressbar(){
        val progressBar = view?.findViewById<ProgressBar>(R.id.paginationProgressBarBreaking)
        progressBar?.visibility = View.GONE
    }


}