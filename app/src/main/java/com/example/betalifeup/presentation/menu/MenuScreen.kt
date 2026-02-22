package com.example.betalifeup.presentation.menu

import android.util.Log
import com.example.betalifeup.presentation.model.Meta
import androidx.compose.foundation.background
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import androidx.compose.foundation.clickable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.StrokeCap
import com.example.betalifeup.ui.theme.botonMorado
import com.example.betalifeup.ui.theme.principalNaranja
import com.example.betalifeup.ui.theme.secundarioAmarillo
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.foundation.layout.offset
import com.example.betalifeup.ui.theme.descripcionMetaCard
import com.example.betalifeup.ui.theme.fechaMetaCard
import com.example.betalifeup.ui.theme.metaCard
import com.example.betalifeup.ui.theme.progresoFondo
import com.example.betalifeup.ui.theme.tituloMetaCard
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import com.example.betalifeup.ui.theme.botonCompletarMeta
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navigateToCreator: () -> Unit = {},
    navigateToProfile: () -> Unit,
    navigateToMenuMeta: (String) -> Unit,
    viewModel: MenuViewModel = viewModel()
) {
    val metasList by viewModel.metas.collectAsState()
    val metasNoCompletadas = metasList.filter { it.fechaCompletada == null }
    var expandedMetaId by remember { mutableStateOf<String?>(null) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text="LISTA DE METAS",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp) },
                actions = {
                    IconButton(onClick = { navigateToProfile() }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Perfil",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = principalNaranja
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = secundarioAmarillo
            ){}
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navigateToCreator() },
                containerColor = botonMorado,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .offset(y = (100).dp)
                    .size(width = 220.dp, height = 60.dp)
            ){
                Text(
                    text = "Crear meta +",
                    fontSize = 20.sp
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ){ paddingValues ->
        if (metasNoCompletadas.isEmpty()){
            Spacer(modifier = Modifier.height(18.dp))
            Text(text="Aún no has creado ninguna misión", color = Color.White, fontSize = 20.sp)
        } else {
            if (viewModel.errorMessage.isNotBlank()){
                AlertDialog(
                    onDismissRequest = {viewModel.reiniciarError()},
                    text = {Text(text = viewModel.errorMessage)},
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.reiniciarError()
                            }
                        ) {
                            Text("Ok")
                        }
                    }
                )
            } else if (viewModel.metaActualizada){
                AlertDialog(
                    onDismissRequest = {viewModel.reiniciarMetaActualizada()},
                    text = {Text("¡Enhorabuena! 🎉 Has completado la meta. Bien hecho.")},
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.reiniciarMetaActualizada()
                            }
                        ) {
                            Text("Ok")
                        }
                    }
                )
            }
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .background(secundarioAmarillo)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    items = metasNoCompletadas,
                    key = { it.id }
                ) { meta ->
                    MetaItem(
                        meta = meta,
                        expanded = expandedMetaId == meta.id,
                        viewModel = viewModel,
                        userId = userId,
                        onClick = {expandedMetaId = if (expandedMetaId == meta.id) null else meta.id },
                        onComprobarMisionesClick = { navigateToMenuMeta(meta.id) }
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
    userId: String?,
    viewModel: MenuViewModel,
    onClick: () -> Unit,
    onComprobarMisionesClick: () -> Unit
){
    val progreso = meta.progresoMeta()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = metaCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ){
        Column(modifier = Modifier.padding(16.dp)){
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ){
                    Text(
                        text = meta.titulo,
                        color = tituloMetaCard,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = meta.descripcion,
                        color = descripcionMetaCard,
                        fontSize = 16.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Fecha límite: ${formatFecha(meta.fechaLimite)}",
                        fontSize = 14.sp,
                        color = fechaMetaCard
                    )
                }
                Box(
                    modifier = Modifier.width(72.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = meta.nivelMaximoMeta(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = tituloMetaCard
                    )
                }
            }
            LinearProgressIndicator(
                progress = progreso,
                color = com.example.betalifeup.ui.theme.progreso,
                trackColor = progresoFondo,
                strokeCap = StrokeCap.Round,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    if (meta.nivelMaximoMeta() == "MAX") {
                        Button(
                            onClick = {
                                if (userId != null) {
                                    viewModel.completarMeta(userId, meta.id)
                                }
                            },
                            modifier = Modifier.align(Alignment.End),
                            colors = ButtonDefaults.buttonColors(botonCompletarMeta)
                        ) {
                            Text(
                                text = "Completar meta",
                                color = Color.White
                            )
                        }
                    } else {
                        Button(
                            onClick = onComprobarMisionesClick,
                            modifier = Modifier.align(Alignment.End),
                            colors = ButtonDefaults.buttonColors(principalNaranja)
                        ) {
                            Text(
                                text = "Misiones disponibles",
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

fun formatFecha(timestamp: Long): String {
    if (timestamp == 0L) return "Sin fecha límite"
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}