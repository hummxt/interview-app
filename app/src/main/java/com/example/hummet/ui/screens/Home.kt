package com.example.hummet.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hummet.R

data class InterviewTopic(
    val title: String,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Homepage(
    onNavigateToCoding: () -> Unit,
    onNavigateToQuestions: () -> Unit
) {
    var showNotifs by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("Logic") }

    val row1 = listOf("Languages", "Backend", "Frontend")
    val row2 = listOf("Databases", "DevOps", "Mobile")

    val cardData = when (selectedTab) {
        "Languages" -> Pair(
            InterviewTopic(
                "Core Languages",
                "Java, Kotlin, JavaScript, Python – syntax, memory model, and best practices."
            ),
            InterviewTopic(
                "Advanced Concepts",
                "OOP, Functional Programming, closures, generics, and immutability."
            )
        )
        "Backend" -> Pair(
            InterviewTopic(
                "Backend Frameworks",
                "Spring Boot, Ktor, Node.js, Go Fiber – REST APIs and MVC architecture."
            ),
            InterviewTopic(
                "APIs & Auth",
                "REST, GraphQL, JWT, OAuth 2.0, request lifecycle and middleware."
            )
        )
        "Frontend" -> Pair(
            InterviewTopic(
                "Frontend Frameworks",
                "React, Next.js, Vue – components, hooks, state management."
            ),
            InterviewTopic(
                "UI & Performance",
                "Responsive design, accessibility, rendering optimization."
            )
        )
        "Databases" -> Pair(
            InterviewTopic(
                "SQL & NoSQL",
                "PostgreSQL, MySQL, MongoDB – schema design and queries."
            ),
            InterviewTopic(
                "Performance",
                "Indexes, transactions, normalization, and query optimization."
            )
        )
        "DevOps" -> Pair(
            InterviewTopic(
                "Containers & CI/CD",
                "Docker, GitHub Actions, pipelines, automated deployments."
            ),
            InterviewTopic(
                "Cloud Basics",
                "AWS, GCP, environment variables, scaling and monitoring."
            )
        )
        "Mobile" -> Pair(
            InterviewTopic(
                "Android Development",
                "Kotlin, Jetpack Compose, lifecycle, MVVM architecture."
            ),
            InterviewTopic(
                "Cross-Platform",
                "React Native, Flutter – shared logic and performance tradeoffs."
            )
        )
        else -> Pair(
            InterviewTopic(
                "System Design",
                "Scalability, load balancing, caching, and microservices."
            ),
            InterviewTopic(
                "Clean Code",
                "SOLID principles, refactoring, and code reviews."
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = null,
                            modifier = Modifier.size(45.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text("Hello, Hummet ", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("Beginner", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        }
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { showNotifs = true }) {
                            Icon(Icons.Outlined.Notifications, null, modifier = Modifier.size(28.dp))
                        }
                        DropdownMenu(
                            expanded = showNotifs,
                            onDismissRequest = { showNotifs = false },
                            modifier = Modifier.width(200.dp)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Ready for a mock interview?") },
                                onClick = { showNotifs = false },
                                leadingIcon = { Icon(Icons.Outlined.Lightbulb, null) }
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(horizontal = 16.dp).fillMaxSize()) {

            SearchBar(
                query = "", onQueryChange = {}, onSearch = {}, active = false, onActiveChange = {},
                placeholder = { Text("Search interview topics...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {}

            Spacer(modifier = Modifier.height(18.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFC3E7FF)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Icon(Icons.Outlined.Terminal, null, modifier = Modifier.size(32.dp))
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = { 0.0f }, modifier = Modifier.size(45.dp),
                                color = Color.Black, strokeWidth = 4.dp, trackColor = Color.White.copy(0.4f)
                            )
                            Text("0/3", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Text("Daily Challenge", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
                    Text("Today: System Design Basics", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = onNavigateToCoding, colors = ButtonDefaults.buttonColors(Color.Black), shape = RoundedCornerShape(12.dp)) {
                        Text("Start Interview", color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row1.forEach { tab ->
                        FilterChip(
                            modifier = Modifier.weight(1f).height(42.dp),
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            label = { Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { Text(tab, fontSize = 11.sp) } },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row2.forEach { tab ->
                        FilterChip(
                            modifier = Modifier.weight(1f).height(42.dp),
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            label = { Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { Text(tab, fontSize = 11.sp) } },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SmallTaskCard(
                    topic = cardData.first, color = Color(0xFFE8B9FF),
                    icon = Icons.Rounded.Psychology, modifier = Modifier.weight(1f),
                    onClick = onNavigateToQuestions
                )
                SmallTaskCard(
                    topic = cardData.second, color = Color(0xFFFFB366),
                    icon = Icons.Outlined.Code, modifier = Modifier.weight(1f),
                    onClick = onNavigateToCoding
                )
            }
        }
    }
}

@Composable
fun SmallTaskCard(topic: InterviewTopic, color: Color, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick, modifier = modifier.height(210.dp),
        colors = CardDefaults.cardColors(containerColor = color), shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Icon(icon, null, modifier = Modifier.size(28.dp))
            Column {
                Text(topic.title, fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleMedium, lineHeight = 20.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(topic.description, style = MaterialTheme.typography.bodySmall, color = Color.Black.copy(0.7f), lineHeight = 16.sp)
            }
        }
    }
}