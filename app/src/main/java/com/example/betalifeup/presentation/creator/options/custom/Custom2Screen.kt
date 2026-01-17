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
import com.example.betalifeup.presentation.model.Nivel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.foundation.clickable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Custom2Screen(
    viewModel: CustomMetaViewModel,
    onBack: () -> Unit,
    onNavigateToCustom3: (nivelId: String) -> Unit
) {

    val meta by viewModel.metaTemporal.collectAsState()
    val niveles = meta.niveles
    var mostrarDialogo by remember { mutableStateOf(false) }
    var expandedNivelId by remember { mutableStateOf<String?>(null) } // Variable que permite cerrar una Card que está expandida si el usuario pulsa sobre otra Card diferente

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "META: ${meta.titulo}",
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

            viewModel.addNivel()
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
                        onAddMisionesClick = { onNavigateToCustom3(nivel.id) }
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
                        onAddMisionesClick = { onNavigateToCustom3(nivel.id) }
                    )
                }
            }
        }
    }

    if (mostrarDialogo){
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Nuevo nivel") },
            text = { Text(text="¿Quieres añadir un nuevo nivel?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.addNivel()
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
    onAddMisionesClick: (String) -> Unit = {}
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
                        // State hoisting + event callbacks
                        onClick = { onAddMisionesClick(nivel.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Añadir / Modificar misiones")
                    }
                }
            }

        }

    }
}
