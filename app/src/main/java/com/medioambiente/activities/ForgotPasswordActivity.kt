package com.medioambiente.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.medioambiente.databinding.ActivityForgotPasswordBinding
import com.medioambiente.network.ApiClient
import com.medioambiente.network.EmailRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendEmailButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            if (email.isEmpty()) {
                binding.emailEditText.error = "Ingresa tu correo"
            } else {
                sendForgotPasswordEmail(email)
            }
        }
    }

    private fun sendForgotPasswordEmail(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("ForgotPasswordActivity", "Correo a enviar: $email")
                val request = EmailRequest(correo = email)
                val response = ApiClient.apiServiceInstance.forgotPassword(request)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val recoverResponse = response.body()
                        if (recoverResponse != null) {
                            Toast.makeText(this@ForgotPasswordActivity, recoverResponse.mensaje, Toast.LENGTH_LONG).show()


                            val intent = Intent(this@ForgotPasswordActivity, ChangePasswordActivity::class.java)
                            intent.putExtra("email", email)
                            intent.putExtra("reset_code", recoverResponse.codigo)
                            startActivity(intent)

                            finish()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = "Error: $errorBody"
                        Log.e("ForgotPassword", "Error al enviar correo: $errorMessage")
                        Toast.makeText(this@ForgotPasswordActivity, "Error al enviar el correo. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ForgotPassword", "Excepción: ${e.message}", e)
                    Toast.makeText(this@ForgotPasswordActivity, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}