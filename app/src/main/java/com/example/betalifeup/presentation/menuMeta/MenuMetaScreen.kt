package com.example.betalifeup.presentation.menuMeta

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.betalifeup.presentation.model.Mision
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuMetaScreen(
    metaId: String,
    onBack: () -> Unit,
    viewModel: MenuMetaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    val nivelActual = uiState.nivelActual

    LaunchedEffect(metaId) {
        viewModel.escucharMetaNivelActual(metaId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(uiState.meta?.titulo ?: "Meta")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (viewModel.errorMessage.isNotBlank()){
            AlertDialog(
                onDismissRequest = {viewModel.reiniciarError()},
                text = {Text(text = viewModel.errorMessage)},
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.reiniciarError()
                        }
                    ) {
                        Text("Ok")
                    }
                }
            )
        }
        when {
            uiState.cargando -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = uiState.error ?: "Error desconocido")
                }
            }
            uiState.nivelActual == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Meta completada")
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Text(
                        text = "Nivel actual: ${nivelActual?.titulo}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = uiState.misionesVisibles,
                            key = { it.id }
                        ) { mision ->
                            nivelActual?.let { nivel ->
                                var confirmarMision by remember { mutableStateOf(false) }
                                if (confirmarMision) {
                                    AlertDialog(
                                        onDismissRequest = { confirmarMision = false },
                                        title = { Text("Confirmar misión completada") },
                                        text = { Text("¿Marcar la misión como completada?") },
                                        confirmButton = {
                                            TextButton(onClick = {
                                                viewModel.completarMision(
                                                    userId = userId,
                                                    metaId = metaId,
                                                    nivelId = nivel.id,
                                                    misionId = mision.id
                                                )
                                                confirmarMision = false
                                            }) {
                                                Text("Sí")
                                            }
                                        },
                                        dismissButton = {
                                            TextButton(onClick = { confirmarMision = false }) {
                                                Text("No")
                                            }
                                        }
                                    )
                                }
                                MisionNivelItem(
                                    mision = mision,
                                    onCompletar = {
                                        confirmarMision = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisionNivelItem(
    mision: Mision,
    onCompletar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(enabled = !mision.completada) {
                onCompletar()
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = mision.titulo,
                style = MaterialTheme.typography.titleMedium
            )
            if (mision.descripcion.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = mision.descripcion)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (mision.completada) "✅ Completada" else "⬜ Pendiente",
                color = if (mision.completada)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}



