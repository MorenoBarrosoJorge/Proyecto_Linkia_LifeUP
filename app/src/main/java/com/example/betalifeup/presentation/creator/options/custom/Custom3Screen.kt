package com.example.betalifeup.presentation.creator.options.custom


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
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


@Composable
fun Custom3Screen(nivelId: Int, viewModel: Custom2ViewModel = viewModel()){

    val misionesPorNivel by viewModel.misionesNivel.collectAsState()
    val misiones = misionesPorNivel[nivelId].orEmpty()


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
                    MisionItem(mision = mision)
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
fun MisionItem(mision: Mision){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
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
        }
    }
}
