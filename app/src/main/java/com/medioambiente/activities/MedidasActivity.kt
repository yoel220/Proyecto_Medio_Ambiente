package com.medioambiente.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.medioambiente.R
import com.medioambiente.adapters.MedidasAdapter
import com.medioambiente.models.Medida
import com.medioambiente.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MedidasActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var medidasAdapter: MedidasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medidas)

        recyclerView = findViewById(R.id.recyclerViewMedidas)
        progressBar = findViewById(R.id.progressBar)
        errorTextView = findViewById(R.id.errorTextView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchMedidas()
    }

    private fun fetchMedidas() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        errorTextView.visibility = View.GONE


        ApiClient.apiServiceInstance.getMedidas().enqueue(object : Callback<List<Medida>> {
            override fun onResponse(call: Call<List<Medida>>, response: Response<List<Medida>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val medidas = response.body()
                    if (medidas != null && medidas.isNotEmpty()) {
                        medidasAdapter = MedidasAdapter(medidas) { medida ->
                        }
                        recyclerView.adapter = medidasAdapter
                        recyclerView.visibility = View.VISIBLE
                    } else {
                        errorTextView.text = "No se encontraron medidas."
                        errorTextView.visibility = View.VISIBLE
                    }
                } else {
                    errorTextView.text = "Error en la respuesta: ${response.code()}"
                    errorTextView.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<List<Medida>>, t: Throwable) {
                progressBar.visibility = View.GONE
                errorTextView.text = "Error de conexión: ${t.message}"
                errorTextView.visibility = View.VISIBLE
            }
        })
    }
}