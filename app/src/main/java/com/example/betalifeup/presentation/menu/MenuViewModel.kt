package com.example.betalifeup.presentation.menu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.betalifeup.presentation.model.Meta
import com.example.betalifeup.data.MetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MenuViewModel(
    private val repository: MetaRepository = MetaRepository()
) : ViewModel() {

    private val _metas = MutableStateFlow<List<Meta>>(emptyList())
    val metas: StateFlow<List<Meta>> = _metas

    var errorMessage by mutableStateOf("")
        private set

    var metaActualizada by mutableStateOf(false)
        private set

    init {
        cargarMetas()
    }

    fun reiniciarError() {
        errorMessage = ""
    }

    fun reiniciarMetaActualizada() {
        metaActualizada = false
    }

    fun cargarMetas() {
        repository.escucharMetas(
            onResult = { listaMetas ->
                _metas.value = listaMetas
            },
            onError = {/*Controlar error al escuchar metas*/ }
        )
    }

    fun completarMeta(userId: String, metaId: String) {
        repository.conectado { conectado ->
            if (!conectado){
                errorMessage = "Error de conexión"
                return@conectado
            }
            repository.marcarMetaComoCompletada(
                userId = userId,
                metaId = metaId,
                onSuccess = {
                    metaActualizada = true
                },
                onError = { error ->
                    errorMessage = error
                }
            )
        }
    }
}
