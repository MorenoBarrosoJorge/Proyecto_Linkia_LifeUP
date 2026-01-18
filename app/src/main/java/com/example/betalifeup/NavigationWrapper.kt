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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.betalifeup.presentation.creator.options.custom.CustomMetaViewModel

@Composable
fun NavigationWrapper(navHostController: NavHostController, auth: FirebaseAuth){ // Le pasamos como parámetros el Nav y Firebase

    val customMetaViewModel: CustomMetaViewModel = viewModel()

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
                viewModel = customMetaViewModel,
                navigateToCustom2 = {navHostController.navigate("custom2")}
            )
        }
        composable("custom2") {
            Custom2Screen(
                viewModel = customMetaViewModel,
                onBack = {navHostController.popBackStack()},
                onNavigateToCustom3 = { nivelId -> navHostController.navigate("custom3/$nivelId") })
        }
        composable(
            route = "custom3/{nivelId}",
            arguments = listOf(
                navArgument("nivelId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nivelId = backStackEntry.arguments?.getString("nivelId") ?: ""
            Custom3Screen(
                nivelId = nivelId,
                onBack = { navHostController.popBackStack() },
                viewModel = customMetaViewModel
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