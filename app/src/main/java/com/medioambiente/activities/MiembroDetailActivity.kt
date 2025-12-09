package com.medioambiente.activities

import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import com.medioambiente.R
import com.medioambiente.models.Miembro

class MiembroDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_miembro_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val miembro = intent.getSerializableExtra("miembro") as? Miembro

        if (miembro != null) {
            val fotoImageView: ShapeableImageView = findViewById(R.id.miembroFotoImageView)
            val nombreTextView: TextView = findViewById(R.id.miembroNombreTextView)
            val cargoTextView: TextView = findViewById(R.id.miembroCargoTextView)
            val biografiaTextView: TextView = findViewById(R.id.miembroBiografiaTextView)

            nombreTextView.text = miembro.nombre
            cargoTextView.text = miembro.cargo
            biografiaTextView.text = miembro.biografia

            title = miembro.nombre


            val fotoData = miembro.fotoUrl
            Log.d("MiembroDetail", "Datos de la foto: $fotoData")

            if (!fotoData.isNullOrEmpty()) {
                val requestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_error_image)


                if (fotoData.startsWith("http") || fotoData.startsWith("https")) {
                    Glide.with(this)
                        .load(fotoData)
                        .apply(requestOptions)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(fotoImageView)
                } else {
                    try {
                        val decodedString: ByteArray = Base64.decode(fotoData, Base64.DEFAULT)
                        Glide.with(this)
                            .asBitmap()
                            .load(decodedString)
                            .apply(requestOptions)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(fotoImageView)
                    } catch (e: IllegalArgumentException) {
                        Log.e("MiembroDetail", "Error al decodificar Base64: ${e.message}")
                        Glide.with(this)
                            .load(R.drawable.ic_error_image)
                            .into(fotoImageView)
                    }
                }
            } else {
                Glide.with(this)
                    .load(R.drawable.ic_error_image)
                    .into(fotoImageView)
            }
        } else {
            Log.e("MiembroDetail", "Objeto Miembro recibido es nulo.")
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}