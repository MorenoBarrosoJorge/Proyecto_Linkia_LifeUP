package com.example.betalifeup.presentation.metaMenu

import androidx.lifecycle.ViewModel
import com.example.betalifeup.data.MetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.betalifeup.presentation.model.Meta
import com.example.betalifeup.presentation.model.Nivel

class MenuMetaViewModel(private val repository: MetaRepository = MetaRepository()) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuMetaUiState())
    val uiState: StateFlow<MenuMetaUiState> = _uiState

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

    private fun calcularNivelActual(meta: Meta): Nivel? {
        return meta.niveles
            .sortedBy { it.orden }
            .firstOrNull { nivel ->
                nivel.misiones.any { !it.completada }
            }
    }

    fun completarMision(userId: String, metaId: String, nivelId: String, misionId: String) {
        repository.actualizarEstadoMision(
            userId = userId,
            metaId = metaId,
            nivelId = nivelId,
            misionId = misionId
        )
    }

}