package com.medioambiente.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("correo")
    val correo: String,
    @SerializedName("password")
    val password: String
)