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

    private lateinit var navHostController: NavHostController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        enableEdgeToEdge()
        @Suppress("UnusedMaterial3ScaffoldPaddingParameter")
        setContent {
            navHostController = rememberNavController()
            NavigationWrapper(navHostController, auth)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser!=null){
            Log.i("Correcto", "Logeado")
            auth.signOut()
        } else {
            Log.i("Incorrecto", "No logeado")
        }
    }

}
