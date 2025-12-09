package com.medioambiente.models

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("mensaje")
    val mensaje: String
)