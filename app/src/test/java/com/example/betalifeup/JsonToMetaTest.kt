package com.example.betalifeup

import com.example.betalifeup.presentation.model.Meta
import com.example.betalifeup.presentation.model.Nivel
import com.example.betalifeup.presentation.model.Mision
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

class MetaJsonTest {

    private val gson = Gson()

    @Test
    fun JsonToMeta() {
        val json = """
            {
                "id": "meta1",
                "titulo": "Aprender Kotlin",
                "descripcion": "Meta de prueba",
                "fechaLimite": 0,
                "fechaCreacion": 0,
                "niveles": [
                    {
                        "id": "nivel1",
                        "titulo": "Nivel 1",
                        "orden": 1,
                        "misiones": [
                            {
                                "id": "m1",
                                "titulo": "Variables",
                                "descripcion": "Aprender variables",
                                "orden": 1,
                                "completada": false
                            }
                        ]
                    }
                ],
                "fechaCompletada": null
            }
        """

        val meta = gson.fromJson(json, Meta::class.java)

        assertEquals("meta1", meta.id)
        assertEquals("Aprender Kotlin", meta.titulo)
        assertEquals(1, meta.niveles.size)
        assertEquals(1, meta.niveles[0].misiones.size)
        assertFalse(meta.niveles[0].misiones[0].completada)
    }

    @Test
    fun actualizarMisionCompletada() {
        val mision = Mision(
            id = "m1",
            titulo = "Variables",
            descripcion = "Aprender variables",
            orden = 1,
            completada = false
        )
        val nivel = Nivel(
            id = "nivel1",
            titulo = "Nivel 1",
            orden = 1,
            misiones = listOf(mision)
        )
        var meta = Meta(
            id = "meta1",
            titulo = "Aprender Kotlin",
            descripcion = "Meta de prueba",
            fechaLimite = 0L,
            fechaCreacion = 0L,
            niveles = listOf(nivel),
            fechaCompletada = null
        )
        val nivelesActualizados = meta.niveles.map { n ->
            n.copy(
                misiones = n.misiones.map { m ->
                    if (m.id == "m1") m.copy(completada = true) else m
                }
            )
        }
        meta = meta.copy(niveles = nivelesActualizados)
        assertTrue(meta.niveles[0].misiones[0].completada)
    }
}
