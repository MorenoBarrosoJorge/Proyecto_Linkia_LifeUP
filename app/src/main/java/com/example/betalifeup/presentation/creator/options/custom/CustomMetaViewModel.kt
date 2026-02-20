package com.example.betalifeup.presentation.creator.options.custom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.betalifeup.data.MetaRepository
import com.example.betalifeup.presentation.model.Meta
import com.example.betalifeup.presentation.model.Nivel
import com.example.betalifeup.presentation.model.Mision
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CustomMetaViewModel(
    private val repository: MetaRepository = MetaRepository()
) : ViewModel() {
    private val _metaTemporal = MutableStateFlow(
        Meta(id = "", titulo = "", descripcion = "", fechaLimite = 0L, niveles = emptyList())
    )
    val metaTemporal: StateFlow<Meta> = _metaTemporal

    var mostrarTips by mutableStateOf(true)
        private set

    private val _uiEvent = MutableSharedFlow<CustomUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    var subiendo by mutableStateOf(true)
        private set

    var errorMessage by mutableStateOf("")
        private set

    var metaGuardada by mutableStateOf(false)
        private set





    fun setTitulo(titulo: String) {
        _metaTemporal.value = _metaTemporal.value.copy(titulo = titulo)
    }

    fun setDescripcion(descripcion: String) {
        _metaTemporal.value = _metaTemporal.value.copy(descripcion = descripcion)
    }

    fun setFechaLimite(fecha: Long) {
        _metaTemporal.value = _metaTemporal.value.copy(fechaLimite = fecha)
    }

    fun ocultarTips() {
        mostrarTips = false
    }

    fun addNivel(){
        val nivelesActuales = _metaTemporal.value.niveles
        if (nivelesActuales.isNotEmpty() && nivelesActuales.last().misiones.isEmpty()){
            viewModelScope.launch {
                _uiEvent.emit(
                    CustomUiEvent.ShowSnackbar("¡Tienes que añadir al menos una misión antes de crear un nuevo nivel!")
                )
            }
            return
        }
        val nuevoNivel = Nivel(
            id = UUID.randomUUID().toString(),
            titulo = "Nivel ${nivelesActuales.size + 1}",
            misiones = emptyList(),
            orden = nivelesActuales.size + 1
        )
        _metaTemporal.value = _metaTemporal.value.copy(
            niveles = nivelesActuales + nuevoNivel
        )
    }

    fun addMision(nivelId: String, titulo: String, descripcion: String) {
        val nivelesActuales = _metaTemporal.value.niveles.map { nivel ->
            if (nivel.id == nivelId) {
                val nuevaMision = Mision(
                    id = UUID.randomUUID().toString(),
                    titulo = titulo,
                    descripcion = descripcion,
                    orden = nivel.misiones.size + 1
                )
                nivel.copy(misiones = nivel.misiones + nuevaMision)
            } else nivel
        }
        _metaTemporal.value = _metaTemporal.value.copy(niveles = nivelesActuales)
    }

    fun updateMision(nivelId: String, misionId: String, nuevoTitulo: String, nuevaDescripcion: String) {
        val nivelesActuales = _metaTemporal.value.niveles.map { nivel ->
            if (nivel.id == nivelId) {
                val misionesActuales = nivel.misiones.map { mision ->
                    if (mision.id == misionId) mision.copy(titulo = nuevoTitulo, descripcion = nuevaDescripcion)
                    else mision
                }
                nivel.copy(misiones = misionesActuales)
            } else nivel
        }
        _metaTemporal.value = _metaTemporal.value.copy(niveles = nivelesActuales)
    }

    fun deleteMision(nivelId: String, misionId: String) {
        val nivelesActuales = _metaTemporal.value.niveles.map { nivel ->
            if (nivel.id == nivelId) {
                val misionesActuales = nivel.misiones.filter { it.id != misionId }
                nivel.copy(misiones = misionesActuales)
            } else nivel
        }
        _metaTemporal.value = _metaTemporal.value.copy(niveles = nivelesActuales)
    }

    fun guardarMeta(userId: String) {
        subiendo = true
        errorMessage = ""
        metaGuardada = false
        val metaParaGuardar = _metaTemporal.value.copy(
            id = ""
        )
        repository.subirMeta(
            userID = userId,
            meta = metaParaGuardar,
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

    fun reiniciarValoresMeta() {
        _metaTemporal.value = Meta(
            id = "",
            titulo = "",
            descripcion = "",
            fechaLimite = 0L,
            fechaCreacion = 0L,
            niveles = emptyList(),
            fechaCompletada = null
        )
    }
}
