package com.medioambiente.models

import com.google.gson.annotations.SerializedName

data class Medida(
    @SerializedName("id")
    val id: String,
    @SerializedName("titulo")
    val titulo: String,
    @SerializedName("descripcion")
    val contenido: String,
    @SerializedName("icono")
    val icono: String,
    @SerializedName("categoria")
    val categoria: String,
    @SerializedName("fecha_creacion")
    val fechaCreacion: String
)