package com.example.betalifeup.presentation.menu

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

    init {
        cargarMetas()
    }

    fun cargarMetas() {
        repository.escucharMetas(
            onResult = { listaMetas ->
                _metas.value = listaMetas
            },
            onError = {/*Controlar error al escuchar metas*/ }
        )
    }

    fun formatFecha(timestamp: Long): String {
        if (timestamp == 0L) return "Sin fecha límite"
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun obtenerNivelActualMeta(meta: Meta): Int? {
        val nivelActual = meta.niveles
            .sortedBy { it.orden }
            .firstOrNull { nivel ->
                nivel.misiones.any { !it.completada }
            }
        return nivelActual?.orden
    }

    fun nivelCard(meta: Meta): String {
        val nivel = obtenerNivelActualMeta(meta)
        return nivel?.let { "Nv $it" } ?: "MAX"
    }

    fun completarMeta(userId: String, metaId: String) {
        repository.marcarMetaComoCompletada(
            userId = userId,
            metaId = metaId
        )
    }

}
