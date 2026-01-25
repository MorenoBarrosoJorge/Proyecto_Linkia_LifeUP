package com.example.betalifeup

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.betalifeup.data.MetaRepository
import com.example.betalifeup.presentation.model.Meta
import com.example.betalifeup.presentation.model.Mision
import com.example.betalifeup.presentation.model.Nivel
import com.google.firebase.auth.FirebaseAuth
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class FirebaseMetaTest {

    private val repository = MetaRepository()

    @Test
    fun crearMisionFirebase() {
        val authLatch = CountDownLatch(1)
        var userId: String? = null

        FirebaseAuth.getInstance()
            .signInAnonymously()
            .addOnSuccessListener {
                userId = it.user?.uid
                authLatch.countDown()
            }
            .addOnFailureListener {
                authLatch.countDown()
            }

        authLatch.await(5, TimeUnit.SECONDS)
        assertNotNull("No se pudo autenticar usuario anónimo", userId)

        val mision = Mision(
            id = UUID.randomUUID().toString(),
            titulo = "Test misión",
            descripcion = "Descripción",
            orden = 1,
            completada = false
        )

        val nivel = Nivel(
            id = UUID.randomUUID().toString(),
            titulo = "Nivel Test",
            orden = 1,
            misiones = listOf(mision)
        )

        val meta = Meta(
            id = "",
            titulo = "Meta Test",
            descripcion = "Meta de prueba",
            fechaLimite = 0L,
            fechaCreacion = 0L,
            niveles = listOf(nivel),
            fechaCompletada = null
        )

        repository.subirMeta(userId!!, meta)

        val latch = CountDownLatch(1)
        var metaRecibida: Meta? = null

        repository.escucharMetas(
            onResult = { listaMetas ->
                metaRecibida = listaMetas.find { it.titulo == "Meta Test" }
                if (metaRecibida != null) {
                    latch.countDown()
                }
            },
            onError = {
                latch.countDown()
            }
        )

        latch.await(5, TimeUnit.SECONDS)

        assertNotNull("No se ha recibido ninguna meta de Firebase", metaRecibida)
        assertEquals("Meta Test", metaRecibida!!.titulo)
        assertEquals(1, metaRecibida!!.niveles.size)
        assertEquals(1, metaRecibida!!.niveles[0].misiones.size)
        assertEquals(
            "Test misión",
            metaRecibida!!.niveles[0].misiones[0].titulo
        )
    }
}
