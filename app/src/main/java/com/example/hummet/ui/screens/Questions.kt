package com.example.hummet.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class Question(
    val id: Int,
    val title: String,
    val difficulty: String,
    val category: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionsScreen() {
    val sampleQuestions = listOf(
        Question(1, "What is polymorphism in OOP?", "Easy", "Languages"),
        Question(2, "Explain REST API principles", "Medium", "Backend"),
        Question(3, "What is useState in React?", "Easy", "Frontend"),
        Question(4, "Difference between SQL and NoSQL", "Medium", "Databases"),
        Question(5, "What is Docker and why use it?", "Medium", "DevOps"),
        Question(6, "Explain Android Activity Lifecycle", "Medium", "Mobile"),
        Question(7, "What are closures in JavaScript?", "Medium", "Languages"),
        Question(8, "Explain MVC architecture", "Easy", "Backend")
    )

    var selectedDifficulty by remember { mutableStateOf("All") }
    val difficulties = listOf("All", "Easy", "Medium", "Hard")

    val filteredQuestions = if (selectedDifficulty == "All") {
        sampleQuestions
    } else {
        sampleQuestions.filter { it.difficulty == selectedDifficulty }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Interview Questions",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                difficulties.forEach { difficulty ->
                    FilterChip(
                        selected = selectedDifficulty == difficulty,
                        onClick = { selectedDifficulty = difficulty },
                        label = { Text(difficulty) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredQuestions) { question ->
                    QuestionCard(question = question)
                }
            }
        }
    }
}

@Composable
fun QuestionCard(question: Question) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (question.difficulty) {
                "Easy" -> Color(0xFFE8F5E9)
                "Medium" -> Color(0xFFFFF3E0)
                "Hard" -> Color(0xFFFFEBEE)
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.QuestionAnswer,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (question.difficulty) {
                        "Easy" -> Color(0xFF4CAF50)
                        "Medium" -> Color(0xFFFF9800)
                        "Hard" -> Color(0xFFF44336)
                        else -> Color.Gray
                    }
                ) {
                    Text(
                        text = question.difficulty,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = question.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = question.category,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
