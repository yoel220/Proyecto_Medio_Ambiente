package com.medioambiente.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.medioambiente.R
import com.medioambiente.databinding.ActivityAreaDetailBinding
import com.medioambiente.models.AreaProtegida
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

class AreaDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAreaDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAreaDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Configuration.getInstance().load(applicationContext, getSharedPreferences("osmdroid", MODE_PRIVATE))

        val area = intent.getParcelableExtra<AreaProtegida>("area_protegida")

        if (area != null) {
            setupViews(area)
            setupMap(area)
        } else {
            Toast.makeText(this, "No se encontró información del área.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (::binding.isInitialized) {
            binding.mapaArea.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::binding.isInitialized) {
            binding.mapaArea.onPause()
        }
    }

    private fun setupViews(area: AreaProtegida) {
        binding.areaNombreDetail.text = area.nombre.ifEmpty { "Nombre no disponible" }
        binding.areaTipoDetail.text = "Tipo: ${area.tipo.ifEmpty { "No disponible" }}"
        binding.areaDescripcion.text = area.descripcion.ifEmpty { "Descripción no disponible" }


        binding.areaUbicacion.text = "Ubicación: ${area.provincia.ifEmpty { "No disponible" }}"
        binding.areaSuperficie.text = "Superficie: ${area.extension.ifEmpty { "No disponible" }}"


        binding.areaLatitud.text = "Latitud: ${area.latitud}"
        binding.areaLongitud.text = "Longitud: ${area.longitud}"

        Glide.with(this)
            .load(area.imagen)
            .placeholder(R.drawable.placeholder_area)
            .error(R.drawable.ic_error_image)
            .into(binding.areaImagenFull)
    }

    private fun setupMap(area: AreaProtegida) {
        if (area.latitud != 0.0 && area.longitud != 0.0) {
            binding.mapaArea.visibility = View.VISIBLE
            binding.mapaArea.setTileSource(TileSourceFactory.MAPNIK)
            binding.mapaArea.setMultiTouchControls(true)

            val location = GeoPoint(area.latitud, area.longitud)

            binding.mapaArea.controller.setZoom(14.0)
            binding.mapaArea.controller.setCenter(location)

            val marcador = Marker(binding.mapaArea)
            marcador.position = location
            marcador.title = area.nombre
            marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            binding.mapaArea.overlays.add(marcador)
            binding.mapaArea.invalidate()
        } else {
            binding.mapaArea.visibility = View.GONE
        }
    }
}