package com.example.betalifeup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import android.util.Log

class MainActivity : ComponentActivity() {

    private lateinit var navHostController: NavHostController // Variable para poder navegar entre pantallas
    private lateinit var auth: FirebaseAuth // Variable para la autentificación de credenciales de usuario

    //Añadir una variable para la base de datos de Realtime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth // Se inicializa la variable de Firebase
        enableEdgeToEdge()
        @Suppress("UnusedMaterial3ScaffoldPaddingParameter")
        setContent {
            navHostController = rememberNavController()
            NavigationWrapper(navHostController, auth)
        }
    }

    override fun onStart() { // Al iniciar sesión, podremos aparecer logeados o no
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser!=null){
            // Volver al menú de inicio de sesión
            Log.i("Correcto", "Logeado")
            auth.signOut()
        } else {
            Log.i("Incorrecto", "No logeado")
        }
    }

}
