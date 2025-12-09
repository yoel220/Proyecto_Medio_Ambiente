package com.medioambiente.models

import com.google.gson.annotations.SerializedName

data class Servicio(
    @SerializedName("id")
    val id: String,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("descripcion")
    val descripcion: String,
    @SerializedName("icono")
    val icono: String
)