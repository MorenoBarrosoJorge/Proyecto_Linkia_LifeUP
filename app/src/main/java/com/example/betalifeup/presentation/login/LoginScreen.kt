package com.example.betalifeup.presentation.login

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.material3.Icon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.text.style.TextDecoration
import com.example.betalifeup.ui.theme.botonMorado
import com.example.betalifeup.ui.theme.campoTexto
import com.example.betalifeup.ui.theme.campoTextoSeleccionado
import com.example.betalifeup.ui.theme.principalNaranja
import com.example.betalifeup.ui.theme.secundarioAmarillo
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun LoginScreen(auth: FirebaseAuth, viewModel: LoginViewModel = viewModel(), navigateToMenu: () -> Unit = {}, navigateBack: () -> Unit, navigateToRecoverCredentials: () -> Unit = {}) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(
                secundarioAmarillo, principalNaranja
            ), startY = 0f, endY = 800f))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(Modifier.height(8.dp))
        Row{
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    tint = Color.White,
                    contentDescription = "Volver a inicio",
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(Modifier.weight(1f))
        }
        Spacer(Modifier.height(50.dp))
        Text(
            text="Correo electrónico",
            color = White,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp
        )
        TextField(
            value=email,
            onValueChange = {email = it},
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = campoTexto,
                focusedContainerColor = campoTextoSeleccionado
            )
        )
        Spacer(Modifier.height(48.dp))
        Text(
            text="Contraseña",
            color = White,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp
        )
        TextField(
            value=password,
            onValueChange = {password = it},
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility
                else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = campoTexto,
                focusedContainerColor = campoTextoSeleccionado
            )
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "He olvidado mi contraseña",
            color = Blue,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                navigateToRecoverCredentials()
            }
        )
        Spacer(Modifier.height(48.dp))
        if (viewModel.errorMessage.isNotEmpty()) {
            Text(
                text = viewModel.errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))
        }
        Button(
            onClick = {
                viewModel.validarCredenciales(email = email, password = password)
                if (viewModel.errorMessage.isEmpty()){
                    viewModel.iniciarUsuario(auth = auth, email = email, password = password)
                    if (viewModel.confirmar){
                        navigateToMenu()
                    }
                }
        },
            colors = ButtonDefaults.buttonColors(botonMorado),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 6.dp,
                pressedElevation = 0.dp
            ),
            modifier = Modifier.size(width = 220.dp, height = 60.dp)
            ) {
            Text(
                text = "Iniciar sesión",
                fontSize = 24.sp,
                color = Color.White
            )
        }
        Spacer(Modifier.height(12.dp))
    }
}


