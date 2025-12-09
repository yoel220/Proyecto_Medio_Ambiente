package com.medioambiente.models

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("correo") val correo: String,
    @SerializedName("codigo") val codigo: String,
    @SerializedName("nueva_password") val nuevaPassword: String
)