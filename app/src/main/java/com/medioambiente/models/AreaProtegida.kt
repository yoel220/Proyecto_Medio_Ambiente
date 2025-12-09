package com.medioambiente.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AreaProtegida(
    val id: String,
    val nombre: String,
    val tipo: String,
    val descripcion: String,
    @SerializedName("ubicacion")
    val provincia: String,
    @SerializedName("superficie")
    val extension: String,
    val imagen: String,
    val latitud: Double,
    val longitud: Double
) : Parcelable