package com.medioambiente.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.medioambiente.R
import com.medioambiente.databinding.ActivityNewsDetailBinding
import com.medioambiente.models.News
import android.util.Log
import android.view.View
import com.medioambiente.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class NewsDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val newsObject = intent.getParcelableExtra<News>("news_object")

        if (newsObject != null) {
            setupViews(newsObject)
        } else {
            Toast.makeText(this, "Noticia no válida. Vuelve a intentarlo.", Toast.LENGTH_SHORT)
                .show()
            finish()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Detalles de la Noticia"
    }

    private fun setupViews(news: News) {
        binding.newsTitleTextView.text = news.titulo
        binding.newsDateTextView.text = news.fecha
        binding.newsContentTextView.text = news.contenido
        binding.newsIdTextView.text = "ID: ${news.id}"
        Glide.with(this)
            .load(news.imagen)
            .placeholder(R.drawable.ic_placeholder_image)
            .error(R.drawable.ic_error_image)
            .into(binding.newsImageView)
    }
}