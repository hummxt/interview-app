package com.example.hummet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hummet.ui.components.BottomBar
import com.example.hummet.ui.components.Screen
import com.example.hummet.ui.screens.Homepage
import com.example.hummet.ui.screens.Coding
import com.example.hummet.ui.screens.Interview
import com.example.hummet.ui.theme.HummetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HummetTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomBar(navController = navController)
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Home.route) {
                            Homepage(
                                onNavigateToCoding = { navController.navigate(Screen.Coding.route) },
                                onNavigateToInterview = { navController.navigate(Screen.Interview.route) }
                            )
                        }
                        composable(Screen.Interview.route) { Interview() }
                        composable(Screen.Coding.route) { Coding() }
                    }
                }
            }
        }
    }
}