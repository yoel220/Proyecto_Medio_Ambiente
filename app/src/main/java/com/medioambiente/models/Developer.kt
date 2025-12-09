// Archivo: Developer.kt
package com.medioambiente.models

import java.io.Serializable

data class Developer(
    val nombre: String,
    val matricula: String,
    val telefono: String,
    val telegram: String,
    val foto: Int
) : Serializable