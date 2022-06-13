package com.example.news.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.adapter.NewsAdapter
import com.example.news.ui.MainActivity
import com.example.news.ui.NewsViewModel

class FavoritesNewsFragment : Fragment() {

//    private var _binding: FragmentDashboardBinding? = null
//    private val binding get() = _binding!!

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?= inflater.inflate(R.layout.fragment_favorites_news, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        initRecyclerview()
        click()
    }

    private fun click(){
        newsAdapter.setItemListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_navigation_favoritesNews_to_articleFragment3, bundle
            )
        }
    }

    private fun initRecyclerview(){
        val rv = view?.findViewById<RecyclerView>(R.id.rvSavedNews)
        newsAdapter = NewsAdapter()
        rv?.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}