package com.medioambiente.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.medioambiente.R
import com.medioambiente.models.LoginResponse
import com.medioambiente.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var correoEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView
    private lateinit var forgotPasswordTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        correoEditText = findViewById(R.id.correoEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerTextView = findViewById(R.id.registerTextView)
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView)

        loginButton.setOnClickListener {
            Log.d("LoginActivity", "Botón de login presionado.")
            val correo = correoEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (correo.isEmpty()) {
                correoEditText.error = "Correo requerido"
                return@setOnClickListener
            } else {
                correoEditText.error = null
            }

            if (password.isEmpty()) {
                passwordEditText.error = "Contraseña requerida"
                return@setOnClickListener
            } else {
                passwordEditText.error = null
            }

            Log.d("LoginActivity", "Validación de campos exitosa.")
            sendLoginRequest(correo, password)
        }

        registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        forgotPasswordTextView.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun sendLoginRequest(correo: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("LoginActivity", "Iniciando llamada a la API de login...")
                val response = ApiClient.apiServiceInstance.login(correo, password)

                withContext(Dispatchers.Main) {
                    Log.d("LoginActivity", "Respuesta de la API recibida. Código: ${response.code()}")
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse != null) {
                            // Guardar datos del usuario y el token de autenticación
                            val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                            with(sharedPref.edit()) {
                                putString("auth_token", loginResponse.token)
                                putString("user_id", loginResponse.usuario.id)
                                putString("user_name", loginResponse.usuario.nombre)
                                putBoolean("is_logged_in", true) // Marcar como logueado
                                apply()
                            }

                            Toast.makeText(this@LoginActivity, "¡Bienvenido, ${loginResponse.usuario.nombre}!", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Respuesta inválida del servidor", Toast.LENGTH_LONG).show()
                            Log.e("LoginActivity", "Error: Cuerpo de la respuesta nulo.")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = if (errorBody.isNullOrEmpty()) {
                            "Credenciales incorrectas"
                        } else {
                            "Error: $errorBody"
                        }
                        Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_LONG).show()
                        Log.e("LoginActivity", "Error de login: $errorMessage")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("LoginActivity", "Excepción al iniciar sesión: ${e.message}", e)
                    Toast.makeText(this@LoginActivity, "Ocurrió un error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}