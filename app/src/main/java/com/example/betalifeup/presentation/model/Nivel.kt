package com.example.betalifeup.presentation.model

data class Nivel (
    val id: String = "",
    val titulo: String = "",
    val orden: Int = 0,
    val misiones: List<Mision> = emptyList()
)