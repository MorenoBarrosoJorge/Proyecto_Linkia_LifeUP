package com.example.betalifeup.presentation.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel(): ViewModel(){

    private val auth = FirebaseAuth.getInstance()
    private val _email = MutableStateFlow(auth.currentUser?.email.orEmpty())
    val email: StateFlow<String> = _email
    private val _mensaje = MutableStateFlow<String?>(null) // Para recoger mensaje de error
    val mensaje: StateFlow<String?> = _mensaje

    fun cambiarContrasena(nuevaContraseña: String) {
        val user = auth.currentUser ?: return

        user.updatePassword(nuevaContraseña)
            .addOnSuccessListener {
                _mensaje.value = "Contraseña actualizada correctamente"
            }
            .addOnFailureListener { e ->
                _mensaje.value = e.message
            }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    fun cerrarSesion() {
        auth.signOut()
    }

}