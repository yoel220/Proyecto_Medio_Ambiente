package com.medioambiente.models

import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("id")
    val id: String,
    @SerializedName("titulo")
    val titulo: String,
    @SerializedName("descripcion")
    val descripcion: String,
    @SerializedName("url")
    val urlVideo: String,
    @SerializedName("thumbnail")
    val urlMiniatura: String,
    @SerializedName("categoria")
    val categoria: String,
    @SerializedName("duracion")
    val duracion: String
)