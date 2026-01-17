package com.example.betalifeup.presentation.model

data class Nivel (
    val id: Int = 0,
    val titulo: String,
    val misiones: List<Mision> = emptyList()
)