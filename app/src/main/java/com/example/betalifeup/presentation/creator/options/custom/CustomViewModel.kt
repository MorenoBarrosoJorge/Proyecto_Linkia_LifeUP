package com.example.betalifeup.presentation.creator.options.custom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.betalifeup.data.MetaRepository
import com.example.betalifeup.presentation.model.Meta

class CreatorViewModel(
    private val repository: MetaRepository = MetaRepository()
) : ViewModel() {

    var titulo by mutableStateOf("")
        private set

    var descripcion by mutableStateOf("")
        private set

    var fechaLimite by mutableStateOf(0L)
        private set

    var showTips by mutableStateOf(true)
        private set

    fun onTituloChange(value: String) {
        titulo = value
    }

    fun onDescripcionChange(value: String) {
        descripcion = value
    }

    fun onFechaLimiteChange(timestamp: Long) {
        fechaLimite = timestamp
    }

    fun ocultarTips() {
        showTips = false
    }

    fun subirMeta(
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val meta = Meta(
            titulo = titulo,
            descripcion = descripcion,
            fechaLimite = fechaLimite
        )

//        repository.subirMeta(
//            meta = meta,
//            onSuccess = onSuccess,
//            onError = onError
//        )
    }
}
