package com.example.hummet.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Homepage(
    onNavigateToCoding: () -> Unit,
    onNavigateToInterview: () -> Unit
) {
    Column {
        Text("Welcome to Hummet!")
        Button(onClick = onNavigateToCoding) {
            Text("Go to Coding")
        }
        Button(onClick = onNavigateToInterview) {
            Text("Go to Interview")
        }
    }
}