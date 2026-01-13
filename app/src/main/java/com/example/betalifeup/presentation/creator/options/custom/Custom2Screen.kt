package com.example.betalifeup.presentation.creator.options.custom

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.material3.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.example.betalifeup.presentation.model.Nivel

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Custom2Screen(
    tituloMeta: String,
    onBack: () -> Unit
) {

    var niveles by remember { mutableStateOf(listOf<Nivel>()) }

    var mostrarDialogo by remember { mutableStateOf(false) }
    var tituloNivel by remember { mutableStateOf("") }
    var numeroNivel = niveles.size+1
    var titulo = "Nivel $numeroNivel"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "META: $tituloMeta",
                        maxLines = 1, // Para evitar que la barra crezca verticalmente
                        overflow = TextOverflow.Ellipsis // Añade puntos suspensivos en caso de tener un título muy largo
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {mostrarDialogo = true}
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir nivel +"
                )
            }
        }
    ) { paddingValues -> // Espacio de TopAppBar

        if (niveles.isEmpty()){

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ){
                Text(text="Aún no has añadido ningún nivel")
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ){
                items(niveles){ nivel ->
                    NivelItem(nivel = nivel)
                }
            }
        }
    }

    if (mostrarDialogo){
        AlertDialog(
            onDismissRequest = {
                mostrarDialogo = false
                tituloNivel = ""
            },
            title = { Text("Nuevo nivel") },
            text = { Text(text="¿Quieres añadir un nuevo nivel?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val numeroNivel = niveles.size+1
                        val nuevoNivel = Nivel(
                            id = numeroNivel,
                            titulo = "Nivel $numeroNivel"
                        )
                        niveles = niveles + nuevoNivel
                        mostrarDialogo = false
                    }
                ) {
                    Text("Añadir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarDialogo = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun NivelItem(nivel: Nivel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = nivel.titulo,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}


//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        Text(
//            text="META: $tituloMeta",
//            fontSize = 22.sp,
//            fontWeight = FontWeight.Bold
//            )
//
//        Spacer(modifier = Modifier.height(16.dp))
//    }
