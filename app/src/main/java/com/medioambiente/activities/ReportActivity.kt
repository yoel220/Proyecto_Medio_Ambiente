package com.medioambiente.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.medioambiente.R
import com.medioambiente.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class ReportActivity : AppCompatActivity() {

    private lateinit var tituloEditText: TextInputEditText
    private lateinit var descripcionEditText: TextInputEditText
    private lateinit var photoButton: Button
    private lateinit var photoPreview: ImageView
    private lateinit var latitudEditText: TextInputEditText
    private lateinit var longitudEditText: TextInputEditText
    private lateinit var submitReportButton: Button

    private var selectedPhotoBitmap: Bitmap? = null

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(this, "Permiso de cámara denegado.", Toast.LENGTH_SHORT).show()
            }
        }

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                selectedPhotoBitmap = bitmap
                photoPreview.setImageBitmap(bitmap)
                photoPreview.visibility = View.VISIBLE
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        initViews()
        setListeners()
    }

    private fun initViews() {
        tituloEditText = findViewById(R.id.tituloEditText)
        descripcionEditText = findViewById(R.id.descripcionEditText)
        photoButton = findViewById(R.id.photoButton)
        photoPreview = findViewById(R.id.photoPreview)
        latitudEditText = findViewById(R.id.latitudEditText)
        longitudEditText = findViewById(R.id.longitudEditText)
        submitReportButton = findViewById(R.id.submitReportButton)
    }

    private fun setListeners() {
        photoButton.setOnClickListener {
            checkCameraPermissionAndOpenCamera()
        }

        submitReportButton.setOnClickListener {
            submitReport()
        }
    }

    private fun checkCameraPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                Toast.makeText(this, "Necesitamos permiso para usar la cámara para adjuntar una foto del reporte.", Toast.LENGTH_LONG).show()
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            else -> {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun openCamera() {
        takePictureLauncher.launch(null)
    }

    private fun submitReport() {
        val titulo = tituloEditText.text.toString()
        val descripcion = descripcionEditText.text.toString()
        val latitud = latitudEditText.text.toString().toDoubleOrNull()
        val longitud = longitudEditText.text.toString().toDoubleOrNull()
        val fotoBase64 = bitmapToBase64(selectedPhotoBitmap)

        if (titulo.isEmpty() || descripcion.isEmpty() || latitud == null || longitud == null || fotoBase64.isNullOrEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos y adjunte una foto.", Toast.LENGTH_LONG).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                val token = sharedPref.getString("auth_token", "") ?: ""

                if (token.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ReportActivity, "Error: Token de autorización no encontrado.", Toast.LENGTH_LONG).show()
                    }
                    return@launch
                }

                val tituloBody = titulo.toRequestBody("text/plain".toMediaTypeOrNull())
                val descripcionBody = descripcion.toRequestBody("text/plain".toMediaTypeOrNull())
                val fotoBody = fotoBase64.toRequestBody("text/plain".toMediaTypeOrNull())
                val latitudBody = latitud.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val longitudBody = longitud.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                val response = ApiClient.apiServiceInstance.sendReporte(
                    "Bearer $token",
                    tituloBody,
                    descripcionBody,
                    fotoBody,
                    latitudBody,
                    longitudBody
                )

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ReportActivity, "Reporte enviado con éxito.", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(this@ReportActivity, "Error al enviar reporte: $errorBody", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ReportActivity, "Ocurrió un error de red: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap?): String? {
        if (bitmap == null) return null
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}