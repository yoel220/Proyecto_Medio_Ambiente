package com.medioambiente.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.medioambiente.R
import com.medioambiente.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VoluntarioActivity : AppCompatActivity() {

    private lateinit var cedulaEditText: TextInputEditText
    private lateinit var nombreEditText: TextInputEditText
    private lateinit var apellidoEditText: TextInputEditText
    private lateinit var correoEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var telefonoEditText: TextInputEditText
    private lateinit var solicitarButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voluntario)

        cedulaEditText = findViewById(R.id.cedulaEditText)
        nombreEditText = findViewById(R.id.nombreEditText)
        apellidoEditText = findViewById(R.id.apellidoEditText)
        correoEditText = findViewById(R.id.correoEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        telefonoEditText = findViewById(R.id.telefonoEditText)
        solicitarButton = findViewById(R.id.solicitarButton)

        solicitarButton.setOnClickListener {
            if (validateFields()) {
                sendVolunteerRequest()
            }
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true

        val cedula = cedulaEditText.text.toString().trim()
        val nombre = nombreEditText.text.toString().trim()
        val apellido = apellidoEditText.text.toString().trim()
        val correo = correoEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val telefono = telefonoEditText.text.toString().trim()

        if (cedula.isEmpty()) {
            cedulaEditText.error = "Campo requerido"
            isValid = false
        } else {
            cedulaEditText.error = null
        }
        if (nombre.isEmpty()) {
            nombreEditText.error = "Campo requerido"
            isValid = false
        } else {
            nombreEditText.error = null
        }
        if (apellido.isEmpty()) {
            apellidoEditText.error = "Campo requerido"
            isValid = false
        } else {
            apellidoEditText.error = null
        }
        if (correo.isEmpty()) {
            correoEditText.error = "Campo requerido"
            isValid = false
        } else {
            correoEditText.error = null
        }
        if (password.isEmpty()) {
            passwordEditText.error = "Campo requerido"
            isValid = false
        } else {
            passwordEditText.error = null
        }
        if (telefono.isEmpty()) {
            telefonoEditText.error = "Campo requerido"
            isValid = false
        } else {
            telefonoEditText.error = null
        }

        return isValid
    }

    private fun sendVolunteerRequest() {
        val cedula = cedulaEditText.text.toString().trim()
        val nombre = nombreEditText.text.toString().trim()
        val apellido = apellidoEditText.text.toString().trim()
        val correo = correoEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val telefono = telefonoEditText.text.toString().trim()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.apiServiceInstance.solicitarVoluntariado(
                    cedula,
                    nombre,
                    apellido,
                    correo,
                    password,
                    telefono
                )

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val mensaje = response.body()?.mensaje ?: "Solicitud enviada correctamente"
                        Toast.makeText(this@VoluntarioActivity, mensaje, Toast.LENGTH_LONG).show()
                        clearFields()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = if (errorBody.isNullOrEmpty()) {
                            "Error al enviar la solicitud: ${response.code()}"
                        } else {
                            "Error: $errorBody"
                        }
                        Toast.makeText(this@VoluntarioActivity, errorMessage, Toast.LENGTH_LONG).show()
                        Log.e("VoluntarioAPI", "Error al enviar solicitud: $errorMessage")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@VoluntarioActivity, "Ocurrió un error: ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e("VoluntarioAPI", "Excepción: ${e.message}", e)
                }
            }
        }
    }

    private fun clearFields() {
        cedulaEditText.text?.clear()
        nombreEditText.text?.clear()
        apellidoEditText.text?.clear()
        correoEditText.text?.clear()
        passwordEditText.text?.clear()
        telefonoEditText.text?.clear()
    }
}