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
import com.example.news.ui.MainActivity
import com.example.news.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class ArticleFragment : Fragment() {

    lateinit var viewModel: NewsViewModel
    //val args: ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?= inflater.inflate(R.layout.fragment_article, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
//        val article = args.articllllle
//        val webView = view.findViewById<WebView>(R.id.webView)
//        webView.apply {
//            webViewClient = WebViewClient()
//            loadUrl(article.urrrrll)
//        }
//
//        saveArticle()
    }

//    fun saveArticle(){
//        val saveArticle = view?.findViewById<WebView>(R.id.fab)
//        saveArticle?.setOnClickListener{
//            viewModel.saveArticle(article)
//            Snackbar.make(view, "Artical Saved Successfly", Snackbar.LENGTH_LONG).show()
//        }
//    }




}