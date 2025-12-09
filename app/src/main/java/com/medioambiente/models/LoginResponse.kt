package com.medioambiente.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("usuario")
    val usuario: Usuario
)

data class Usuario(
    @SerializedName("id")
    val id: String,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("apellido")
    val apellido: String,
    @SerializedName("correo")
    val correo: String,
    @SerializedName("cedula")
    val cedula: String
)