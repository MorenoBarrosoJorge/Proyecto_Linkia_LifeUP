package com.example.betalifeup.presentation.creator.options.fast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.betalifeup.data.MetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FastViewModel (
    private val repository: MetaRepository = MetaRepository()
): ViewModel() {
    private val _uiState = MutableStateFlow(MetaPromptUiState())
    val uiState: StateFlow<MetaPromptUiState> = _uiState

    fun recogerPrompt(text: String) {
        _uiState.value = _uiState.value.copy(prompt = text)
    }
    fun generarMetaPrompt() {
        val prompt = _uiState.value.prompt.trim().lowercase()
        _uiState.value = _uiState.value.copy(cargando = true, error = null)

        viewModelScope.launch {
            if (prompt.contains("python")){
                val meta = MetaTemplates.plantillaAprenderPython
                _uiState.value = _uiState.value.copy(
                    metaGenerada = meta,
                    cargando = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    error = "No se pudo generar la meta",
                    cargando = false
                )
            }
        }
    }
    fun confirmarMetaPrompt(userId: String) {
        val meta = _uiState.value.metaGenerada ?: return

        viewModelScope.launch {
            try {
                repository.subirMeta(userId, meta)
                _uiState.value = MetaPromptUiState()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al guardar la meta"
                )
            }
        }
    }
}