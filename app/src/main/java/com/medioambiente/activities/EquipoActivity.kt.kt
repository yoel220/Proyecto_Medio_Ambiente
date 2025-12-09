package com.medioambiente.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.medioambiente.R
import com.medioambiente.adapters.EquipoAdapter
import com.medioambiente.models.Miembro
import com.medioambiente.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EquipoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var equipoAdapter: EquipoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipo)

        recyclerView = findViewById(R.id.recyclerViewEquipo)
        progressBar = findViewById(R.id.progressBar)
        errorTextView = findViewById(R.id.errorTextView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchEquipo()
    }

    private fun fetchEquipo() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        errorTextView.visibility = View.GONE


        ApiClient.apiServiceInstance.getEquipo().enqueue(object : Callback<List<Miembro>> {
            override fun onResponse(call: Call<List<Miembro>>, response: Response<List<Miembro>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val equipo = response.body()
                    if (equipo != null && equipo.isNotEmpty()) {
                        equipoAdapter = EquipoAdapter(equipo)
                        recyclerView.adapter = equipoAdapter
                        recyclerView.visibility = View.VISIBLE
                    } else {
                        errorTextView.text = "No se encontró información del equipo."
                        errorTextView.visibility = View.VISIBLE
                    }
                } else {
                    errorTextView.text = "Error en la respuesta: ${response.code()}"
                    errorTextView.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<List<Miembro>>, t: Throwable) {
                progressBar.visibility = View.GONE
                errorTextView.text = "Error de conexión: ${t.message}"
                errorTextView.visibility = View.VISIBLE
            }
        })
    }
}