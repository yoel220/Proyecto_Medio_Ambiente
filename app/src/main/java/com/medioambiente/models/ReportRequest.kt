package com.medioambiente.models

import com.google.gson.annotations.SerializedName

data class ReportRequest(
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("foto") val foto: String, // Base64
    @SerializedName("latitud") val latitud: Double,
    @SerializedName("longitud") val longitud: Double
)