package com.example.betalifeup.presentation.recoverCredentials

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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun RecoverCredentialsScreen(auth: FirebaseAuth, navigateToLogin: () -> Unit){ // Pasamos como parámetros la autentificación y el navegador a "inicio de sesión"
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(32.dp)) {
        Text("Recuperar contraseña", fontSize = 24.sp)
        TextField(value = email, onValueChange = { email = it }, label = { Text("Correo") })

        if (message.isNotEmpty()) {
            Text(message, color = Color.Green)
        }

        Button(onClick = {
            message = ""
            auth.sendPasswordResetEmail(email.trim())
                .addOnCompleteListener { task ->
                    message = if (task.isSuccessful) {
                        "Se ha enviado un correo de recuperación."
                    } else {
                        "Error al enviar correo. Inténtalo de nuevo."
                    }
                }
        }) {
            Text("Enviar correo")
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "Volver al login",
            color = Color.Cyan,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable { navigateToLogin() }
        )
    }
}
