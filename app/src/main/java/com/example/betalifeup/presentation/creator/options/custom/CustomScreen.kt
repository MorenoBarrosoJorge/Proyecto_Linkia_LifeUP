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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

import android.app.DatePickerDialog
import java.util.Calendar
import androidx.compose.ui.platform.LocalContext

import androidx.lifecycle.viewmodel.compose.viewModel




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
fun CustomScreen(viewModel: CreatorViewModel = viewModel()){
    val context = LocalContext.current
    var calendar = Calendar.getInstance()

    var fechaLimite = viewModel.fechaLimite

    // Muestra la fecha límite de la meta
    val timeText =
        if (fechaLimite == 0L) {
            "Sin fecha límite"
        } else {
            val cal = Calendar.getInstance().apply {
                timeInMillis = fechaLimite
            }
            "${cal.get(Calendar.DAY_OF_MONTH)}/${cal.get(Calendar.MONTH) + 1}/${cal.get(Calendar.YEAR)}"
        }

    // Selección de fecha límite para la meta
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth, 0, 0, 0)
            viewModel.onFechaLimiteChange(calendar.timeInMillis)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Para impedir la selección de una fecha pasada
    datePickerDialog.datePicker.minDate = System.currentTimeMillis()

    if (viewModel.showTips) {
        TipsDialog(
            showTips = { viewModel.ocultarTips() },
            tips = listOf("Completa todos los campos", "Usa datos reales", "Revisa antes de guardar")
        )
    } else {
        Column(
            modifier = Modifier.fillMaxSize().background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextField(
                value = viewModel.titulo,
                onValueChange = viewModel::onTituloChange,
                label = { Text("Título de la meta") }
            )

            TextField(
                value = viewModel.descripcion,
                onValueChange = viewModel::onDescripcionChange,
                label = { Text("Descripción de la meta") }
            )

            Text(
                text = timeText,
                color = Color.LightGray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { datePickerDialog.show() }) {
                Text("Seleccionar fecha")
            }

            Button(
                onClick = {
                    viewModel.subirMeta(
                        onSuccess = {
                            // navegación, toast, limpiar campos…
                        },
                        onError = {
                            // mostrar error
                        }
                    )
                }
            ) {
                Text("Subir meta")
            }
        }
    }
}


