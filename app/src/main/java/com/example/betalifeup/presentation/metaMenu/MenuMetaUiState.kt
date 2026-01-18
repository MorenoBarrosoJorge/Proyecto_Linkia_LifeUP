package com.example.betalifeup.presentation.metaMenu

import com.example.betalifeup.presentation.model.Meta
import com.example.betalifeup.presentation.model.Nivel
import com.example.betalifeup.presentation.model.Mision

data class MenuMetaUiState (
    val meta: Meta? = null,
    val nivelActual: Nivel? = null,
    val misionesVisibles: List<Mision> = emptyList(),
    val cargando: Boolean = true,
    val error: String? = null
)