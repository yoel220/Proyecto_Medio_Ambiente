package com.medioambiente.activities

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.medioambiente.adapters.ReporteAdapter
import com.medioambiente.databinding.ActivityMisReportesBinding
import com.medioambiente.models.Reporte
import com.medioambiente.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MisReportesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMisReportesBinding
    private lateinit var reportsAdapter: ReporteAdapter
    private val allReportsList = mutableListOf<Reporte>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMisReportesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearch()
        obtenerMisReportes()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Mis Reportes"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupRecyclerView() {
        reportsAdapter = ReporteAdapter(this, emptyList())
        binding.misReportesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MisReportesActivity)
            adapter = reportsAdapter
        }
    }

    private fun setupSearch() {
        binding.searchReportEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterReports(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterReports(query: String) {
        val filteredList = if (query.isEmpty()) {
            allReportsList
        } else {
            allReportsList.filter { reporte ->
                reporte.id?.toString()?.contains(query, ignoreCase = true) ?: false
            }
        }
        reportsAdapter.updateData(filteredList)
        if (filteredList.isEmpty()) {
            binding.emptyListMessage.text = "No se encontraron reportes con ese ID."
            binding.emptyListMessage.visibility = View.VISIBLE
        } else {
            binding.emptyListMessage.visibility = View.GONE
        }
    }

    private fun obtenerMisReportes() {
        binding.progressBar.visibility = View.VISIBLE
        binding.emptyListMessage.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                val token = sharedPref.getString("auth_token", "")

                if (token.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        binding.progressBar.visibility = View.GONE
                        binding.emptyListMessage.text = "Error: Sesión no iniciada."
                        binding.emptyListMessage.visibility = View.VISIBLE
                        Toast.makeText(this@MisReportesActivity, "Error: Sesión no iniciada. Por favor, vuelva a iniciar sesión.", Toast.LENGTH_LONG).show()
                        //finish()
                    }
                    return@launch
                }

                val response = ApiClient.apiServiceInstance.getMisReportes("Bearer $token")

                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val reportes = response.body() ?: emptyList<Reporte>()
                        allReportsList.clear()
                        allReportsList.addAll(reportes)

                        reportsAdapter.updateData(allReportsList)

                        if (reportes.isEmpty()) {
                            binding.emptyListMessage.text = "Aún no tienes reportes realizados."
                            binding.emptyListMessage.visibility = View.VISIBLE
                            binding.misReportesRecyclerView.visibility = View.GONE
                        } else {
                            binding.emptyListMessage.visibility = View.GONE
                            binding.misReportesRecyclerView.visibility = View.VISIBLE
                        }
                    } else {
                        val errorMessage = "Error al obtener los reportes: ${response.code()}"
                        Log.e("MisReportesActivity", errorMessage)
                        binding.emptyListMessage.text = errorMessage
                        binding.emptyListMessage.visibility = View.VISIBLE
                        Toast.makeText(this@MisReportesActivity, errorMessage, Toast.LENGTH_LONG).show()
                        binding.misReportesRecyclerView.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    Log.e("MisReportesActivity", "Excepción: ${e.message}", e)
                    binding.emptyListMessage.text = "Ocurrió un error: ${e.message}"
                    binding.emptyListMessage.visibility = View.VISIBLE
                    Toast.makeText(this@MisReportesActivity, "Ocurrió un error: ${e.message}", Toast.LENGTH_LONG).show()
                    binding.misReportesRecyclerView.visibility = View.GONE
                }
            }
        }
    }
}