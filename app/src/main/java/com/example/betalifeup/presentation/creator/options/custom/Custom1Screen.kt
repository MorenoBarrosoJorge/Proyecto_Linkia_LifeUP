package com.example.betalifeup.presentation.creator.options.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color.Companion.White
import java.util.Calendar
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.example.betalifeup.ui.theme.botonBlacno
import com.example.betalifeup.ui.theme.botonMorado
import com.example.betalifeup.ui.theme.campoTexto
import com.example.betalifeup.ui.theme.campoTextoSeleccionado
import com.example.betalifeup.ui.theme.principalNaranja
import com.google.firebase.auth.FirebaseAuth


@Composable
fun TipsDialog(showTips: () -> Unit, tips: List<String>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Antes de empezar", fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))

            tips.forEachIndexed { index, tip ->
                Text("${index + 1}. $tip", fontSize = 16.sp, color = Color.DarkGray, modifier = Modifier.padding(bottom = 8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = showTips, modifier = Modifier.align(Alignment.End)) {
                Text("Entendido")
            }
        }
    }
}


@Composable
fun Custom1Screen(viewModel: CustomMetaViewModel, navigateToCustom2: () -> Unit = {}, navigateToMenu: () -> Unit, navigateBack: () -> Unit, auth: FirebaseAuth){
    val context = LocalContext.current
    val meta by viewModel.metaTemporal.collectAsState()
    var calendar = remember { Calendar.getInstance() }
    val timeText = if (meta.fechaLimite == 0L) {
            "Sin fecha límite"
        } else {
            val cal = Calendar.getInstance().apply {
                timeInMillis = meta.fechaLimite
            }
            "${cal.get(Calendar.DAY_OF_MONTH)}/${cal.get(Calendar.MONTH) + 1}/${cal.get(Calendar.YEAR)}"
        }
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth, 0, 0, 0)
            viewModel.setFechaLimite(calendar.timeInMillis)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    datePickerDialog.datePicker.minDate = System.currentTimeMillis()

    if (viewModel.mostrarTips) {
        TipsDialog(
            showTips = { viewModel.ocultarTips() },
            tips = listOf("Completa todos los campos", "Usa datos reales", "Revisa antes de guardar")
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(principalNaranja)
                .padding(24.dp),
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
            Spacer(Modifier.height(50.dp))
            Text(
                text = "Título de la meta",
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
            Spacer(Modifier.height(8.dp))
            TextField(
                value = meta.titulo,
                onValueChange = viewModel::setTitulo,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = campoTexto,
                    focusedContainerColor = campoTextoSeleccionado
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(50.dp))
            Text(
                text = "Definición de la meta",
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
            TextField(
                value = meta.descripcion,
                onValueChange = viewModel::setDescripcion,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = campoTexto,
                    focusedContainerColor = campoTextoSeleccionado
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(50.dp))
            Text(
                text = "*Opcional",
                fontSize = 12.sp,
                color = Color.White
            )
            Text(
                text = "Fecha seleccionada: ${timeText}",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { datePickerDialog.show() },
                modifier = Modifier
                    .width(250.dp)
                    .height(60.dp)
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(botonBlacno),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Text(
                    text = "Seleccionar fecha",
                    color = Color.DarkGray,
                    fontSize = 18.sp
                )
            }
            Spacer(Modifier.height(50.dp))
            Button(
                onClick = { navigateToCustom2() },
                modifier = Modifier
                    .width(250.dp)
                    .height(60.dp)
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(botonBlacno),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Text(
                    text = "Añadir niveles +",
                    color = Color.DarkGray,
                    fontSize = 18.sp
                )
            }
            Spacer(Modifier.height(150.dp))
            Button(
                onClick = {
                val userId = auth.currentUser?.uid
                if (!userId.isNullOrEmpty()) {
                    viewModel.guardarMeta(userId)
                    viewModel.reiniciarValoresMeta()
                    navigateToMenu()
                    }
                },
                modifier = Modifier
                    .width(250.dp)
                    .height(60.dp)
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(botonMorado),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Text(
                    text = "Subir Meta",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


