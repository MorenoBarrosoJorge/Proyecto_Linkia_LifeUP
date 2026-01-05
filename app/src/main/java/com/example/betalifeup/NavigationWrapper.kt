package com.example.betalifeup

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.betalifeup.presentation.initial.InitialScreen
import com.example.betalifeup.presentation.login.LoginScreen
import com.example.betalifeup.presentation.recoverCredentials.RecoverCredentialsScreen
import com.example.betalifeup.presentation.singup.SignupScreen
import com.google.firebase.auth.FirebaseAuth
import com.example.betalifeup.presentation.menu.MenuScreen
import com.example.betalifeup.presentation.creator.CreatorScreen
import com.example.betalifeup.presentation.creator.options.CustomScreen
import com.example.betalifeup.presentation.recoverCredentials.RecoverCredentialsScreen

@Composable
fun NavigationWrapper(navHostController: NavHostController, auth: FirebaseAuth){ // Le pasamos como parámetros el Nav y Firebase

    NavHost(navController = navHostController, startDestination="initial"){
        composable("initial"){
            InitialScreen( // Pantalla de inicio de sesión/registro
                navigateToLogin = {navHostController.navigate("login")},
                navigateToSignUp = {navHostController.navigate("signup")}
            )
        }
        composable("login"){
            LoginScreen( // Pantalla de inicio de sesión. Nos lleva a la pantalla de Menú si logeamos correctamente
                auth,
                navigateToMenu = {navHostController.navigate("menu")},
                navigateToRecoverCredentials = {navHostController.navigate("recoverCredentials")}
            ) //{ navHostController.navigate("menu") }
        }
        composable("signup"){
            SignupScreen( // Pantalla de registro de usuario. Nos lleva a la pantalla de Menú si registramos correctamente
                auth,
                navigateToMenu = {navHostController.navigate("menu")}
                )
        }
        composable("menu"){
            MenuScreen( // Pantalla de Menú de usuario. Accedemos solo con credenciales correctas
                navigateToCreator = {navHostController.navigate("creator")}
            )
        }
        composable("creator"){
            CreatorScreen( // Pantalla de creación de metas. Accedemos desde el Menú de usuario
                navigateToCustom = {navHostController.navigate("custom")}
            )
        }
        composable("custom"){
            CustomScreen(// Pantalla de creación de meta personalizada
            )
        }
        composable("recoverCredentials"){
            RecoverCredentialsScreen(
                auth,
                navigateToLogin = {navHostController.navigate("login")}
            )
        }
    }
}