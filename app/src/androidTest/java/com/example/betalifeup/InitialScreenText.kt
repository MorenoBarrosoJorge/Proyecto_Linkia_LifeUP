package com.example.betalifeup

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InitialScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun pantallaInicial_seMuestraCorrectamente() {

        composeRule
            .onNodeWithText("Life Up")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Tu app para reforzar la rutina")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Iniciar sesión")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Registro")
            .assertIsDisplayed()
    }
}
