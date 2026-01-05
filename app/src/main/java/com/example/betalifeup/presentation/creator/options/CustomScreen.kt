package com.example.betalifeup.presentation.creator.options

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.betalifeup.presentation.creator.subirMeta
import com.example.betalifeup.presentation.model.Meta
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

import androidx.compose.runtime.MutableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState


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
fun TipsDialog(showTips: () -> Unit, tips: List<String>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Antes de empezar", fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))

            tips.forEachIndexed { index, tip ->
                Text("${index + 1}. $tip", fontSize = 16.sp, color = Color.DarkGray, modifier = Modifier.padding(bottom = 8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = showTips, modifier = Modifier.align(Alignment.End)) {
                Text("Entendido")
            }
        }
    }
}


@Composable
fun CustomScreen() {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // Estado para mostrar los tips
    var showTips by remember { mutableStateOf(true) }

    // Lista de tips que quieras mostrar
    val tipsList = listOf(
        "Completa todos los campos",
        "Usa datos reales",
        "Revisa antes de guardar"
    )

    // Diálogo de tips
    if (showTips) {
        // Mostramos el diálogo profesional
        TipsDialog(showTips = { showTips = false }, tips = tipsList)
    } else {
        // Formulario normal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "¿Título de la meta?",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Título de la meta") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "¿Descripción de la meta?",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción de la meta") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { subirMeta(name, description) }) {
                Text("Subir meta")
            }
        }
    }
}
