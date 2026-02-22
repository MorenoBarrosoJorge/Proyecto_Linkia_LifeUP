package com.example.betalifeup.presentation.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.net.Uri
import com.example.betalifeup.data.MetaRepository

class ProfileViewModel(
    private val repository: MetaRepository = MetaRepository()
) : ViewModel(){

    private val auth = FirebaseAuth.getInstance()
    private val _email = MutableStateFlow(auth.currentUser?.email.orEmpty())
    val email: StateFlow<String> = _email
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun cambiarContrasena(nuevaContrasena: String) {
        val user = auth.currentUser ?: return
        user.updatePassword(nuevaContrasena)
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

    fun onImagenSeleccionada(uri: Uri) {
        _uiState.value = _uiState.value.copy(imagenUri = uri)
    }

    fun contrasenaValida(password: String): Boolean{
        return password.length > 9 &&
                password.any { it.isDigit() } &&
                password.any { it.isLowerCase() } &&
                password.any { it.isUpperCase() }
    }
}