package com.medioambiente.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Miembro(
    @SerializedName("id")
    val id: String,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("cargo")
    val cargo: String,
    @SerializedName("departamento")
    val departamento: String,
    @SerializedName("foto")
    val fotoUrl: String,
    @SerializedName("biografia")
    val biografia: String
) : Serializable