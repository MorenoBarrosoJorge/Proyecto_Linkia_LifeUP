package com.example.betalifeup.presentation.profile
import android.net.Uri

data class ProfileUiState(
    val imagenUri: Uri? = null,
    val cargando: Boolean = false,
    val error: String? = null
)
