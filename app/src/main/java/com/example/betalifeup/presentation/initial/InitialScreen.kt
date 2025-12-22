package com.example.betalifeup.presentation.initial

import android.text.Layout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import com.example.betalifeup.ui.theme.Black
import com.example.betalifeup.ui.theme.Gray
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import com.example.betalifeup.R
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import com.example.betalifeup.ui.theme.BackgroundButton
import com.example.betalifeup.ui.theme.ShapeButton
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.painter.Painter


@Preview
@Composable
fun InitialScreen(navigateToLogin: () -> Unit = {}, navigateToSignUp: () -> Unit = {}){ // Pasamos como parámetros el navegador a la pantalla de inicio de sesión y registro.
    Column(
        modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Gray, Black), startY = 0f, endY = 600f)), horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.weight(1f))
        //Image(painter = painterResource(id = R.drawable.spotifylogo), contentDescription = "Logo Spotify")
        Text("Life Up", color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.Bold)
        Text("Tu app para reforzar la rutina", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { navigateToSignUp() }, // Al hacer click, navegamos hasta la pantalla de registro de usuario
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )
        {
            Text(text="Registro", color = Color.White, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        //CustomButton(Modifier.clickable{}, "Google")
        //Spacer(modifier = Modifier.height(8.dp))
        Text(text="¿Ya tienes una cuenta? Entonces no esperes más", color = Color.Gray, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(1.dp))
        Text(text="Iniciar sesión",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(24.dp)
                .clickable{navigateToLogin()}) // Al hacer click, navegamos hasta la pantalla de inicio de sesión

    }
}

@Composable
fun CustomButton(modifier: Modifier, title: String){ // Botón modificado, reutilizable al ser una función
    Box(modifier = modifier
        .fillMaxWidth()
        .height(48.dp)
        .padding(horizontal = 32.dp)
        .background(
        BackgroundButton
    )
        .border(2.dp, ShapeButton, CircleShape)
        , contentAlignment = Alignment.CenterStart)
    {
        Text(text=title, color = Color.White, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    }
}