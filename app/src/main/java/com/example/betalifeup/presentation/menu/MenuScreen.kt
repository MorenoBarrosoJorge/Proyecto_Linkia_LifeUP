package com.example.betalifeup.presentation.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import android.R.attr.text
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import com.example.betalifeup.ui.theme.Black
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@Composable
fun MenuScreen(navigateToCreator: () -> Unit = {}){ // Pasamos como parámetro el navegador a la pantalla de creación de metas
    Column(Modifier.fillMaxSize().background(Black), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(text="Feed de metas", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 30.sp)
        Spacer(modifier = Modifier.weight(1f))
        Button(
            /*
            * Al hacer click, pueden ocurrir 2 escenarios:
            *
            * 1. El usuario aún puede crear metas, por lo que se accede a la pantalla de creación.
            * 2. El usuario ya tiene el máximo número de metas activas para su perfil, por lo que se mostrará un aviso informando de esto indicando que, para crear una nueva meta, debe eliminar una de las actualmente activas.
            * */
            onClick = { navigateToCreator() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )
        {
            Text(text="Crear nueva meta", color = Color.White, fontSize = 20.sp)
        }
    }
}

//Crea una preview