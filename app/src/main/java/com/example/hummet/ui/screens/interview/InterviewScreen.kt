package com.example.hummet.ui.screens.interview

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class InterviewTopic(
    val id: Int,
    val title: String,
    val description: String,
    val difficulty: DifficultyLevel,
    val category: Category,
    val questionsCount: Int,
    val estimatedTime: String,
    val isCompleted: Boolean = false,
    val progress: Float = 0f
)

enum class DifficultyLevel(val label: String, val color: Color) {
    JUNIOR("Junior", Color(0xFF10B981)),
    MIDDLE("Middle", Color(0xFFF59E0B)),
    SENIOR("Senior", Color(0xFFEF4444))
}

enum class Category(val label: String, val icon: ImageVector, val gradient: List<Color>) {
    WEB("Web", Icons.Outlined.Language, listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8))),
    MOBILE("Mobile", Icons.Outlined.PhoneAndroid, listOf(Color(0xFF10B981), Color(0xFF059669))),
    DESKTOP("Desktop", Icons.Outlined.Computer, listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9))),
    BACKEND("Backend", Icons.Outlined.Storage, listOf(Color(0xFFEC4899), Color(0xFFDB2777))),
    FRONTEND("Frontend", Icons.Outlined.Palette, listOf(Color(0xFF14B8A6), Color(0xFF0D9488))),
    DEVOPS("DevOps", Icons.Outlined.CloudQueue, listOf(Color(0xFFF97316), Color(0xFFEA580C))),
    DATABASE("Database", Icons.Outlined.TableChart, listOf(Color(0xFF6366F1), Color(0xFF4F46E5))),
    ARCHITECTURE("Architecture", Icons.Outlined.AccountTree, listOf(Color(0xFFEC4899), Color(0xFF9333EA))),
    TESTING("Testing", Icons.Outlined.BugReport, listOf(Color(0xFFFBBF24), Color(0xFFF59E0B))),
    SECURITY("Security", Icons.Outlined.Security, listOf(Color(0xFFEF4444), Color(0xFFDC2626)))
}

val sampleTopics = listOf(
    InterviewTopic(1, "Object-Oriented Programming", "Core OOP concepts: encapsulation and inheritance.", DifficultyLevel.JUNIOR, Category.BACKEND, 24, "45 min"),
    InterviewTopic(2, "Kotlin Coroutines", "Structured concurrency and Flow.", DifficultyLevel.MIDDLE, Category.MOBILE, 32, "60 min"),
    InterviewTopic(3, "Jetpack Compose", "Modern declarative UI and State management.", DifficultyLevel.MIDDLE, Category.MOBILE, 28, "55 min"),
    InterviewTopic(4, "Clean Architecture", "SOLID principles and layered architecture.", DifficultyLevel.SENIOR, Category.ARCHITECTURE, 20, "50 min")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterviewScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedDifficulty by remember { mutableStateOf<DifficultyLevel?>(null) }

    val isDark = isSystemInDarkTheme()
    val primaryTextColor = if (isDark) Color.White else Color.Black
    val secondaryTextColor = if (isDark) Color.LightGray else Color.Gray
    val accentButtonColor = if (isDark) Color.White else Color.Black
    val accentButtonTextColor = if (isDark) Color.Black else Color.White
    val containerColor = if (isDark) Color(0xFF1E1E1E) else Color(0xFFF0F0F0)
    val dailyCardColor = if (isDark) Color(0xFF0D2F47) else Color(0xFFC3E7FF)
    val dailyCardTextColor = if (isDark) Color.White else Color.Black

    val filteredTopics = remember(searchQuery, selectedCategory, selectedDifficulty) {
        sampleTopics.filter { topic ->
            val matchesSearch = searchQuery.isEmpty() || topic.title.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == null || topic.category == selectedCategory
            val matchesDifficulty = selectedDifficulty == null || topic.difficulty == selectedDifficulty
            matchesSearch && matchesCategory && matchesDifficulty
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Interview Prep",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = primaryTextColor
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ProgressOverviewCard(
                    dailyCardColor = dailyCardColor,
                    dailyCardTextColor = dailyCardTextColor,
                    accentButtonColor = accentButtonColor,
                    accentButtonTextColor = accentButtonTextColor
                )
            }

            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search interview topics...", color = secondaryTextColor, fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = primaryTextColor, modifier = Modifier.size(20.dp)) },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = containerColor,
                        unfocusedContainerColor = containerColor,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = primaryTextColor,
                        unfocusedTextColor = primaryTextColor,
                        cursorColor = primaryTextColor
                    )
                )
            }

            item {
                CategoryFilterRow(
                    selectedCategory = selectedCategory,
                    accentColor = accentButtonColor,
                    accentTextColor = accentButtonTextColor,
                    containerColor = containerColor,
                    textColor = primaryTextColor,
                    onCategorySelected = { selectedCategory = if (selectedCategory == it) null else it }
                )
            }

            item {
                DifficultyFilterRow(
                    selectedDifficulty = selectedDifficulty,
                    accentColor = accentButtonColor,
                    accentTextColor = accentButtonTextColor,
                    containerColor = containerColor,
                    textColor = primaryTextColor,
                    onDifficultySelected = { selectedDifficulty = if (selectedDifficulty == it) null else it }
                )
            }

            item {
                Text(
                    "Topics (${filteredTopics.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = primaryTextColor
                )
            }

            if (filteredTopics.isEmpty()) {
                item { EmptyState(secondaryTextColor) }
            } else {
                items(filteredTopics, key = { it.id }) { topic ->
                    TopicCard(
                        topic = topic,
                        primaryTextColor = primaryTextColor,
                        secondaryTextColor = secondaryTextColor,
                        containerColor = containerColor,
                        onClick = { }
                    )
                }
            }
        }
    }
}

@Composable
fun ProgressOverviewCard(
    dailyCardColor: Color,
    dailyCardTextColor: Color,
    accentButtonColor: Color,
    accentButtonTextColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = dailyCardColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Your Progress", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = dailyCardTextColor)
                    Text("Ready for your next challenge?", style = MaterialTheme.typography.bodyMedium, color = dailyCardTextColor.copy(alpha = 0.8f))
                }
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { 0.35f },
                        modifier = Modifier.size(44.dp),
                        color = dailyCardTextColor,
                        strokeWidth = 4.dp,
                        trackColor = dailyCardTextColor.copy(0.2f)
                    )
                    Text("35%", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = dailyCardTextColor)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = accentButtonColor, contentColor = accentButtonTextColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Continue Learning")
            }
        }
    }
}

@Composable
fun CategoryFilterRow(
    selectedCategory: Category?,
    accentColor: Color,
    accentTextColor: Color,
    containerColor: Color,
    textColor: Color,
    onCategorySelected: (Category) -> Unit
) {
    Column {
        Text("Categories", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = textColor, modifier = Modifier.padding(bottom = 8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(Category.entries) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { onCategorySelected(category) },
                    label = { Text(category.label, fontSize = 12.sp) },
                    shape = RoundedCornerShape(12.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = accentColor,
                        selectedLabelColor = accentTextColor,
                        containerColor = containerColor,
                        labelColor = textColor
                    ),
                    border = null
                )
            }
        }
    }
}

@Composable
fun DifficultyFilterRow(
    selectedDifficulty: DifficultyLevel?,
    accentColor: Color,
    accentTextColor: Color,
    containerColor: Color,
    textColor: Color,
    onDifficultySelected: (DifficultyLevel) -> Unit
) {
    Column {
        Text("Difficulty Level", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = textColor, modifier = Modifier.padding(bottom = 8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DifficultyLevel.entries.forEach { difficulty ->
                FilterChip(
                    modifier = Modifier.weight(1f),
                    selected = selectedDifficulty == difficulty,
                    onClick = { onDifficultySelected(difficulty) },
                    label = { Text(difficulty.label, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 12.sp) },
                    shape = RoundedCornerShape(12.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = accentColor,
                        selectedLabelColor = accentTextColor,
                        containerColor = containerColor,
                        labelColor = textColor
                    ),
                    border = null
                )
            }
        }
    }
}

@Composable
fun TopicCard(
    topic: InterviewTopic,
    primaryTextColor: Color,
    secondaryTextColor: Color,
    containerColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(44.dp).background(Brush.linearGradient(topic.category.gradient), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(topic.category.icon, null, tint = Color.White, modifier = Modifier.size(22.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(topic.title, fontWeight = FontWeight.Bold, color = primaryTextColor, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("${topic.questionsCount} Questions • ${topic.estimatedTime}", style = MaterialTheme.typography.bodySmall, color = secondaryTextColor)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(topic.description, color = secondaryTextColor, fontSize = 14.sp, maxLines = 2, lineHeight = 20.sp)
        }
    }
}

@Composable
fun EmptyState(textColor: Color) {
    Box(Modifier.fillMaxWidth().padding(60.dp), contentAlignment = Alignment.Center) {
        Text("No results found", color = textColor, fontWeight = FontWeight.Medium)
    }
}