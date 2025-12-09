package com.medioambiente.models

import com.google.gson.annotations.SerializedName

data class VoluntarioRequest(
    @SerializedName("cedula")
    val cedula: String,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("apellido")
    val apellido: String,
    @SerializedName("correo")
    val correo: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("telefono")
    val telefono: String
)