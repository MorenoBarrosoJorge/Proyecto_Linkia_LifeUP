package com.example.betalifeup.presentation.creator.options.custom


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import com.example.betalifeup.presentation.model.Mision
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


@Composable
fun Custom3Screen(nivelId: Int, viewModel: Custom2ViewModel = viewModel()){

    val misionesPorNivel by viewModel.misionesNivel.collectAsState()
    val misiones = misionesPorNivel[nivelId].orEmpty()
    var expandedMisionId by remember { mutableStateOf<Int?>(null) } // Variable que permite cerrar una Card que está expandida si el usuario pulsa sobre otra Card diferente

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text="Misiones de Nivel $nivelId",
            color = Color.White,
            fontSize = 40.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (misiones.isEmpty()){
            Text(
                text="Aún no se ha creado ninguna misión",
                color = Color.LightGray,
                fontSize = 16.sp
            )
        } else {
            LazyColumn {
                items(
                    items = misiones,
                    key = { it.id }
                ) { mision ->
                    MisionItem(
                        mision = mision,
                        expanded = expandedMisionId == mision.id,
                        onClick = {expandedMisionId = if (expandedMisionId == mision.id) null else mision.id},
                        onModificarMisionClick = { println("Misión modificada") },
                        onEliminarMisionClick = { println("Misión eliminada") }
                    )
                }
            }
        }
        FloatingActionButton(
            onClick = {
                viewModel.addMision(
                    nivelId = nivelId,
                    titulo = "Misión ${misiones.size+1} de prueba de nivel $nivelId",
                    descripcion = "Testeando creación de misiones"
                )
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Añadir misión"
            )
        }
    }
}


@Composable
fun MisionItem(
    mision: Mision,
    expanded: Boolean,
    onClick: () -> Unit = {},
    onModificarMisionClick: () -> Unit = {},
    onEliminarMisionClick: () -> Unit = {}
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() }
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ){
            Text(
                text = mision.titulo,
                color = Color.DarkGray,
                style = MaterialTheme.typography.titleMedium
            )
            if (mision.descripcion.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = mision.descripcion,
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            // State hoisting + event callbacks
                            onClick = onModificarMisionClick
                        ) {
                            Text("Modificar")
                        }
                        Button(
                            // State hoisting + event callbacks
                            onClick = onEliminarMisionClick
                        ) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }
    }
}
