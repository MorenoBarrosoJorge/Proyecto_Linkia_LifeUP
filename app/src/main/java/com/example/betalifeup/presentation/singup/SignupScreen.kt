package com.example.betalifeup.presentation.singup

import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.betalifeup.ui.theme.campoTexto
import com.example.betalifeup.ui.theme.campoTextoSeleccionado
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.example.betalifeup.ui.theme.botonDeshabilitado
import com.example.betalifeup.ui.theme.botonMorado
import com.example.betalifeup.ui.theme.principalNaranja
import com.example.betalifeup.ui.theme.secundarioAmarillo
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import com.google.firebase.auth.FirebaseAuthUserCollisionException

@Composable
fun SignupScreen(auth: FirebaseAuth, navigateToMenu: () -> Unit, navigateBack: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmarPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(
                secundarioAmarillo, principalNaranja
            ), startY = 0f, endY = 800f))
            .padding(32.dp)
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
            text = "Introduce un correo electrónico",
            textAlign = TextAlign.Left,
            color = White,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = campoTexto,
                focusedContainerColor = campoTextoSeleccionado
            )
        )
        Spacer(Modifier.height(48.dp))
        Text(
            text = "Introduce una contraseña",
            textAlign = TextAlign.Left,
            color = White,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        )
        TextField(
            value = password,
            onValueChange = { password = it },
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
        Text(
            text = "(Mínimo 10 caracteres)",
            textAlign = TextAlign.Left,
            color = Color.White,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(48.dp))
        Text(
            text = "Confirma la contraseña",
            textAlign = TextAlign.Left,
            color = White,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        )
        TextField(
            value = confirmarPassword,
            onValueChange = { confirmarPassword = it },
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
        Spacer(Modifier.height(48.dp))
        if (message.isNotEmpty()){
            Text(
                text = message,
                color = Color.Red,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
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
                    if (password!=confirmarPassword){
                        message = "Las contraseñas no coinciden. Vuelve a intentarlo."
                        return@Button
                    }
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.i("Correcto", "SIGN UP OK")
                            navigateToMenu()
                        } else {
                            val excepcion = task.exception
                            when (excepcion) {
                                is FirebaseAuthUserCollisionException -> {
                                    message = "El correo que has introducido ya tiene una cuenta asociada."
                                }
                                else -> message = "Error inesperado: ${excepcion?.localizedMessage}"
                            }
                        }
                    }
                },
                enabled = contrasenaValida(password, confirmarPassword),
                colors = ButtonDefaults.buttonColors(
                    containerColor = botonMorado,
                    contentColor = Color.White,
                    disabledContainerColor = botonDeshabilitado,
                    disabledContentColor = Color.DarkGray
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 0.dp
                ),
                modifier = Modifier.size(width = 220.dp, height = 60.dp)
            ) {
                Text("Registrarme")
            }
        }
    }
}

fun contrasenaValida(password: String, confirmarPassword: String): Boolean{
    return password.length > 9 &&
            password.any { it.isDigit() } &&
            password.any { it.isLowerCase() } &&
            password.any { it.isUpperCase() } &&
            confirmarPassword.length > 9 &&
            confirmarPassword.any { it.isDigit() } &&
            confirmarPassword.any { it.isLowerCase() } &&
            confirmarPassword.any { it.isUpperCase() }

}


