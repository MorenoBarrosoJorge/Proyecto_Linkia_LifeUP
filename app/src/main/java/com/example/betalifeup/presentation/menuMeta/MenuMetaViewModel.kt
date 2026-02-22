package com.example.betalifeup.presentation.menuMeta

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.betalifeup.data.MetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.betalifeup.presentation.model.Meta
import com.example.betalifeup.presentation.model.Nivel

class MenuMetaViewModel(private val repository: MetaRepository = MetaRepository()) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuMetaUiState())
    val uiState: StateFlow<MenuMetaUiState> = _uiState

    var errorMessage by mutableStateOf("")
        private set

    var misionActualizada by mutableStateOf(false)
        private set

    fun reiniciarError() {
        errorMessage = ""
    }

    fun reiniciarMisionActualizada() {
        misionActualizada = false
    }

    fun escucharMetaNivelActual(metaId: String) {
        _uiState.value = _uiState.value.copy(cargando = true)

        repository.escucharMetaNivelActual(
            metaId = metaId,
            onResult = { meta ->
                val nivelActual = calcularNivelActual(meta)

                _uiState.value = MenuMetaUiState(
                    meta = meta,
                    nivelActual = nivelActual,
                    misionesVisibles = nivelActual?.misiones ?: emptyList(),
                    cargando = false
                )
            },
            onError = { error ->
                _uiState.value = MenuMetaUiState(
                    cargando = false,
                    error = error.message
                )
            }
        )
    }
//MenuMetaUiState
    private fun calcularNivelActual(meta: Meta): Nivel? {
        return meta.niveles
            .sortedBy { it.orden }
            .firstOrNull { nivel ->
                nivel.misiones.any { !it.completada }
            }
    }

    fun completarMision(userId: String, metaId: String, nivelId: String, misionId: String) {
        repository.conectado { conectado ->
            if (!conectado) {
                errorMessage = "Error de conexión"
                return@conectado
            }
            repository.actualizarEstadoMision(
                userId = userId,
                metaId = metaId,
                nivelId = nivelId,
                misionId = misionId
            )
        }
    }
}