package com.example.betalifeup.presentation.menu

import com.example.betalifeup.presentation.model.Meta
import androidx.compose.foundation.background
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.style.TextOverflow
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import androidx.compose.foundation.clickable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.text.style.TextAlign


fun formatFecha(timestamp: Long): String {
    if (timestamp == 0L) return "Sin fecha límite"

    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navigateToCreator: () -> Unit = {},
    navigateToCustom2: (metaId: String) -> Unit = {},
    viewModel: MenuViewModel = viewModel()
) {
    val metasList by viewModel.metas.collectAsState() // Lista de objetos tipo Meta
    var expandedMetaId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text="LISTA DE METAS",
                        color = Color.Black,
                        fontSize = 24.sp) }
            )
        },
        containerColor = Color.Black,
        bottomBar = {
            Button(
                onClick = { navigateToCreator() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(text = "Nueva meta", color = Color.White, fontSize = 20.sp)
            }
        }
    ){ paddingValues ->
        if (metasList.isEmpty()){
            Spacer(modifier = Modifier.height(16.dp))
            Text(text="Aún no has creado ninguna misión", color = Color.White, fontSize = 20.sp)
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    items = metasList,
                    key = { it.id } // ID de cada meta
                ) { meta ->
                    MetaItem(
                        meta = meta,
                        expanded = expandedMetaId == meta.id,
                        onClick = {expandedMetaId = if (expandedMetaId == meta.id) null else meta.id },
                        onComprobarMisionesClick = {/*Comprobar misiones disponibles en el nivel actual*/}
                    )
                }
            }
        }
    }
}

@Composable
fun MetaItem(
    meta: Meta,
    expanded: Boolean,
    onClick: () -> Unit,
    onComprobarMisionesClick: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ){
        Column(modifier = Modifier.padding(16.dp)){
            Text(
                text = meta.titulo,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = meta.descripcion,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Fecha límite: ${formatFecha(meta.fechaLimite)}",
                color = Color.LightGray
            )

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onComprobarMisionesClick,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Misiones disponibles")
                    }
                }
            }
        }
    }
}