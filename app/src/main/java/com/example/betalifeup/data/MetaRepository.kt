package com.example.betalifeup.data

import android.util.Log
import com.example.betalifeup.presentation.model.Meta
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MetaRepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    fun escucharMetas(
        onResult: (List<Pair<String, Meta>>) -> Unit,
        onError: (Exception) -> Unit = {}
    ) {
        val user = auth.currentUser ?: return
        val uid = user.uid

        val ref = database
            .getReference("metas")
            .child(uid)

        ref.addValueEventListener(object : ValueEventListener { // Avisa de cada cambio en /metas/iud (como modificación de una meta, borrado de meta...)
            override fun onDataChange(snapshot: DataSnapshot) { // Se ejecuta al iniciarse por primera vez o al haber algún cambio en /metas/uid
                                                                // snapshot es una "foto" del estado actual de /metas/uid

                val metas = mutableListOf<Pair<String, Meta>>() // Ya que recoge de Firebase la estructura <ID Meta, Meta>, se utilizar "Pair" para poder recoger y guardar ambos valores juntos, es decir, el ID de la meta y los datos de la meta.

                for (child in snapshot.children) { // Por cada meta dentro de la lista de metas asignadas al usuario actual...
                    val meta = child.getValue(Meta::class.java) // Recoge cada uno de los campos de la meta
                    if (meta != null) { // En caso de encontrar campos en la meta...
                        metas.add(child.key!! to meta) // Recogemos su ID, almacenando dicho ID junto con los campos de su meta
                    }
                }
                onResult(metas) // Devolvemos la lista de metas perteneciente al usuario activo
            }

            override fun onCancelled(error: DatabaseError) { // En caso de pérdida de conexión o fallo en los permisos...
                Log.e("Firebase", "Error cargando metas", error.toException())
                onError(error.toException()) // Devolvemos la excepción que informa de ello
            }
        })
    }
}
