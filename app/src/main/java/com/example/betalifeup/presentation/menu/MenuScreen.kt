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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import java.text.SimpleDateFormat
import java.util.*

import androidx.compose.runtime.*

import androidx.compose.foundation.clickable
import androidx.compose.animation.AnimatedVisibility


fun formatFecha(timestamp: Long): String {
    if (timestamp == 0L) return "Sin fecha límite"

    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@Composable
fun MenuScreen(
    navigateToCreator: () -> Unit = {},
    viewModel: MenuViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val metas = viewModel.metas

    var expandedMetaId by remember { mutableStateOf<String?>(null) } //

    Column(
        Modifier
            .fillMaxSize()
            .background(Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Feed de metas",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        metas.forEach { (metaId, meta) ->

            val isExpanded = expandedMetaId == metaId //

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable {
                        expandedMetaId = if (isExpanded) null else metaId
                    },
                colors = CardDefaults.cardColors(
                    containerColor = Color.DarkGray
                )
            ){
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ){
                    Text(
                        text = meta.titulo ?: "",
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = meta.descripcion ?: "",
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Fecha límite: ${formatFecha(meta.fechaLimite)}",
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    if (isExpanded) {
                        Spacer(modifier = Modifier.height(8.dp))

                        AnimatedVisibility(visible = isExpanded) {
                            Button(
                                onClick = { },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Comprobar misiones")
                            }
                        }
                    }

                }
            }

        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { navigateToCreator() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text(text = "Crear nueva meta", color = Color.White, fontSize = 20.sp)
        }
    }
}




//Crea una preview