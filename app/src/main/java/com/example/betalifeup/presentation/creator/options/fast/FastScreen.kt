package com.example.betalifeup.presentation.creator.options.fast

import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import com.example.betalifeup.presentation.model.Meta
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FastScreen(
    onBack: () -> Unit,
    onMetaConfirmada: () -> Unit,
    viewModel: FastViewModel = viewModel()
){
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.metaGuardada) {
        if (uiState.metaGuardada) {
            onMetaConfirmada()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear meta con IA") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.prompt,
                onValueChange = viewModel::recogerPrompt,
                label = { Text("Indica el objetivo de tu meta") },
                placeholder = { Text("Ej: Aprender Python desde cero") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.cargando
            )
            Button(
                onClick = { viewModel.generarMetaPrompt() },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.prompt.isNotBlank() && !uiState.cargando
            ) {
                Text("Generar meta")
            }
            if (uiState.cargando) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }
            uiState.metaGenerada?.let { meta ->
                MetaPreviewCard(
                    meta = meta,
                    onConfirmar = {
                        val userId = FirebaseAuth
                            .getInstance()
                            .currentUser
                            ?.uid
                            .orEmpty()

                        viewModel.confirmarMetaPrompt(userId)
                    }
                )
            }
        }
    }
}
@Composable
fun MetaPreviewCard(
    meta: Meta,
    onConfirmar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = meta.titulo,
                style = MaterialTheme.typography.titleMedium
            )
            if (meta.descripcion.isNotBlank()) {
                Text(meta.descripcion)
            }
            Divider()
            meta.niveles.forEach { nivel ->
                Text(
                    text = "• ${nivel.titulo}",
                    style = MaterialTheme.typography.bodyMedium
                )
                nivel.misiones.forEach { mision ->
                    Text(
                        text = "    Mision: ${mision.titulo}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onConfirmar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar meta")
            }
        }
    }
}
