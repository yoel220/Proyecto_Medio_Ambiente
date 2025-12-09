package com.medioambiente.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.medioambiente.R
import com.medioambiente.adapters.NormativasAdapter
import com.medioambiente.models.Normativa
import com.medioambiente.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NormativasActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NormativasAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normativas)

        recyclerView = findViewById(R.id.recyclerViewNormativas)
        progressBar = findViewById(R.id.progressBar)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchNormativas()
    }

    private fun fetchNormativas() {
        progressBar.visibility = View.VISIBLE

        val call = ApiClient.apiServiceInstance.getNormativas()

        call.enqueue(object : Callback<List<Normativa>> {
            override fun onResponse(call: Call<List<Normativa>>, response: Response<List<Normativa>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val normativas = response.body()
                    if (normativas != null && normativas.isNotEmpty()) {
                        adapter = NormativasAdapter(normativas)
                        recyclerView.adapter = adapter
                        recyclerView.visibility = View.VISIBLE
                    } else {
                        val message = "No se encontraron normativas."
                        Toast.makeText(this@NormativasActivity, message, Toast.LENGTH_SHORT).show()

                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = if (errorBody.isNullOrEmpty()) {
                        "Error en la respuesta: ${response.code()}"
                    } else {
                        "Error: $errorBody"
                    }
                    Toast.makeText(this@NormativasActivity, errorMessage, Toast.LENGTH_LONG).show()
                    Log.e("NormativasActivity", "Error de la API: $errorMessage")
                }
            }

            override fun onFailure(call: Call<List<Normativa>>, t: Throwable) {
                progressBar.visibility = View.GONE
                val errorMessage = "Ocurrió un error: ${t.message}"
                Toast.makeText(this@NormativasActivity, errorMessage, Toast.LENGTH_LONG).show()
                Log.e("NormativasActivity", "Excepción: ${t.message}", t)
            }
        })
    }
}