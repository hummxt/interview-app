package com.example.hummet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hummet.ui.components.BottomBar
import com.example.hummet.ui.screens.home.Homepage
import com.example.hummet.ui.screens.quiz.QuestionsScreen
import com.example.hummet.ui.screens.interview.InterviewScreen
import com.example.hummet.ui.screens.profile.ProfileScreen
import com.example.hummet.ui.screens.settings.SettingsScreen
import com.example.hummet.ui.theme.HummetTheme
import com.example.hummet.ui.theme.ThemeConfig
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val systemInDarkTheme = isSystemInDarkTheme()
            LaunchedEffect(Unit) {
                // Initialize theme only if it's the first run or follow system
                ThemeConfig.isDarkMode = systemInDarkTheme
            }

            HummetTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val showBottomBar = currentRoute in listOf("home", "questions", "interview", "profile")
                        if (showBottomBar) {
                            BottomBar(navController = navController)
                        }
                    }
                ) { innerPadding: PaddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                            Homepage(
                                onNavigateToInterview = { navController.navigate("interview") },
                                onNavigateToQuestions = { navController.navigate("questions") }
                            )
                        }
                        composable("questions") {
                            QuestionsScreen(navController = navController)
                        }
                        composable("interview") {
                            InterviewScreen()
                        }
                        composable("profile") {
                            ProfileScreen(
                                onNavigateToSettings = { navController.navigate("settings") }
                            )
                        }
                        composable("settings") {
                            SettingsScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}