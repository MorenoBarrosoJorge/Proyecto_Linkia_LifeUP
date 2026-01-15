package com.example.betalifeup.presentation.creator.options.custom

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.betalifeup.presentation.model.Nivel
import com.example.betalifeup.presentation.model.Mision

class Custom2ViewModel: ViewModel(){

    // Lista de niveles por meta
    private val _nivelesMeta = MutableStateFlow<List<Nivel>>(emptyList()) // Lista que se modificará
    val nivelesMeta: StateFlow<List<Nivel>> = _nivelesMeta // Lista que se pasará a Custom2Screen para ser visualizada


    // Lista de misiones por nivel
    private val _misionesNivel = MutableStateFlow<Map<Int,List<Mision>>>(emptyMap()) // Lista que se modificará
    val misionesNivel: StateFlow<Map<Int,List<Mision>>> = _misionesNivel // Lista que se pasará a Custom2Screen para ser visualizada


    fun addNivel() {
        val nuevoId = _nivelesMeta.value.size + 1
        val nuevoNivel = Nivel(
            id = nuevoId,
            titulo = "Nivel $nuevoId"
        )
        _nivelesMeta.value = _nivelesMeta.value + nuevoNivel
        _misionesNivel.value =
            _misionesNivel.value + (nuevoId to emptyList())
    }

    fun addMision(
        nivelId: Int,
        titulo: String,
        descripcion: String
    ) {
        val listaActual = _misionesNivel.value[nivelId].orEmpty() //Para evitar el error NullPointException

        val nuevaMision = Mision(
            id = listaActual.size + 1,
            titulo = titulo,
            descripcion = descripcion
        )

        _misionesNivel.value =
            _misionesNivel.value + (nivelId to (listaActual + nuevaMision)) // Se añade la nueva misión a la lista de misiones existente en el nivel correspondiente
    }

    fun updateMision(
        nivelId: Int,
        misionId: Int,
        nuevoTitulo: String,
        nuevaDescripcion: String
    ) {
        val mapaActual = _misionesNivel.value
        val listaActual = mapaActual[nivelId].orEmpty()

        val listaActualizada = listaActual.map { mision ->
            if (mision.id == misionId) {
                mision.copy(
                    titulo = nuevoTitulo,
                    descripcion = nuevaDescripcion
                )
            } else {
                mision
            }
        }

        _misionesNivel.value = mapaActual + (nivelId to listaActualizada)
    }

    fun deleteMision(
        nivelId: Int,
        misionId: Int
    ) {
        val listaActual = _misionesNivel.value[nivelId].orEmpty()
        val listaActualizada = listaActual.filterNot { it.id == misionId }

        _misionesNivel.value =
            _misionesNivel.value + (nivelId to listaActualizada)
    }

}