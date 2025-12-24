package com.example.hummet.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


data class TopicGroup(
    val id: Int,
    val topicTitle: String,
    val category: String,
    val difficulty: String,
    val questions: List<String>,
    val accentColor: Color
)

val topicData = listOf(
    TopicGroup(1, "React State Management", "Frontend", "Medium",
        listOf("What is the difference between useState and useReducer?", "When should you use Context API over Redux?", "How do you lift state up in React?", "What are the limitations of local state?"),
        Color(0xFFE8B9FF)),
    TopicGroup(2, "Kotlin Coroutines", "Languages", "Hard",
        listOf("What is a CoroutineScope?", "Explain the difference between Launch and Async", "How does 'suspend' work under the hood?", "What is a Dispatcher?"),
        Color(0xFFFFB366)),
    TopicGroup(3, "SQL Indexing", "Databases", "Medium",
        listOf("What is a B-Tree index?", "Difference between Clustered and Non-Clustered indexes?", "When does an index slow down performance?", "How to use EXPLAIN to optimize queries?"),
        Color(0xFFA2D2FF)),
    TopicGroup(4, "Node.js Event Loop", "Backend", "Hard",
        listOf("Explain the phases of the Event Loop", "What is setImmediate vs process.nextTick?", "How does Node handle non-blocking I/O?", "What is the role of Libuv?"),
        Color(0xFFB9FBC0)),
    TopicGroup(5, "Swift UI Basics", "Mobile", "Easy",
        listOf("What is a View in SwiftUI?", "How do @State and @Binding differ?", "Explain the Body property", "How to create a List in SwiftUI?"),
        Color(0xFFFFCFD2))
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionsScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedDifficulty by remember { mutableStateOf("All") }
    var selectedCategory by remember { mutableStateOf("All") }

    val filteredTopics = topicData.filter { topic ->
        val matchesSearch = topic.topicTitle.contains(searchQuery, ignoreCase = true)
        val matchesDiff = selectedDifficulty == "All" || topic.difficulty == selectedDifficulty
        val matchesCat = selectedCategory == "All" || topic.category == selectedCategory
        matchesSearch && matchesDiff && matchesCat
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = {
                Text("Topic Library", fontWeight = FontWeight.Black, fontSize = 22.sp)
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent
            )
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("Search topics...", fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.3f),
                focusedBorderColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Difficulty",
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.labelLarge,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All", "Easy", "Medium", "Hard").forEach { diff ->
                FilterChip(
                    modifier = Modifier.weight(1f).height(40.dp),
                    selected = selectedDifficulty == diff,
                    onClick = { selectedDifficulty = diff },
                    label = { Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { Text(diff, fontSize = 11.sp, fontWeight = FontWeight.Bold) } },
                    shape = RoundedCornerShape(12.dp),
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color.Black, selectedLabelColor = Color.White)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Categories",
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.labelLarge,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        val categories = listOf("All", "Languages", "Backend", "Frontend", "Databases", "Mobile")
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                categories.take(3).forEach { cat ->
                    FilterChip(
                        modifier = Modifier.weight(1f).height(40.dp),
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { Text(cat, fontSize = 11.sp, fontWeight = FontWeight.Bold) } },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color.Black, selectedLabelColor = Color.White)
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                categories.takeLast(3).forEach { cat ->
                    FilterChip(
                        modifier = Modifier.weight(1f).height(40.dp),
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { Text(cat, fontSize = 11.sp, fontWeight = FontWeight.Bold) } },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color.Black, selectedLabelColor = Color.White)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Topics",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            itemsIndexed(filteredTopics) { index, topic ->
                TopicListItem(topic = topic, onClick = { navController.navigate("quiz/${topic.id}") })
                if (index < filteredTopics.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 72.dp, end = 16.dp),
                        thickness = 0.5.dp,
                        color = Color.LightGray.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
}


@Composable
fun TopicListItem(topic: TopicGroup, onClick: () -> Unit) {
    ListItem(
        modifier = Modifier.clickable { onClick() },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        ),
        headlineContent = {
            Text(
                topic.topicTitle,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        supportingContent = {
            Text(
                "${topic.questions.size} Questions • ${topic.difficulty}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        },
        leadingContent = {
            Surface(
                color = topic.accentColor.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.Quiz,
                        contentDescription = null,
                        tint = topic.accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.LightGray
            )
        }
    )
}