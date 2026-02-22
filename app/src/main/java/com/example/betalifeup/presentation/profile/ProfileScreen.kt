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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.filled.Person
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.unit.sp

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
    val uiState by viewModel.uiState.collectAsState()
    var error by remember { mutableStateOf("") }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.onImagenSeleccionada(it)
        }
    }

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
            ProfileImage(
                imageUri = uiState.imagenUri,
                onClick = {
                    imagePicker.launch("image/*")
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Pulsa la imagen para cambiarla",
                color = Color.LightGray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text("Cuenta", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Email", fontWeight = FontWeight.Bold)
            Text(email)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Cambiar contraseña",
                color = Color.Blue,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { if (!desplegarCambioContrasena) desplegarCambioContrasena = true else desplegarCambioContrasena = false }
            )
            if (desplegarCambioContrasena) {
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
                        if (viewModel.contrasenaValida(nuevaContrasena)) {
                            viewModel.cambiarContrasena(nuevaContrasena)
                            nuevaContrasena = ""
                        }
                        else {
                            error = "La contraseña no es válida. Inténtalo de nuevo."
                            nuevaContrasena = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cambiar contraseña")
                }
                if (error.isNotEmpty()){
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = error,
                        color = Color.Red,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
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

@Composable
fun ProfileImage(
    imageUri: Uri?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Imagen de perfil",
                contentScale = ContentScale.Crop,
                modifier = modifier.fillMaxSize()
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
