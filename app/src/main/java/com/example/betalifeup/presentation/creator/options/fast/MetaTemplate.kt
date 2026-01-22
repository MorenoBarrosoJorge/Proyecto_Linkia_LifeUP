package com.example.betalifeup.presentation.creator.options.fast

import com.example.betalifeup.presentation.model.Meta
import com.example.betalifeup.presentation.model.Nivel
import com.example.betalifeup.presentation.model.Mision

import java.util.UUID

object MetaTemplates {

    val plantillaAprenderPython = Meta(
        titulo = "Aprender Python desde cero",
        descripcion = "Quiero dominar los fundamentos de Python",
        niveles = listOf(
            Nivel(
                id = UUID.randomUUID().toString(),
                titulo = "Nivel 1",
                misiones = listOf(
                    Mision(
                        id = UUID.randomUUID().toString(),
                        titulo = "Aprender estructura básica",
                        descripcion = "Variables, tipos y operadores",
                        orden = 1
                    ),
                    Mision(
                        id = UUID.randomUUID().toString(),
                        titulo = "Condicionales",
                        descripcion = "IF, ELSE y operadores lógicos",
                        orden = 2
                    )
                )
            ),
            Nivel(
                id = UUID.randomUUID().toString(),
                titulo = "Nivel 2",
                misiones = listOf(
                    Mision(
                        id = UUID.randomUUID().toString(),
                        titulo = "Bucles",
                        descripcion = "FOR, WHILE y sus variantes",
                        orden = 1
                    )
                )
            )
        )
    )
}
