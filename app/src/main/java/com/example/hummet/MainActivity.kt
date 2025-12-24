package com.example.hummet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hummet.ui.components.BottomBar
import com.example.hummet.ui.screens.Homepage
import com.example.hummet.ui.screens.CodingScreen
import com.example.hummet.ui.screens.QuestionsScreen
import com.example.hummet.ui.screens.QuizScreen
import com.example.hummet.ui.theme.HummetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HummetTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentRoute == "home" || currentRoute == "questions" || currentRoute == "coding") {
                            BottomBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                            Homepage(
                                onNavigateToCoding = { navController.navigate("coding") },
                                onNavigateToQuestions = { navController.navigate("questions") }
                            )
                        }
                        composable("questions") {
                            QuestionsScreen(navController = navController)
                        }
                        composable("coding") {
                            CodingScreen()
                        }
                        composable(
                            route = "quiz/{topicId}",
                            arguments = listOf(navArgument("topicId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val topicId = backStackEntry.arguments?.getInt("topicId") ?: 0
                            QuizScreen(
                                topicId = topicId,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}