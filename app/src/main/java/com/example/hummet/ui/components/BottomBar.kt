package com.example.hummet.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

data class ScreenRoute(val route: String, val label: String, val icon: ImageVector)

object NavigationRoutes {
    val Home = ScreenRoute("home", "Home", Icons.Default.Home)
    val Quiz = ScreenRoute("quiz", "Quiz", Icons.Default.QuestionAnswer)
    val Interview = ScreenRoute("interview", "Interview", Icons.Outlined.People)
    val Coding = ScreenRoute("coding", "Coding", Icons.Default.Code)
}

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        NavigationRoutes.Home,
        NavigationRoutes.Quiz,
        NavigationRoutes.Interview,
        NavigationRoutes.Coding
    )

    NavigationBar {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry.value?.destination

        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}