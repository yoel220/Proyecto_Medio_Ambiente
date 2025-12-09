package com.medioambiente.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.medioambiente.R
import com.medioambiente.models.Reporte
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

class MapaReportesActivity : AppCompatActivity() {

    private lateinit var mapa: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(applicationContext, getSharedPreferences("osmdroid", MODE_PRIVATE))

        setContentView(R.layout.activity_mapa_reportes)

        mapa = findViewById(R.id.mapa)
        mapa.setTileSource(TileSourceFactory.MAPNIK)
        mapa.setMultiTouchControls(true)
        val rdCenterPoint = GeoPoint(18.7357, -70.1627)
        mapa.controller.setZoom(8.0)
        mapa.controller.setCenter(rdCenterPoint)
        obtenerReportesYAgregarMarcadores()
    }

    override fun onResume() {
        super.onResume()
        mapa.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapa.onPause()
    }

    private fun obtenerReportesYAgregarMarcadores() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                val token = sharedPref.getString("auth_token", "") ?: ""

                val response = ApiClient.apiServiceInstance.getMisReportes("Bearer $token")

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val reportes = response.body() ?: emptyList<Reporte>()
                        agregarMarcadores(reportes)
                    } else {
                        val errorMessage = "Error al obtener los reportes: ${response.code()}"
                        Log.e("MapaReportesActivity", errorMessage)
                        Toast.makeText(this@MapaReportesActivity, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("MapaReportesActivity", "Excepción: ${e.message}", e)
                    Toast.makeText(this@MapaReportesActivity, "Ocurrió un error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun agregarMarcadores(reportes: List<Reporte>) {
        val reportesConUbicacion = reportes.filter { it.latitud != null && it.longitud != null }

        if (reportesConUbicacion.isNotEmpty()) {
            val primeraUbicacion = GeoPoint(reportesConUbicacion.first().latitud!!, -reportesConUbicacion.first().longitud!!)
            mapa.controller.animateTo(primeraUbicacion)
        }

        for (reporte in reportesConUbicacion) {
            val ubicacion = GeoPoint(reporte.latitud!!, -reporte.longitud!!)
            val marcador = Marker(mapa)
            marcador.position = ubicacion
            marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marcador.infoWindow = null

            marcador.setOnMarkerClickListener { _, _ ->
                reporte.id?.let { reporteId ->
                    val intent = Intent(this@MapaReportesActivity, ReportDetailsActivity::class.java)
                    intent.putExtra("reporte_id", reporteId)
                    startActivity(intent)
                } ?: run {
                    Toast.makeText(this@MapaReportesActivity, "Error: El ID del reporte no es válido.", Toast.LENGTH_SHORT).show()
                }
                true
            }
            mapa.overlays.add(marcador)
        }
        mapa.invalidate()
    }
}