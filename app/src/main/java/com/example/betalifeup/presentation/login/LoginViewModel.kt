package com.example.betalifeup.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel: ViewModel() {

    var errorMessage by mutableStateOf("")
        private set

    var confirmar by mutableStateOf((false))
        private set

    fun iniciarUsuario(auth: FirebaseAuth, email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    confirmar = true
                } else {
                    val exception = task.exception
                    if (exception is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException) {
                        errorMessage = "Credenciales incorrectas. Vuelve a intentarlo."
                    } else {
                        errorMessage = "Error al iniciar sesión. Inténtalo de nuevo."
                    }
                }
            }
    }

    fun validarCredenciales(email: String, password: String){
        errorMessage = ""
        when {
            email.isBlank() && password.isBlank() -> {
                errorMessage = "No has introducido ninguna credencial"
            }

            email.isBlank() -> {
                errorMessage = "No has introducido ningún correo electrónico"
            }

            password.isBlank() -> {
                errorMessage = "No has introducido ninguna contraseña"
            }

            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                errorMessage = "No has introducido un correo electrónico válido"
            }

            password.length < 10 -> {
                errorMessage = "No has introducido una contraseña válida"
            }
        }
    }
}