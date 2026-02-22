package com.example.betalifeup.presentation.singup

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.composableLambdaNInstance
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class SignupViewModel(): ViewModel() {

    var errorMessage by mutableStateOf("")
        private set

    var confirmar by mutableStateOf((false))
        private set

    fun contrasenaValida(password: String, confirmarPassword: String): Boolean{
        return password.length > 9 &&
                password.any { it.isDigit() } &&
                password.any { it.isLowerCase() } &&
                password.any { it.isUpperCase() } &&
                confirmarPassword.length > 9 &&
                confirmarPassword.any { it.isDigit() } &&
                confirmarPassword.any { it.isLowerCase() } &&
                confirmarPassword.any { it.isUpperCase() } &&
                password == confirmarPassword

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

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                errorMessage = "No has introducido un correo electrónico válido"
            }
        }
    }

    fun crearUsuario(auth: FirebaseAuth, email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                confirmar = true
            } else {
                val excepcion = task.exception
                when (excepcion) {
                    is FirebaseAuthUserCollisionException -> {
                        errorMessage = "El correo que has introducido ya tiene una cuenta asociada."
                    }
                    else -> errorMessage = "Error al intentar completar el registro. Inténtalo de nuevo."
                }
            }
        }
    }
}