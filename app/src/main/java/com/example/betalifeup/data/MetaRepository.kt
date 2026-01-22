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
        onResult: (List<Meta>) -> Unit, // Recoge las metas del usaurio actual
        onError: (Exception) -> Unit = {} // Al detectar un error, se mostará un mensaje informativo en la consola
    ) {

        val uid = auth.currentUser?.uid
            ?: return // Guarda el id del usuario para poder recoger sus metas disponibles. En caso de no devolver ningun usuario, devuelve null para evitar crasheos

        database.child("users")
            .child(uid)
            .child("metas")
            .addValueEventListener(object : ValueEventListener { // Mistener en tiempo real
                override fun onDataChange(snapshot: DataSnapshot) { /*Se llama a esta función al entrar por primera vez en la pantalla "Menú" y cada vez que hay algún cambio en alguna de las metas activas
                                                                     El parámetro snapshot recoge el estado actual de las metas activas del usuario*/
                    val metas =
                        snapshot.children.mapNotNull { metaSnap -> // Se recoge cada una de las metas existentes (snapshot.children) y se convierten en objetos tipo "Meta" (.mapNotNull evita nulls)
                            val meta =
                                metaSnap.getValue(Meta::class.java) // Por cada meta, se recogen sus valores (titulo, descripción...) y se convierten de campos JSON a atributos de Meta
                            meta?.copy(
                                id = metaSnap.key ?: ""
                            ) // Añade el id de la meta y lo integra con el resto de atributos
                        }
                    onResult(metas) // Al obtener resultado, devuelve la lista de metas recogidas
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Error cargando metas", error.toException())
                    onError(error.toException()) // Devolvemos la excepción que informa de ello
                }
            })
    }

    fun escucharMetaNivelActual( //Similar a escucharMetas(), solo que esta función escucha la meta indicada a través de su ID
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

        // Primero obtenemos la meta completa
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
                val metaCompletada = nivelesActualizados.all { nivel ->
                    nivel.misiones.all { it.completada }
                }

                val updates = mutableMapOf<String, Any>(
                    "niveles" to nivelesActualizados
                )

                if (metaCompletada && meta.fechaCompletada == null) {
                    updates["fechaCompletada"] = System.currentTimeMillis()
                }

                // Sobrescribimos toda la lista de niveles
                metaRef.updateChildren(updates)
            }
        }.addOnFailureListener { e ->
            Log.e("Firebase", "Error actualizando misión", e)
        }
    }
}
