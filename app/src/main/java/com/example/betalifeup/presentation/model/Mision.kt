package com.example.betalifeup.presentation.model

data class Mision (
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val orden: Int = 0,
    val completada:Boolean = false
)