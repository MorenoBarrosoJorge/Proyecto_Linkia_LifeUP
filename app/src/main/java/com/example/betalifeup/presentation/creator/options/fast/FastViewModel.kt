package com.example.betalifeup.presentation.creator.options.fast

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    var subiendo by mutableStateOf(true)
        private set

    var errorMessage by mutableStateOf("")
        private set

    var metaGuardada by mutableStateOf(false)
        private set

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
        subiendo = true
        errorMessage = ""
        metaGuardada = false

        repository.subirMeta(
            userID = userId,
            meta = meta,
            onSucces = {
                subiendo = false
                metaGuardada = true
            },
            onError = { error ->
                subiendo = false
                errorMessage = error

            }
        )
    }
}