package com.example.betalifeup.presentation.creator.options.custom

sealed class CustomUiEvent {
    data class ShowSnackbar(val message: String) : CustomUiEvent()
}