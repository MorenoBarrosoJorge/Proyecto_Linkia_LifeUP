package com.example.betalifeup.data

import android.util.Log
import com.example.betalifeup.presentation.model.Meta
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MetaRepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    fun subirMeta(userID: String, meta: Meta) {

        val uid = userID
        val metaRef = database
            .child("users")
            .child(uid)
            .child("metas")
            .push()

        val metaFechaCreacion = meta.copy(
            id = metaRef.key ?: "",
            fechaCreacion = System.currentTimeMillis()
        )
        metaRef.setValue(metaFechaCreacion)
    }

    fun escucharMetas(
        onResult: (List<Meta>) -> Unit,
        onError: (Exception) -> Unit = {}
    ) {

        val uid = auth.currentUser?.uid
            ?: return

        database.child("users")
            .child(uid)
            .child("metas")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val metas =
                        snapshot.children.mapNotNull { metaSnap ->
                            val meta =
                                metaSnap.getValue(Meta::class.java)
                            meta?.copy(
                                id = metaSnap.key ?: ""
                            )
                        }
                    onResult(metas)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Error cargando metas", error.toException())
                    onError(error.toException())
                }
            })
    }

    fun escucharMetaNivelActual(
        metaId: String,
        onResult: (Meta) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: return

        database.child("users")
            .child(uid)
            .child("metas")
            .child(metaId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val meta = snapshot.getValue(Meta::class.java)
                    if (meta != null) {
                        onResult(meta.copy(id = snapshot.key ?: ""))
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Error escuchando meta", error.toException())
                    onError(error.toException())
                }
            }
            )
    }

    fun actualizarEstadoMision(
        userId: String,
        metaId: String,
        nivelId: String,
        misionId: String
    ) {
        val metaRef = database
            .child("users")
            .child(userId)
            .child("metas")
            .child(metaId)

        metaRef.get().addOnSuccessListener { snapshot ->
            val meta = snapshot.getValue(Meta::class.java)
            if (meta != null) {
                val nivelesActualizados = meta.niveles.map { nivel ->
                    if (nivel.id == nivelId) {
                        val misionesActualizadas = nivel.misiones.map { mision ->
                            if (mision.id == misionId) mision.copy(completada = true)
                            else mision
                        }
                        nivel.copy(misiones = misionesActualizadas)
                    } else nivel
                }
                val updates = mutableMapOf<String, Any>(
                    "niveles" to nivelesActualizados
                )
                metaRef.updateChildren(updates)
            }
        }.addOnFailureListener { e ->
            Log.e("Firebase", "Error actualizando misión", e)
        }
    }

    fun marcarMetaComoCompletada(
        userId: String,
        metaId: String
    ) {
        database
            .child("users")
            .child(userId)
            .child("metas")
            .child(metaId)
            .child("fechaCompletada")
            .setValue(System.currentTimeMillis())
    }

    fun eliminarMeta(userId: String, metaId: String) {
        database.child("users")
            .child(userId)
            .child("metas")
            .child(metaId)
            .removeValue()
            .addOnSuccessListener {
                Log.d("Firebase", "Meta eliminada correctamente")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error eliminando meta", e)
            }
    }
}
