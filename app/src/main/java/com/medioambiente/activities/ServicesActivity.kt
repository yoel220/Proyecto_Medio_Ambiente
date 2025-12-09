package com.medioambiente.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.medioambiente.R
import com.medioambiente.adapters.ServicesAdapter
import com.medioambiente.models.Servicio
import com.medioambiente.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log
import retrofit2.Response


class ServicesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var searchEditText: EditText
    private lateinit var servicesAdapter: ServicesAdapter

    private val allServicesList = mutableListOf<Servicio>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)

        recyclerView = findViewById(R.id.recyclerViewServices)
        progressBar = findViewById(R.id.progressBar)
        errorTextView = findViewById(R.id.errorTextView)
        searchEditText = findViewById(R.id.searchServiceEditText)

        recyclerView.layoutManager = LinearLayoutManager(this)

        setupSearch()
        fetchServices()
    }

    private fun setupSearch() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterServices(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterServices(query: String) {
        val filteredList = if (query.isEmpty()) {
            allServicesList
        } else {
            allServicesList.filter { servicio ->
                servicio.id.toString().contains(query, ignoreCase = true)
            }
        }
        servicesAdapter.updateData(filteredList)
    }

    private fun fetchServices() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        errorTextView.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: Response<List<Servicio>> = ApiClient.apiServiceInstance.getServicios()

                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val services = response.body()
                        if (services != null && services.isNotEmpty()) {
                            allServicesList.clear()
                            allServicesList.addAll(services)
                            servicesAdapter = ServicesAdapter(allServicesList)
                            recyclerView.adapter = servicesAdapter
                            recyclerView.visibility = View.VISIBLE
                        } else {
                            errorTextView.text = "No se encontraron servicios."
                            errorTextView.visibility = View.VISIBLE
                        }
                    } else {
                        errorTextView.text = "Error en la respuesta: ${response.code()}"
                        errorTextView.visibility = View.VISIBLE
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    errorTextView.text = "Error de conexión: ${e.message}"
                    errorTextView.visibility = View.VISIBLE
                    Log.e("ServicesActivity", "Error fetching services: ${e.message}")
                    Toast.makeText(this@ServicesActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}