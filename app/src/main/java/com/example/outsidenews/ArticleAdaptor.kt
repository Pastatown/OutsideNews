package com.example.outsidenews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ArticleAdaptor(private val listener:OnArticleClick) : RecyclerView.Adapter<ArticleAdaptor.ArticleViewHolder>() {

    private val articles: ArrayList<News> = ArrayList()
    class ArticleViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val titleView : TextView = itemView.findViewById(R.id.titre)
        val author : TextView = itemView.findViewById(R.id.auteur)
        val image : ImageView = itemView.findViewById(R.id.image)
        val name : TextView = itemView.findViewById(R.id.name)
        val date : TextView = itemView.findViewById(R.id.date)
        val description : TextView = itemView.findViewById(R.id.description)





    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_article, parent, false)
        val viewHolder = ArticleViewHolder(view)
        view.setOnClickListener {
            listener.onClicked(articles[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currentNews = articles[position]
        holder.titleView.text = currentNews.title
        holder.author.text = currentNews.author

        holder.date.text = currentNews.publishedAt
        holder.description.text = currentNews.description


        Glide.with(holder.itemView.context).load(currentNews.urlToImage).into(holder.image)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun updateNews(updatedNews:ArrayList<News>){
        articles.clear()
        articles.addAll(updatedNews)
        notifyDataSetChanged()
    }
}

interface OnArticleClick{
    fun onClicked(news: News)
}

