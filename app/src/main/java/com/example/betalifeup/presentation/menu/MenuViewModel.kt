package com.example.betalifeup.presentation.menu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.betalifeup.presentation.model.Meta
import com.example.betalifeup.data.MetaRepository

class MenuViewModel(
    private val repository: MetaRepository = MetaRepository()
) : ViewModel() { // Hereda de ViewModel (librería interna de Android) que permite que la pantalla "sobreviva" a recomposiciones de Compose o rotación de pantalla

    var metas by mutableStateOf<List<Pair<String, Meta>>>(emptyList())
        private set // La UI puede leer las metas, pero no escribirlas

    init { // Cuando se crea el ViewModel, es decir, cuando la lógica de la pantalla comienza a funcionar...
        repository.escucharMetas( // Se llama a la función responsable de recoger cada una de las metas asignadas al usuario actual
            onResult = { metas = it }, // Se asigna la lista de metas a la variable "metas" (que se recogerá en MenuScreen.kt)
            onError = { /* manejar error */ }
        )
    }
}
