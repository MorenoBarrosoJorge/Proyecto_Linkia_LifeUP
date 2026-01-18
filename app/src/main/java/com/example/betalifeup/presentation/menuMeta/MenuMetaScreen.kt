package com.example.betalifeup.presentation.menuMeta

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MenuMetaScreen(metaId: String, onBack: () -> Unit, viewModel: MenuMetaViewModel = viewModel()){
    Text("Misiones disponibles para la meta", color = Color.Black)
}