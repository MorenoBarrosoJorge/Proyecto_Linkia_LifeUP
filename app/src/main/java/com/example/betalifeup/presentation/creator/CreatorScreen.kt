package com.example.betalifeup.presentation.creator

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.betalifeup.presentation.model.Meta
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Button
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun subirMeta(nombreMeta: String, descripcionMeta: String) { // Función que recoge los datos proporcionados por el usuario, crea un archivo JSON y los sube a Firebase.
    val user = FirebaseAuth.getInstance().currentUser ?: return // Inicializamos la variable que recoge el nombre de usuario actualmente logeado
    val uid = user.uid // Inicializamos la variable que recoge el identificador del usuario actualmente logeado.

    val database = FirebaseDatabase.getInstance().reference // Inicializamos la base de datos donde almacenaremos el archivo JSON.

    val metaId = database.child("metas").child(uid).push().key ?: return

    val meta = Meta( // TEMPORAL | Creamos un objeto "Meta", con atributos de "titulo" y "descripción"
        titulo = nombreMeta,
        descripcion = descripcionMeta
    )

    database.child("metas").child(uid).child(metaId).setValue(meta) // Navegamos por la base de datos para introducir correctamente los datos proporcionados por el usuario.
        .addOnSuccessListener {
            Log.i("Firebase", "Meta subida correctamente")
        }
        .addOnFailureListener {
            Log.e("Firebase", "Error al subir meta", it)
        }
}

@Composable
fun CreatorScreen(navigateToCustom: () -> Unit = {}) {

    Column (modifier = Modifier.fillMaxSize().background(color = Color.Black), horizontalAlignment = Alignment.CenterHorizontally){
        Spacer(modifier = Modifier.height(8.dp))
        Text(text="Elige una de las opciones para crear tu nueva meta:", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { navigateToCustom() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )
        {
            Text(text="Personalizada")
        }
        Spacer(modifier = Modifier.height(8.dp))

    }
}
