package com.example.betalifeup.presentation.creator.options.custom

import android.widget.Space
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

import androidx.compose.runtime.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import android.R.attr.onClick

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Custom2Screen(
    tituloMeta: String,
    onBack: () -> Unit,
    onAddMisiones: () -> Unit = {}
) {

    var niveles by remember { mutableStateOf(listOf<Nivel>()) }

    var mostrarDialogo by remember { mutableStateOf(false) }
    var numeroNivel = niveles.size+1
    var titulo = "Nivel $numeroNivel"

    var expandedNivelId by remember { mutableStateOf<Int?>(null) } // Variable que permite cerrar una Card que está expandida si el usuario pulsa sobre otra Card diferente

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
        // Voy a crear un nivel de manera automática al entrar en esta pantalla
        if (niveles.isEmpty()){

            val numeroNivel = niveles.size+1
            val nuevoNivel = Nivel(
                id = numeroNivel,
                titulo = "Nivel $numeroNivel"
            )
            niveles = niveles + nuevoNivel

            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ){
                items(
                    niveles,
                    key = {it.id}
                ) { nivel ->
                    NivelItem(
                        nivel = nivel,
                        expanded = expandedNivelId == nivel.id,
                        onClick = { expandedNivelId = if (expandedNivelId == nivel.id) null else nivel.id },
                        onAddMisionesClick = { onAddMisiones() }
                    )
                }
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ){
                items(
                    niveles,
                    key = {it.id}
                ) { nivel ->
                    NivelItem(
                        nivel = nivel,
                        expanded = expandedNivelId == nivel.id,
                        onClick = { expandedNivelId = if (expandedNivelId == nivel.id) null else nivel.id },
                        onAddMisionesClick = { onAddMisiones() }
                    )
                }
            }
        }
    }

    if (mostrarDialogo){
        AlertDialog(
            onDismissRequest = {
                mostrarDialogo = false
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
fun NivelItem(
    nivel: Nivel,
    expanded: Boolean,
    onClick: () -> Unit = {},
    onAddMisionesClick: () -> Unit = {}
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable{ onClick() }
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = nivel.titulo,
                style = MaterialTheme.typography.titleMedium
            )

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onAddMisionesClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Añadir / Modificar misiones")
                    }
                }
            }

        }

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
