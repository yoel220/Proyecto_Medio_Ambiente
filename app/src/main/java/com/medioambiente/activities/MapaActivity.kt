package com.medioambiente.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.medioambiente.R
import com.medioambiente.databinding.ActivityMapaBinding
import com.medioambiente.models.AreaProtegida
import com.medioambiente.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import android.content.Intent

class MapaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE))

        binding = ActivityMapaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupMap()
        fetchAreasProtegidas()
    }

    private fun setupMap() {
        binding.map.setTileSource(TileSourceFactory.MAPNIK)
        binding.map.setMultiTouchControls(true)

        val startPoint = GeoPoint(18.7357, -70.1627)
        binding.map.controller.setZoom(8.0)
        binding.map.controller.setCenter(startPoint)
    }

    private fun fetchAreasProtegidas() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.apiServiceInstance.getAreasProtegidas()
                val areas = response.body()

                withContext(Dispatchers.Main) {
                    if (areas.isNullOrEmpty()) {
                        Toast.makeText(this@MapaActivity, "No se encontraron áreas protegidas para mostrar en el mapa.", Toast.LENGTH_LONG).show()
                    } else {
                        addMarkersToMap(areas)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("MapaActivity", "Error al obtener áreas protegidas: ${e.message}")
                    Toast.makeText(this@MapaActivity, "Error de conexión al cargar el mapa.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun addMarkersToMap(areas: List<AreaProtegida>) {
        areas.forEach { area ->
            val location = GeoPoint(area.latitud, area.longitud)
            val marker = Marker(binding.map)
            marker.position = location
            marker.title = area.nombre
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            binding.map.overlays.add(marker)
            marker.setOnMarkerClickListener { marker, mapView ->
                val intent = Intent(this, AreaDetailActivity::class.java)
                intent.putExtra("area_protegida", area)
                startActivity(intent)
                true
            }
        }
        binding.map.invalidate()
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }
}