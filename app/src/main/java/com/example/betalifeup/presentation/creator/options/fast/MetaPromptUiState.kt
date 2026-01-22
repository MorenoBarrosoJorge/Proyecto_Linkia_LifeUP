package com.example.betalifeup.presentation.creator.options.fast

import com.example.betalifeup.presentation.model.Meta

data class MetaPromptUiState (
    val prompt: String = "",
    val metaGenerada: Meta? = null,
    val cargando: Boolean = false,
    val error: String? = null,
    val metaGuardada:Boolean = false
)