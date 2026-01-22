package com.example.betalifeup.presentation.model

data class Meta(
    val id: String = "",
    val titulo:String = "",
    val descripcion:String = "",
    val fechaLimite:Long = 0L,
    val fechaCreacion:Long = 0L,
    val niveles: List<Nivel> = emptyList(),
    val fechaCompletada: Long? = null
){
    fun progresoMeta(): Float {
        val totalMisiones = niveles.sumOf { it.misiones.size }
        if (totalMisiones == 0) return 0f
        val misionesCompletadas = niveles.sumOf { nivel ->
            nivel.misiones.count { it.completada }
        }
        return misionesCompletadas.toFloat() / totalMisiones.toFloat()
    }
}