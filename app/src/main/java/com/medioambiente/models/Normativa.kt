package com.medioambiente.models

import com.google.gson.annotations.SerializedName

data class Normativa(
    @SerializedName("id") val id: String,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("enlace") val enlace: String?
)