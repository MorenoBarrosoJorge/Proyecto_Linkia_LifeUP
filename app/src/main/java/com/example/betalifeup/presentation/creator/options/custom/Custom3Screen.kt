package com.example.betalifeup.presentation.creator.options.custom


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import com.example.betalifeup.presentation.model.Mision
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import com.example.betalifeup.presentation.model.Nivel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Custom3Screen(nivelId: String, onBack: () -> Unit, viewModel: CustomMetaViewModel = viewModel()) {

    val meta by viewModel.metaTemporal.collectAsState()
    val nivel = meta.niveles.find { it.id == nivelId }
    val misiones = nivel?.misiones.orEmpty()
    var expandedMisionId by remember { mutableStateOf<String?>(null) } // Variable que permite cerrar una Card que está expandida si el usuario pulsa sobre otra Card diferente
    var mostrarDialogo by remember { mutableStateOf(false) }
    var mostrarModificar by remember { mutableStateOf(false) }
    var mostrarEliminar by remember { mutableStateOf(false) }
    var misionSeleccionadaId by remember { mutableStateOf<String?>(null) }
    var nuevoTituloMision by remember { mutableStateOf("") }
    var nuevaDescripcionMision by remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "${nivel?.titulo?: ""} / Misiones",
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
                onClick = { mostrarDialogo = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir misión"
                )
            }
        }
    ) { paddingValues ->

        if (misiones.isEmpty()) {
            Column{
                Spacer(modifier = Modifier.padding(paddingValues))
                Text(
                    text = "Aún no se ha creado ninguna misión",
                    color = Color.DarkGray,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                items(
                    items = misiones,
                    key = { it.id }
                ) { mision ->
                    MisionItem(
                        mision = mision,
                        expanded = expandedMisionId == mision.id,
                        onClick = {
                            expandedMisionId =
                                if (expandedMisionId == mision.id) null else mision.id
                        },
                        onModificarMisionClick = { misionId ->
                            misionSeleccionadaId = misionId
                            nuevoTituloMision = mision.titulo
                            nuevaDescripcionMision = mision.descripcion
                            mostrarModificar = true
                                                 },
                        onEliminarMisionClick = { misionId ->
                            misionSeleccionadaId = misionId
                            mostrarEliminar = true
                        }
                    )
                }
            }
        }
    }
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogo = false
            },
            title = { Text("Nueva misión") },
            text = { Text(text = "¿Quieres añadir una misión nueva?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.addMision(
                            nivelId = nivelId,
                            titulo = "Misión ${misiones.size+1}",
                            descripcion = "Testeando creación de misiones"
                        )
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
    if (mostrarModificar) {
        AlertDialog(
            onDismissRequest = { mostrarModificar = false },
            title = { Text("Modificar misión") },
            text = {
                Column {
                    OutlinedTextField(
                        value = nuevoTituloMision,
                        onValueChange = { nuevoTituloMision = it },
                        label = { Text("Título") },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = nuevaDescripcionMision,
                        onValueChange = { nuevaDescripcionMision = it },
                        label = { Text("Descripción") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        misionSeleccionadaId?.let { misionId ->
                            viewModel.updateMision(
                                nivelId = nivelId,
                                misionId = misionId,
                                nuevoTitulo = nuevoTituloMision,
                                nuevaDescripcion = nuevaDescripcionMision
                            )
                        }
                        nuevoTituloMision = ""
                        nuevaDescripcionMision = ""
                        misionSeleccionadaId = null
                        mostrarModificar = false
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    nuevoTituloMision = ""
                    nuevaDescripcionMision = ""
                    misionSeleccionadaId = null
                    mostrarModificar = false
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
    if (mostrarEliminar){
        AlertDialog(
            onDismissRequest = { mostrarEliminar = false },
            title = { Text("Modificar misión") },
            text = { Text("¿Estás seguro? Esta acción no se puede deshacer") },
            confirmButton = {
                TextButton(
                    onClick = {
                        misionSeleccionadaId?.let { misionId ->
                            viewModel.deleteMision(
                                nivelId = nivelId,
                                misionId = misionId
                            )
                        }
                        misionSeleccionadaId = null
                        mostrarModificar = false
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    misionSeleccionadaId = null
                    mostrarModificar = false
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun MisionItem(
    mision: Mision,
    expanded: Boolean,
    onClick: () -> Unit = {},
    onModificarMisionClick: (String) -> Unit = {},
    onEliminarMisionClick: (String) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() }
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = mision.titulo,
                color = Color.DarkGray,
                style = MaterialTheme.typography.titleMedium
            )
            if (mision.descripcion.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = mision.descripcion,
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            // State hoisting + event callbacks
                            onClick = { onModificarMisionClick(mision.id) }
                        ) {
                            Text("Modificar")
                        }
                        Button(
                            // State hoisting + event callbacks
                            onClick = { onEliminarMisionClick(mision.id) }
                        ) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }
    }
}
