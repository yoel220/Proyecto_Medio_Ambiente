package com.medioambiente.activities

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.medioambiente.R
import com.medioambiente.databinding.ActivityReportDetailsBinding
import com.medioambiente.models.Reporte
import com.medioambiente.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReportDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val reporteId = intent.getStringExtra("reporte_id")

        if (reporteId.isNullOrEmpty()) {
            Toast.makeText(this, "ID de reporte no válido.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        fetchReportDetails(reporteId)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Detalles del Reporte"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun fetchReportDetails(reporteId: String) {
        binding.progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                val token = sharedPref.getString("auth_token", "")

                if (token.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@ReportDetailsActivity, "Error: Sesión no iniciada.", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    return@launch
                }

                val response = ApiClient.apiServiceInstance.getReporteById("Bearer $token", reporteId)

                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val reporte = response.body()
                        if (reporte != null) {
                            setupViews(reporte)
                        } else {
                            Toast.makeText(this@ReportDetailsActivity, "Reporte no encontrado.", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        val errorMessage = "Error al obtener los detalles: ${response.code()}"
                        Log.e("ReportDetailsActivity", errorMessage)
                        Toast.makeText(this@ReportDetailsActivity, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    Log.e("ReportDetailsActivity", "Excepción: ${e.message}", e)
                    Toast.makeText(this@ReportDetailsActivity, "Ocurrió un error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupViews(reporte: Reporte) {
        binding.reportIdTextView.text = "ID: ${reporte.id ?: "No disponible"}"
        binding.reportTitleTextView.text = reporte.titulo?.ifEmpty { "Título no disponible" } ?: "Título no disponible"
        binding.reportCodeTextView.text = "Código: ${reporte.codigo?.ifEmpty { "No disponible" } ?: "No disponible"}"
        binding.reportDescriptionTextView.text = reporte.descripcion?.ifEmpty { "Descripción no disponible" } ?: "Descripción no disponible"
        binding.reportStatusTextView.text = "Estado: ${reporte.estado?.ifEmpty { "No disponible" } ?: "No disponible"}"
        binding.reportDateTextView.text = "Fecha: ${reporte.fecha?.ifEmpty { "No disponible" } ?: "No disponible"}"
        binding.reportLocationTextView.text = "Latitud: ${reporte.latitud}, Longitud: ${reporte.longitud}"

        val comentario = reporte.comentarioMinisterio
        binding.reportMinistryCommentLabel.visibility = View.VISIBLE
        binding.reportMinistryCommentTextView.visibility = View.VISIBLE
        binding.reportMinistryCommentTextView.text = if (comentario.isNullOrEmpty()) {
            "Sin comentarios"
        } else {
            comentario
        }

        if (!reporte.foto.isNullOrEmpty()) {
            decodeBase64AndLoadImage(reporte.foto)
        } else {
            binding.reportImageView.setImageResource(R.drawable.ic_placeholder_image)
        }
    }

    private fun decodeBase64AndLoadImage(base64String: String) {
        try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

            Glide.with(this)
                .load(bitmap)
                .placeholder(R.drawable.ic_placeholder_image)
                .error(R.drawable.ic_error_image)
                .into(binding.reportImageView)

        } catch (e: IllegalArgumentException) {
            Log.e("ReportDetailsActivity", "Error al decodificar Base64: ${e.message}")
            binding.reportImageView.setImageResource(R.drawable.ic_error_image)
        }
    }
}