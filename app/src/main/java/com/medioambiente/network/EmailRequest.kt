package com.medioambiente.network

import com.google.gson.annotations.SerializedName

data class EmailRequest(
    @SerializedName("correo") val correo: String
)