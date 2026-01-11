package com.example.betalifeup.presentation.creator.options.custom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height

import androidx.compose.material3.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Custom2Screen(
    tituloMeta: String,
    onBack: () -> Unit,
    onAddNivel: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "META: $tituloMeta",
                        maxLines = 1, // Para evitar que la barra crezca verticalmente
                        overflow = TextOverflow.Ellipsis // Añade puntos suspensivos en caso de tener un título muy largo
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNivel
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir nivel +"
                )
            }
        }
    ) { paddingValues -> // Espacio de TopAppBar

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Aquí irá el contenido de niveles
        }
    }
}



//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        Text(
//            text="META: $tituloMeta",
//            fontSize = 22.sp,
//            fontWeight = FontWeight.Bold
//            )
//
//        Spacer(modifier = Modifier.height(16.dp))
//    }
