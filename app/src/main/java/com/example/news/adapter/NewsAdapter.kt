package com.example.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.models.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.article_row,
                parent,
                false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {

            val articleImage = findViewById<ImageView>(R.id.articleImage)
            val source = findViewById<TextView>(R.id.source)
            val title = findViewById<TextView>(R.id.title)
            val description = findViewById<TextView>(R.id.description)
            val publishedAt = findViewById<TextView>(R.id.publishedAt)

            Glide.with(context).load(article.urlToImage).into(articleImage)
            source.text = article.source?.name
            title.text = article.title
            description.text = article.description
            publishedAt.text = article.publishedAt

            setOnClickListener {
                itemListener?.let { it(article) }
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var itemListener: ((Article)-> Unit)? = null
    fun setItemListener(listener: (Article)-> Unit){
        itemListener = listener
    }
}