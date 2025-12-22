package com.example.betalifeup.presentation.singup

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.betalifeup.R
import com.example.betalifeup.ui.theme.Black
import com.example.betalifeup.ui.theme.SelectedField
import com.example.betalifeup.ui.theme.UnselectedField
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignupScreen(auth: FirebaseAuth, navigateToMenu: () -> Unit) { // Pasamos como parámetros el autentificador de credenciales y el navegador al menú.
    var email by remember { mutableStateOf("") } // Inicializamos la variable de "email"
    var password by remember { mutableStateOf("") } // Inicializamos la variable de "password"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(){
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
            text="Introduce un correo electrónico",
            color = White,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
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
            text="Introduce una contraseña",
            color = White,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
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
        Button(onClick = {
            /*
            * Llamamos a la función del autentificador para crear nuevas credenciales de acceso.
            *
            * Si las credenciales no concuerdan con ningunas de las almacenadas en Firebase, se crearán y guardarán para futuros logeos.
            *
            * Si las credenciales corresponden a alguna ya existente en Firebase, se mostará mensaje de error, eliminando las credenciales introducidas por el usuario en los TextField para que vuelva a intentar introducir credenciales únicas.
            * */
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Log.i("Correcto","SIGN UP OK")
                    navigateToMenu()

                } else {
                    Log.i("Incorrecto","SIGN UP KO")
                }
            }
        }) {
            Text("Registro")
        }
    }
}