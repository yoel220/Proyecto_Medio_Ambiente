package com.medioambiente.models

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("token") val token: String,
    @SerializedName("usuario") val usuario: Usuario
)