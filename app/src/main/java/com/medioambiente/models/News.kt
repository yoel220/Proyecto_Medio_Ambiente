package com.medioambiente.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    @SerializedName("id")
    val id: String,
    @SerializedName("titulo")
    val titulo: String,
    @SerializedName("resumen")
    val resumen: String,
    @SerializedName("contenido")
    val contenido: String,
    @SerializedName("imagen")
    val imagen: String,
    @SerializedName("fecha")
    val fecha: String
) : Parcelable