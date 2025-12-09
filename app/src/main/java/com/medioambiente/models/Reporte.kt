package com.medioambiente.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reporte(
    @SerializedName("id") val id: String?,
    @SerializedName("codigo") val codigo: String?,
    @SerializedName("titulo") val titulo: String?,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("foto") val foto: String?,
    @SerializedName("estado") val estado: String?,
    @SerializedName("latitud") val latitud: Double?,
    @SerializedName("longitud") val longitud: Double?,
    @SerializedName("fecha") val fecha: String?,
    @SerializedName("comentario_ministerio") val comentarioMinisterio: String?
) : Parcelable