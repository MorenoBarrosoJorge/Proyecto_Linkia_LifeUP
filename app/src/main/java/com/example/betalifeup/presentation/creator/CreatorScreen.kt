package com.example.betalifeup.presentation.creator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.betalifeup.ui.theme.principalNaranja
import com.example.betalifeup.ui.theme.tituloMetaCard
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedCard
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorScreen(navigateToCustom1: () -> Unit = {}, navigateToFast: () -> Unit, navigateBack: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(principalNaranja)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(8.dp))
        Row {
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
        Spacer(Modifier.height(140.dp))
        customOptionCard(
            titulo = "RÁPIDA",
            descripcion = "Deja que nuestra IA genera una meta por tí. ¡Solo dale una idea clara!",
            onClick = { navigateToFast() }
        )
        Spacer(Modifier.height(18.dp))
        customOptionCard(
            titulo = "PERSONALIZADA",
            descripcion = "Crea tu propia meta desde cero. ¡Manos a la obra!",
            onClick = { navigateToCustom1() }
        )
    }
}

@Composable
fun customOptionCard(
    titulo: String,
    descripcion: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pulsado by interactionSource.collectIsPressedAsState()

    OutlinedCard(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = Modifier
            .width(300.dp)
            .height(180.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (pulsado) principalNaranja.copy(alpha = 0.9f) else principalNaranja
        ),
        border = BorderStroke(
            2.dp,
            if (pulsado) tituloMetaCard else Color.Black.copy(alpha = 0.2f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = titulo,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = tituloMetaCard
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = descripcion,
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

