package com.medioambiente.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.medioambiente.R
import com.medioambiente.adapters.NewsAdapter
import com.medioambiente.models.News
import com.medioambiente.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        recyclerView = findViewById(R.id.recyclerViewNews)
        progressBar = findViewById(R.id.progressBar)
        errorTextView = findViewById(R.id.errorTextView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchNews()
    }

    private fun fetchNews() {
        progressBar.visibility = View.VISIBLE
        errorTextView.visibility = View.GONE
        recyclerView.visibility = View.GONE


        ApiClient.apiServiceInstance.getNoticias().enqueue(object : Callback<List<News>> {
            override fun onResponse(call: Call<List<News>>, response: Response<List<News>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val newsList = response.body()
                    if (newsList != null && newsList.isNotEmpty()) {
                        newsAdapter = NewsAdapter(newsList)
                        recyclerView.adapter = newsAdapter
                        recyclerView.visibility = View.VISIBLE
                    } else {
                        errorTextView.text = "No se encontraron noticias."
                        errorTextView.visibility = View.VISIBLE
                    }
                } else {
                    errorTextView.text = "Error en la respuesta: ${response.code()}"
                    errorTextView.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<List<News>>, t: Throwable) {
                progressBar.visibility = View.GONE
                errorTextView.text = "Error de conexión: ${t.message}"
                errorTextView.visibility = View.VISIBLE
            }
        })
    }
}