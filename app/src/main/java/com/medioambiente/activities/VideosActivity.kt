package com.medioambiente.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.medioambiente.R
import com.medioambiente.adapters.VideosAdapter
import com.medioambiente.models.Video
import com.medioambiente.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var videosAdapter: VideosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videos)

        recyclerView = findViewById(R.id.recyclerViewVideos)
        progressBar = findViewById(R.id.progressBar)
        errorTextView = findViewById(R.id.errorTextView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchVideos()
    }

    private fun fetchVideos() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        errorTextView.visibility = View.GONE


        ApiClient.apiServiceInstance.getVideos().enqueue(object : Callback<List<Video>> {
            override fun onResponse(call: Call<List<Video>>, response: Response<List<Video>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val videos = response.body()
                    if (videos != null && videos.isNotEmpty()) {
                        videosAdapter = VideosAdapter(videos) { videoUrl ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                            startActivity(intent)
                        }
                        recyclerView.adapter = videosAdapter
                        recyclerView.visibility = View.VISIBLE
                    } else {
                        errorTextView.text = "No se encontraron videos."
                        errorTextView.visibility = View.VISIBLE
                    }
                } else {
                    errorTextView.text = "Error en la respuesta: ${response.code()}"
                    errorTextView.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<List<Video>>, t: Throwable) {
                progressBar.visibility = View.GONE
                errorTextView.text = "Error de conexión: ${t.message}"
                errorTextView.visibility = View.VISIBLE
            }
        })
    }
}