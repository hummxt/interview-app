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

data class StatItem(
    val label: String,
    val value: String,
    val icon: ImageVector,
    val color: Color
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
    InterviewTopic(4, "Clean Architecture", "SOLID principles and layered architecture.", DifficultyLevel.SENIOR, Category.ARCHITECTURE, 20, "50 min"),
    InterviewTopic(5, "REST API Design", "RESTful principles and best practices.", DifficultyLevel.MIDDLE, Category.BACKEND, 18, "40 min"),
    InterviewTopic(6, "SQL & Database Design", "Queries, indexing, and normalization.", DifficultyLevel.JUNIOR, Category.DATABASE, 30, "50 min"),
    InterviewTopic(7, "Docker & Containers", "Containerization and orchestration basics.", DifficultyLevel.MIDDLE, Category.DEVOPS, 22, "45 min"),
    InterviewTopic(8, "Unit Testing", "TDD, mocking, and test strategies.", DifficultyLevel.JUNIOR, Category.TESTING, 26, "40 min")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterviewScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedDifficulty by remember { mutableStateOf<DifficultyLevel?>(null) }
    var showStats by remember { mutableStateOf(false) }

    val isDark = isSystemInDarkTheme()
    val primaryTextColor = if (isDark) Color.White else Color.Black
    val secondaryTextColor = if (isDark) Color.LightGray else Color.Gray
    val accentButtonColor = if (isDark) Color.White else Color.Black
    val accentButtonTextColor = if (isDark) Color.Black else Color.White
    val containerColor = if (isDark) Color(0xFF1E1E1E) else Color(0xFFF0F0F0)
    val dailyCardColor = if (isDark) Color(0xFF0D2F47) else Color(0xFFC3E7FF)
    val dailyCardTextColor = if (isDark) Color.White else Color.Black
    val streakCardColor = if (isDark) Color(0xFF2D1B47) else Color(0xFFE9D5FF)
    val achievementCardColor = if (isDark) Color(0xFF1F3A1F) else Color(0xFFD1FAE5)

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
                actions = {
                    IconButton(onClick = { showStats = !showStats }) {
                        Icon(
                            if (showStats) Icons.Outlined.GridView else Icons.Outlined.BarChart,
                            contentDescription = null,
                            tint = primaryTextColor
                        )
                    }
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
                AnimatedVisibility(visible = showStats) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatsGrid(
                            primaryTextColor = primaryTextColor,
                            secondaryTextColor = secondaryTextColor,
                            containerColor = containerColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }

            item {
                ProgressOverviewCard(
                    dailyCardColor = dailyCardColor,
                    dailyCardTextColor = dailyCardTextColor,
                    accentButtonColor = accentButtonColor,
                    accentButtonTextColor = accentButtonTextColor
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StreakCard(
                        modifier = Modifier.weight(1f),
                        cardColor = streakCardColor,
                        textColor = dailyCardTextColor
                    )
                    AchievementCard(
                        modifier = Modifier.weight(1f),
                        cardColor = achievementCardColor,
                        textColor = dailyCardTextColor
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search interview topics...", color = secondaryTextColor, fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = primaryTextColor, modifier = Modifier.size(20.dp)) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, null, tint = secondaryTextColor, modifier = Modifier.size(18.dp))
                            }
                        }
                    },
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Topics (${filteredTopics.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = primaryTextColor
                    )
                    if (selectedCategory != null || selectedDifficulty != null) {
                        TextButton(onClick = {
                            selectedCategory = null
                            selectedDifficulty = null
                        }) {
                            Text("Clear filters", fontSize = 12.sp)
                        }
                    }
                }
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
fun StatsGrid(
    primaryTextColor: Color,
    secondaryTextColor: Color,
    containerColor: Color
) {
    val stats = listOf(
        StatItem("Topics Completed", "12", Icons.Outlined.CheckCircle, Color(0xFF10B981)),
        StatItem("Hours Practiced", "18.5", Icons.Outlined.Schedule, Color(0xFF3B82F6)),
        StatItem("Success Rate", "87%", Icons.Outlined.TrendingUp, Color(0xFFF59E0B)),
        StatItem("Current Streak", "7 days", Icons.Outlined.LocalFireDepartment, Color(0xFFEF4444))
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        stats.chunked(2).forEach { rowStats ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowStats.forEach { stat ->
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = containerColor)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(36.dp).background(stat.color.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(stat.icon, null, tint = stat.color, modifier = Modifier.size(18.dp))
                            }
                            Column {
                                Text(
                                    stat.value,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = primaryTextColor
                                )
                                Text(
                                    stat.label,
                                    fontSize = 11.sp,
                                    color = secondaryTextColor,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
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
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Keep Going Strong!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = dailyCardTextColor
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "You're 35% through this month's goals",
                        style = MaterialTheme.typography.bodyMedium,
                        color = dailyCardTextColor.copy(alpha = 0.85f),
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "3 topics mastered this week",
                        style = MaterialTheme.typography.bodySmall,
                        color = dailyCardTextColor.copy(alpha = 0.7f),
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { 0.35f },
                        modifier = Modifier.size(56.dp),
                        color = dailyCardTextColor,
                        strokeWidth = 5.dp,
                        trackColor = dailyCardTextColor.copy(0.2f)
                    )
                    Text(
                        "35%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = dailyCardTextColor
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentButtonColor,
                        contentColor = accentButtonTextColor
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Outlined.PlayArrow, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Continue", fontWeight = FontWeight.SemiBold)
                }
                OutlinedButton(
                    onClick = { },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = dailyCardTextColor
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(listOf(dailyCardTextColor.copy(0.3f), dailyCardTextColor.copy(0.3f)))
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Outlined.Insights, null, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
fun StreakCard(
    modifier: Modifier = Modifier,
    cardColor: Color,
    textColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                Icons.Outlined.LocalFireDepartment,
                null,
                tint = textColor,
                modifier = Modifier.size(24.dp)
            )
            Text(
                "7 Days",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = textColor
            )
            Text(
                "Current Streak",
                fontSize = 12.sp,
                color = textColor.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun AchievementCard(
    modifier: Modifier = Modifier,
    cardColor: Color,
    textColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                Icons.Outlined.EmojiEvents,
                null,
                tint = textColor,
                modifier = Modifier.size(24.dp)
            )
            Text(
                "12 / 50",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = textColor
            )
            Text(
                "Topics Mastered",
                fontSize = 12.sp,
                color = textColor.copy(alpha = 0.8f)
            )
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
        Text(
            "Categories",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(Category.entries) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { onCategorySelected(category) },
                    label = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(category.icon, null, modifier = Modifier.size(14.dp))
                            Text(category.label, fontSize = 12.sp)
                        }
                    },
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
        Text(
            "Difficulty Level",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DifficultyLevel.entries.forEach { difficulty ->
                FilterChip(
                    modifier = Modifier.weight(1f),
                    selected = selectedDifficulty == difficulty,
                    onClick = { onDifficultySelected(difficulty) },
                    label = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        if (selectedDifficulty == difficulty) accentTextColor else difficulty.color,
                                        CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                difficulty.label,
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp
                            )
                        }
                    },
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                Brush.linearGradient(topic.category.gradient),
                                RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            topic.category.icon,
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            topic.title,
                            fontWeight = FontWeight.Bold,
                            color = primaryTextColor,
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            "${topic.questionsCount} Questions • ${topic.estimatedTime}",
                            style = MaterialTheme.typography.bodySmall,
                            color = secondaryTextColor
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(topic.difficulty.color.copy(0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(topic.difficulty.color, CircleShape)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                topic.description,
                color = secondaryTextColor,
                fontSize = 14.sp,
                maxLines = 2,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun EmptyState(textColor: Color) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            Icons.Outlined.SearchOff,
            null,
            tint = textColor.copy(alpha = 0.5f),
            modifier = Modifier.size(48.dp)
        )
        Text(
            "No topics found",
            color = textColor,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Text(
            "Try adjusting your filters",
            color = textColor.copy(alpha = 0.7f),
            fontSize = 13.sp
        )
    }
}