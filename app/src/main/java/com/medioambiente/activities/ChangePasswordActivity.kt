package com.medioambiente.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.medioambiente.R
import com.medioambiente.databinding.ActivityChangePasswordBinding
import com.medioambiente.models.ResetPasswordRequest
import com.medioambiente.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.changePasswordButton.setOnClickListener {
            changePassword()
        }
    }

    private fun changePassword() {
        val userEmail = binding.emailEditText.text.toString().trim()
        val resetCode = binding.codeEditText.text.toString().trim()
        val newPassword = binding.newPasswordEditText.text.toString().trim()
        val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

        if (userEmail.isEmpty()) {
            binding.emailEditText.error = "Campo requerido"
            return
        }
        if (resetCode.isEmpty()) {
            binding.codeEditText.error = "Campo requerido"
            return
        }
        if (newPassword.isEmpty()) {
            binding.newPasswordEditText.error = "Campo requerido"
            return
        }
        if (confirmPassword.isEmpty()) {
            binding.confirmPasswordEditText.error = "Campo requerido"
            return
        }
        if (newPassword != confirmPassword) {
            binding.newPasswordEditText.error = "Las contraseñas no coinciden"
            binding.confirmPasswordEditText.error = "Las contraseñas no coinciden"
            return
        }
        if (newPassword.length < 6) {
            binding.newPasswordEditText.error = "La contraseña debe tener al menos 6 caracteres"
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val requestBody = ResetPasswordRequest(
                    correo = userEmail,
                    codigo = resetCode,
                    nuevaPassword = newPassword
                )

                val response = ApiClient.apiServiceInstance.resetPassword(requestBody)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        Toast.makeText(this@ChangePasswordActivity, "Contraseña cambiada con éxito.", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = "Error: $errorBody"
                        Toast.makeText(this@ChangePasswordActivity, errorMessage, Toast.LENGTH_LONG).show()
                        Log.e("ChangePasswordActivity", "Error al cambiar contraseña: $errorMessage")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ChangePasswordActivity, "Ocurrió un error de red: ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e("ChangePasswordActivity", "Excepción: ${e.message}", e)
                }
            }
        }
    }
}