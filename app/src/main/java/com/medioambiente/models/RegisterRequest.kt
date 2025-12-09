package com.medioambiente.models

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("apellido")
    val apellido: String,
    @SerializedName("correo")
    val correo: String,
    @SerializedName("cedula")
    val cedula: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("telefono")
    val telefono: String,
    @SerializedName("matricula")
    val matricula: String?
)