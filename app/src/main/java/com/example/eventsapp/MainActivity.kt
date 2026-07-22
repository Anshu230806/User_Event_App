package com.example.eventsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
//import dagger.hilt.android.AndroidEntryPoint

//@AndroidEntryPoint

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventTheme{
               // val navController = rememberNavController()
               // we assigned it in EventNavGraph fn Because of that default assignment (= rememberNavController()), 
               //when Google calls TodoNavGraph() in MainActivity, Kotlin automatically generates 
               // and manages a single instance of rememberNavController under the hood
                EventNavGraph()
            }
        }
    }
}
