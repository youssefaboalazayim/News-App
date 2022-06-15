package com.example.news.ui.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.news.R
import com.example.news.models.Article
import com.example.news.ui.MainActivity
import com.example.news.ui.NewsViewModel
import com.example.news.util.MyApplicationContext
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class ArticleFragment : Fragment() {

    lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?= inflater.inflate(R.layout.fragment_article, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val article = arguments?.getSerializable("article") as Article
        val webView = view.findViewById<WebView>(R.id.webView)
        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url?: "")
        }

        saveArticle(article, view)
    }

    fun saveArticle(article: Article, view: View){
        val saveArticle = view.findViewById<FloatingActionButton>(R.id.fab)
        saveArticle?.setOnClickListener{
            viewModel.saveArticle(article)
            Snackbar.make(view, requireContext().getString(R.string.Article_Saved_uccessfly), Snackbar.LENGTH_LONG).show()
        }
    }




}