package com.example.betalifeup.presentation.login

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Icon
import androidx.compose.ui.text.font.FontWeight
import com.example.betalifeup.R
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
import com.example.betalifeup.ui.theme.SelectedField
import com.example.betalifeup.ui.theme.UnselectedField
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.text.style.TextDecoration


@Composable
fun LoginScreen(auth: FirebaseAuth, navigateToMenu: () -> Unit = {}, navigateToRecoverCredentials: () -> Unit = {}) { //Le pasamos también una función que permita navegar hasta el menú principal
    var email by remember { mutableStateOf("") } // Inicializamos la variable de "email"
    var password by remember { mutableStateOf("") } // Inicializamos la variable de "password"
    var errorMessage by remember { mutableStateOf("") }   // Inicializamos la variable que mostará mensaje de eror en caso de credenciales incorrectas

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row{
            Icon(
                painter = painterResource(id = R.drawable.ic_back_24),
                contentDescription = "",
                tint = White,
                modifier = Modifier.padding(vertical = 24.dp)
                    .size(24.dp)
            )
            Spacer(Modifier.weight(1f))
        }
        Text(
            text="Correo electrónico",
            color = White,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp
        )
        TextField(
            value=email,
            onValueChange = {email = it},
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = UnselectedField,
                focusedContainerColor = SelectedField
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
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = UnselectedField,
                focusedContainerColor = SelectedField
            )
        )
        Spacer(Modifier.height(48.dp))

        // Mensaje de error en caso de credenciales incorrectas
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))
        }

        Button(onClick = {
            /*
            * Llamamos a uno de los métodos de la variable 'auth', correspondiente al autentificador de credenciales.
            *
            * Si las credenciales corresponden a las de un usuario registrado en Firebase, podremos navegar hasta su correspondiente "Menú de usuario".
            *
            * Si las credenciales no corresponden a ningún usuario, se mostrará un mensaje de error, eliminando las credenciales introducidas por el usuario para que vuela a escribirlas.
            * */
            errorMessage = ""

            // Comprobamos si el correo existe
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navigateToMenu()
                    } else {
                        val exception = task.exception
                        if (exception is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException){
                            errorMessage = "Credenciales incorrectas. Vuelve a intentarlo."
                        } else {
                            errorMessage = "Error al iniciar sesión. Inténtalo de nuevo."
                        }
                        email = ""
                        password = ""
                    }
                }
        }) {
            Text("Iniciar sesión")
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = "He olvidado mi contraseña",
            color = Blue,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                navigateToRecoverCredentials()
            }
        )
    }
}


