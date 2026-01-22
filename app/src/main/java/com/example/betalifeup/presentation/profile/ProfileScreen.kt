package com.example.betalifeup.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navigateBack: () -> Unit,
    navigateToInitial: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {

    val email by viewModel.email.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    var desplegarCambioContrasena by remember { mutableStateOf(false) }
    var nuevaContrasena by remember { mutableStateOf("") }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("MI PERFIL") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver a menú"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    viewModel.cerrarSesion()
                    navigateToInitial()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Cerrar sesión")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Cuenta", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Email", fontWeight = FontWeight.Bold)
            Text(email)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text="Cambiar contraseña",
                color = Color.Blue,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable{ desplegarCambioContrasena = true }
                )
            if (desplegarCambioContrasena) {
                /*Expandir y mostrar objetos necesarios para realizar cambio de contraseña*/
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = nuevaContrasena,
                    onValueChange = { nuevaContrasena = it },
                    label = { Text("Nueva contraseña") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        if (nuevaContrasena.length >= 6) {
                            viewModel.cambiarContrasena(nuevaContrasena)
                            nuevaContrasena = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cambiar contraseña")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Total metas completadas: 5 (Ejemplo)")
        }
    }
    mensaje?.let {
        AlertDialog(
            onDismissRequest = { viewModel.limpiarMensaje() },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.limpiarMensaje()
                    desplegarCambioContrasena = false
                }) {
                    Text("OK")
                }
            },
            text = { Text(it) }
        )
    }
}