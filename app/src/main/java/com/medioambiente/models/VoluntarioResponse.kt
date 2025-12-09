package com.medioambiente.models

import com.google.gson.annotations.SerializedName

data class VoluntarioResponse(
    @SerializedName("mensaje")
    val mensaje: String
)