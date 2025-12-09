package com.medioambiente.network

import com.google.gson.annotations.SerializedName

data class RecoverPasswordResponse(
    @SerializedName("mensaje")
    val mensaje: String,
    @SerializedName("codigo")
    val codigo: String
)