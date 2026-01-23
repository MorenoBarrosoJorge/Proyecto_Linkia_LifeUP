package com.example.betalifeup.presentation.recoverCredentials

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.betalifeup.ui.theme.botonBlacno
import com.example.betalifeup.ui.theme.campoTexto
import com.example.betalifeup.ui.theme.campoTextoSeleccionado
import com.example.betalifeup.ui.theme.principalNaranja
import com.example.betalifeup.ui.theme.secundarioAmarillo
import android.util.Patterns

@Composable
fun RecoverCredentialsScreen(auth: FirebaseAuth, navigateToLogin: () -> Unit, navigateBack: () -> Unit){
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(
                secundarioAmarillo, principalNaranja
            ), startY = 0f, endY = 800f))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            text="Recuperar credenciales",
            textAlign = TextAlign.Center,
            color = White,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Indícanos tu correo electrónico. Si figura en nuestra base de datos, te enviaremos un correo con las instrucciones para crear una nueva contraseña.",
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 14.sp

        )
        Spacer(Modifier.height(8.dp))
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
        if (message.isNotEmpty()) {
            if (message.contains("intentarlo")||message.contains("válido")) {
                Text(
                    text = message,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
                Spacer(Modifier.height(24.dp))
            } else {
                Text(
                    text = message,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Green
                )
                Spacer(Modifier.height(24.dp))
            }
        }
        Button(
            onClick = {
            message = ""
                if (email.isBlank()) {
                    message = "No has introducido ningún correo. Vuelve a intentarlo."
                    return@Button
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    message = "Introduce un correo electrónico válido."
                    return@Button
                }
            auth.sendPasswordResetEmail(email.trim())
                .addOnCompleteListener { task ->
                    message = if (task.isSuccessful) {
                        "Se ha enviado un correo de recuperación."
                    } else {
                        "Error al enviar correo. Inténtalo de nuevo."
                    }
                }
        },
            colors = ButtonDefaults.buttonColors(botonBlacno),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 6.dp,
                pressedElevation = 0.dp
            ),
            modifier = Modifier.size(width = 220.dp, height = 60.dp)
        ) {
            Text(
                text = "Enviar correo",
                fontSize = 24.sp,
                color = Color.Black
                )
        }
    }
}
