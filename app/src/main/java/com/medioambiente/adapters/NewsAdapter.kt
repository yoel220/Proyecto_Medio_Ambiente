package com.medioambiente.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medioambiente.R
import com.medioambiente.activities.NewsDetailsActivity
import com.medioambiente.models.News

class NewsAdapter(private val newsList: List<News>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.newsImageView)
        val titleTextView: TextView = itemView.findViewById(R.id.newsTitleTextView)
        val summaryTextView: TextView = itemView.findViewById(R.id.newsSummaryTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]

        Log.d("NewsAdapter", "Cargando imagen desde URL: ${news.imagen}")

        if (news.imagen.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(news.imagen)
                .placeholder(R.drawable.ic_placeholder_image)
                .error(R.drawable.ic_error_image)
                .into(holder.imageView)
        } else {
            holder.imageView.setImageResource(R.drawable.ic_placeholder_image)
        }

        holder.titleTextView.text = news.titulo
        holder.summaryTextView.text = news.resumen

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, NewsDetailsActivity::class.java).apply {
                putExtra("news_object", news)
            }
            holder.itemView.context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return newsList.size
    }
}