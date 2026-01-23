package com.example.betalifeup.presentation.initial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import com.example.betalifeup.ui.theme.botonBlacno
import com.example.betalifeup.ui.theme.botonMorado
import com.example.betalifeup.ui.theme.principalNaranja
import com.example.betalifeup.ui.theme.secundarioAmarillo


@Composable
fun InitialScreen(navigateToLogin: () -> Unit = {}, navigateToSignUp: () -> Unit = {}){

    Column(
        modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(
            secundarioAmarillo, principalNaranja
        ), startY = 0f, endY = 800f)), horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(300.dp))
        Text("Life Up", color = Color.White, fontSize = 60.sp, fontWeight = FontWeight.Bold)
        Text("Tu app para reforzar la rutina", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(340.dp))
        Button(
            onClick = { navigateToLogin() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(botonBlacno),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 6.dp,
                pressedElevation = 0.dp
            )
        )
        {
            Text(text="Iniciar sesión", color = Color.Black, fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text="¿No tienes cuenta? Empecemos el registro", color = Color.White, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Button(
            onClick = { navigateToSignUp() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(botonMorado),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 6.dp,
                pressedElevation = 0.dp
            )
        )
        {
            Text(text="Registro", color = Color.White, fontSize = 24.sp)
        }
    }
}