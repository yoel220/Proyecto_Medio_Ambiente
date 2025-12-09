package com.medioambiente.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.medioambiente.adapters.AreasProtegidasAdapter
import com.medioambiente.databinding.ActivityAreasProtegidasBinding
import com.medioambiente.models.AreaProtegida
import com.medioambiente.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AreasProtegidasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAreasProtegidasBinding
    private lateinit var areasAdapter: AreasProtegidasAdapter
    private val allAreasList = mutableListOf<AreaProtegida>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAreasProtegidasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearchAndFilter()
        fetchAreasProtegidas()
    }

    private fun setupRecyclerView() {
        areasAdapter = AreasProtegidasAdapter(emptyList()) { area ->
            val intent = Intent(this, AreaDetailActivity::class.java)
            intent.putExtra("area_protegida", area)
            startActivity(intent)
        }
        binding.rvAreasProtegidas.layoutManager = LinearLayoutManager(this)
        binding.rvAreasProtegidas.adapter = areasAdapter
    }

    private fun setupSearchAndFilter() {
        binding.etSearch.addTextChangedListener { text ->
            filterAreas(text.toString())
        }

        binding.btnFilter.setOnClickListener {
            showFilterDialog()
        }
    }

    private fun filterAreas(query: String) {
        val filteredList = allAreasList.filter { area ->
            area.nombre.contains(query, ignoreCase = true) ||
                    area.provincia.contains(query, ignoreCase = true) ||
                    area.tipo.contains(query, ignoreCase = true)
        }
        areasAdapter.updateData(filteredList)
    }

    private fun fetchAreasProtegidas() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.apiServiceInstance.getAreasProtegidas()
                val areas = response.body() ?: emptyList()

                withContext(Dispatchers.Main) {
                    if (areas.isEmpty()) {
                        binding.rvAreasProtegidas.visibility = View.GONE
                        binding.emptyStateText.visibility = View.VISIBLE
                        Toast.makeText(this@AreasProtegidasActivity, "No se encontraron áreas protegidas.", Toast.LENGTH_SHORT).show()
                    } else {
                        binding.rvAreasProtegidas.visibility = View.VISIBLE
                        binding.emptyStateText.visibility = View.GONE

                        allAreasList.clear()
                        allAreasList.addAll(areas)
                        areasAdapter.updateData(areas)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("AreasProtegidas", "Error fetching areas: ${e.message}")
                    Toast.makeText(this@AreasProtegidasActivity, "Error de conexión.", Toast.LENGTH_SHORT).show()

                    binding.rvAreasProtegidas.visibility = View.GONE
                    binding.emptyStateText.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showFilterDialog() {
        val types = allAreasList.map { it.tipo }.distinct().sorted()
        val provinces = allAreasList.map { it.provincia }.distinct().sorted()

        val filterOptions = mutableListOf<String>()
        filterOptions.add("Todos")
        filterOptions.add("--- Tipos ---")
        filterOptions.addAll(types)
        filterOptions.add("--- Provincias ---")
        filterOptions.addAll(provinces)

        val optionsArray = filterOptions.toTypedArray()

        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Filtrar por")
            .setItems(optionsArray) { dialog, which ->
                val selectedOption = optionsArray[which]
                when {
                    selectedOption == "Todos" -> areasAdapter.updateData(allAreasList)
                    selectedOption.startsWith("---") -> { /* No hacer nada si se selecciona una de las cabeceras */ }
                    types.contains(selectedOption) -> {
                        val filteredList = allAreasList.filter { it.tipo == selectedOption }
                        areasAdapter.updateData(filteredList)
                    }
                    provinces.contains(selectedOption) -> {
                        val filteredList = allAreasList.filter { it.provincia == selectedOption }
                        areasAdapter.updateData(filteredList)
                    }
                }
                Toast.makeText(this, "Filtro aplicado: $selectedOption", Toast.LENGTH_SHORT).show()
            }
        builder.create().show()
    }
}