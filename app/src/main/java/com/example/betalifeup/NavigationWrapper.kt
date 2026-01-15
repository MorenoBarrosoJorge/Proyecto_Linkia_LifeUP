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
import com.example.betalifeup.presentation.creator.options.custom.Custom1Screen
import com.example.betalifeup.presentation.creator.options.custom.Custom2Screen
import com.example.betalifeup.presentation.creator.options.custom.Custom3Screen
import androidx.navigation.navArgument
import androidx.navigation.NavType

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
                navigateToCustom1 = {navHostController.navigate("custom1")}
            )
        }
        composable("custom1"){
            Custom1Screen(// Pantalla de creación de meta personalizada / Título, descripción y fecha límite de la meta
                navigateToCustom2 = {titulo -> navHostController.navigate("custom2/$titulo")}
            )
        }
        composable(
            route = "custom2/{tituloMeta}", // Ruta dinámica
            arguments = listOf( // Para poder pasar valores entre pantallas (nombre del valor){tipo del valor}
                navArgument("tituloMeta") { type = NavType.StringType }
            )
        ) { backStackEntry -> // Objeto que recoge los valores enviados
            val tituloMeta = backStackEntry.arguments?.getString("tituloMeta") ?: "" // Recoge el valor de "tituloMeta"
            Custom2Screen(navController = navHostController, tituloMeta = tituloMeta, onBack = {navHostController.popBackStack()})
        }
        composable(
            route = "custom3/{nivelId}",
            arguments = listOf( // Para poder pasar valores entre pantallas (nombre del valor){tipo del valor}
                navArgument("nivelId") { type = NavType.IntType }
            )
        ){ backStackEntry ->
            val nivelId = backStackEntry.arguments?.getInt("nivelId") ?: 0
            Custom3Screen(nivelId = nivelId, onBack = {navHostController.popBackStack()})
        }
        composable("recoverCredentials"){
            RecoverCredentialsScreen(
                auth,
                navigateToLogin = {navHostController.navigate("login")}
            )
        }
    }
}