package com.example.betalifeup.presentation.menu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.betalifeup.presentation.model.Meta
import com.example.betalifeup.data.MetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MenuViewModel(
    private val repository: MetaRepository = MetaRepository()
) : ViewModel() { // Hereda de ViewModel (librería interna de Android) que permite que la pantalla "sobreviva" a recomposiciones de Compose o rotación de pantalla

    private val _metas = MutableStateFlow<List<Meta>>(emptyList()) // Lista de metas mutable
    val metas: StateFlow<List<Meta>> = _metas // Lista de metas inmutable

    init {
        fun cargarMetas() {
            repository.escucharMetas(
                onResult = { listaMetas -> // Se recogen todas las metas del usuario
                    _metas.value = listaMetas
                },
                onError = {/*Controlar error al escuchar metas*/ }
            )
        }
    }
}
