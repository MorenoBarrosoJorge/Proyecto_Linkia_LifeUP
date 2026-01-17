package com.example.betalifeup.presentation.model

data class Meta(
    val id: String = "",
    val titulo:String = "",
    val descripcion:String = "",
    val fechaLimite:Long = 0L,
    val fechaCreacion:Long = 0L,
    val niveles: List<Nivel> = emptyList()
)