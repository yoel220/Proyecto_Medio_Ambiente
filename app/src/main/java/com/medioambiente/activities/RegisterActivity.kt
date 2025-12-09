package com.medioambiente.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.medioambiente.R
import com.medioambiente.models.RegisterResponse
import com.medioambiente.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var cedulaEditText: TextInputEditText
    private lateinit var nombreEditText: TextInputEditText
    private lateinit var apellidoEditText: TextInputEditText
    private lateinit var correoEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var telefonoEditText: TextInputEditText
    private lateinit var matriculaEditText: TextInputEditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        cedulaEditText = findViewById(R.id.cedulaEditText)
        nombreEditText = findViewById(R.id.nombreEditText)
        apellidoEditText = findViewById(R.id.apellidoEditText)
        correoEditText = findViewById(R.id.correoEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        telefonoEditText = findViewById(R.id.telefonoEditText)
        matriculaEditText = findViewById(R.id.matriculaEditText)
        registerButton = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            Log.d("RegisterActivity", "Botón de registro presionado.")
            if (validateFields()) {
                Log.d("RegisterActivity", "Validación de campos exitosa.")
                sendRegisterRequest()
            } else {
                Log.e("RegisterActivity", "Validación de campos fallida.")
            }
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true
        if (cedulaEditText.text.isNullOrEmpty()) {
            cedulaEditText.error = "Campo requerido"
            isValid = false
            Log.e("RegisterActivity", "Error: Cedula vacía")
        }
        if (nombreEditText.text.isNullOrEmpty()) {
            nombreEditText.error = "Campo requerido"
            isValid = false
            Log.e("RegisterActivity", "Error: Nombre vacío")
        }
        if (apellidoEditText.text.isNullOrEmpty()) {
            apellidoEditText.error = "Campo requerido"
            isValid = false
            Log.e("RegisterActivity", "Error: Apellido vacío")
        }
        if (correoEditText.text.isNullOrEmpty()) {
            correoEditText.error = "Campo requerido"
            isValid = false
            Log.e("RegisterActivity", "Error: Correo vacío")
        }
        if (passwordEditText.text.isNullOrEmpty()) {
            passwordEditText.error = "Campo requerido"
            isValid = false
            Log.e("RegisterActivity", "Error: Password vacío")
        }
        if (telefonoEditText.text.isNullOrEmpty()) {
            telefonoEditText.error = "Campo requerido"
            isValid = false
            Log.e("RegisterActivity", "Error: Teléfono vacío")
        }
        return isValid
    }

    private fun sendRegisterRequest() {
        val cedula = cedulaEditText.text.toString().trim()
        val nombre = nombreEditText.text.toString().trim()
        val apellido = apellidoEditText.text.toString().trim()
        val correo = correoEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val telefono = telefonoEditText.text.toString().trim()
        val matricula = matriculaEditText.text.toString().trim()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("RegisterActivity", "Iniciando llamada a la API de registro...")
                val response = ApiClient.apiServiceInstance.register(
                    cedula,
                    nombre,
                    apellido,
                    correo,
                    password,
                    telefono,
                    matricula
                )

                withContext(Dispatchers.Main) {
                    Log.d("RegisterActivity", "Respuesta de la API recibida. Código: ${response.code()}")
                    if (response.isSuccessful) {
                        val registerResponse = response.body()
                        if (registerResponse != null) {
                            saveAuthToken(registerResponse.token)
                            saveUserData(registerResponse.usuario.id, registerResponse.usuario.nombre)

                            Toast.makeText(
                                this@RegisterActivity,
                                "Registro exitoso. ¡Bienvenido!",
                                Toast.LENGTH_LONG
                            ).show()

                            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@RegisterActivity, "Respuesta inválida del servidor", Toast.LENGTH_LONG).show()
                            Log.e("RegisterActivity", "Error: Cuerpo de la respuesta nulo.")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = if (errorBody.isNullOrEmpty()) {
                            "Error de registro: ${response.code()}"
                        } else {
                            "Error: $errorBody"
                        }
                        Toast.makeText(this@RegisterActivity, errorMessage, Toast.LENGTH_LONG).show()
                        Log.e("RegisterActivity", "Error de registro: $errorMessage")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("RegisterActivity", "Excepción al registrar: ${e.message}", e)
                    Toast.makeText(this@RegisterActivity, "Ocurrió un error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun saveAuthToken(token: String) {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("auth_token", token)
            apply()
        }
    }

    private fun saveUserData(userId: String, userName: String) {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("user_id", userId)
            putString("user_name", userName)
            apply()
        }
    }
}