package com.example.hummet.ui.screens.auth

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hummet.data.repository.UserRepository
import com.example.hummet.ui.theme.isAppInDarkTheme
import kotlinx.coroutines.launch
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit
) {
    val repository = remember { UserRepository() }
    val scope = rememberCoroutineScope()
    
    var currentStep by remember { mutableIntStateOf(0) }
    var selectedLevel by remember { mutableStateOf("Beginner") }
    var selectedPath by remember { mutableStateOf("Mobile Development") }
    var goal by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val isDark = isAppInDarkTheme()
    val primaryTextColor = if (isDark) Color.White else Color.Black
    val containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val borderColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(3) { step ->
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(4.dp)
                            .clip(CircleShape)
                            .background(if (step <= currentStep) primaryTextColor else borderColor)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            AnimatedContent(
                targetState = currentStep,
                transitionSpec = {
                    slideInHorizontally { it } + fadeIn() togetherWith
                    slideOutHorizontally { -it } + fadeOut()
                },
                label = "StepTransition"
            ) { step ->
                when (step) {
                    0 -> ExperienceLevelStep(
                        selectedLevel = selectedLevel,
                        onLevelSelected = { selectedLevel = it },
                        primaryTextColor = primaryTextColor,
                        borderColor = borderColor,
                        containerColor = containerColor
                    )
                    1 -> LearningPathStep(
                        selectedPath = selectedPath,
                        onPathSelected = { selectedPath = it },
                        primaryTextColor = primaryTextColor,
                        borderColor = borderColor,
                        containerColor = containerColor
                    )
                    2 -> GoalStep(
                        goal = goal,
                        onGoalChange = { goal = it },
                        primaryTextColor = primaryTextColor,
                        borderColor = borderColor
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (currentStep < 2) {
                        currentStep++
                    } else {
                        isLoading = true
                        scope.launch {
                            val userName = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.displayName ?: "Hummet User"
                            repository.updateOnboardingData(userName, selectedLevel, selectedPath, goal)
                            isLoading = false
                            onComplete()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryTextColor,
                    contentColor = if (isDark) Color.Black else Color.White
                ),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = if (isDark) Color.Black else Color.White)
                } else {
                    Text(
                        if (currentStep < 2) "Continue" else "Finish Profile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ExperienceLevelStep(
    selectedLevel: String,
    onLevelSelected: (String) -> Unit,
    primaryTextColor: Color,
    borderColor: Color,
    containerColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "What's your experience level?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = primaryTextColor,
            textAlign = TextAlign.Center
        )
        Text(
            "This helps us tailor the interview questions for you.",
            style = MaterialTheme.typography.bodyMedium,
            color = primaryTextColor.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        val levels = listOf(
            OnboardingOption("Beginner", "Just starting out", Icons.Outlined.School),
            OnboardingOption("Intermediate", "Some experience", Icons.Outlined.LaptopMac),
            OnboardingOption("Advanced", "Experienced professional", Icons.Outlined.RocketLaunch)
        )

        levels.forEach { level ->
            SelectableCard(
                title = level.title,
                subtitle = level.subtitle,
                icon = level.icon,
                isSelected = selectedLevel == level.title,
                onClick = { onLevelSelected(level.title) },
                primaryTextColor = primaryTextColor,
                borderColor = borderColor,
                containerColor = containerColor
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun LearningPathStep(
    selectedPath: String,
    onPathSelected: (String) -> Unit,
    primaryTextColor: Color,
    borderColor: Color,
    containerColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "What do you want to learn?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = primaryTextColor,
            textAlign = TextAlign.Center
        )
        Text(
            "Pick your primary focus area.",
            style = MaterialTheme.typography.bodyMedium,
            color = primaryTextColor.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        val paths = listOf(
            OnboardingOption("Mobile Development", "Android, iOS, Flutter", Icons.Outlined.PhoneAndroid),
            OnboardingOption("Frontend Engineering", "React, Vue, Web", Icons.Outlined.Palette),
            OnboardingOption("Backend Engineering", "Kotlin, Python, Java", Icons.Outlined.Storage),
            OnboardingOption("UI/UX Design", "Figma, User Research", Icons.Outlined.Draw)
        )

        paths.forEach { path ->
            SelectableCard(
                title = path.title,
                subtitle = path.subtitle,
                icon = path.icon,
                isSelected = selectedPath == path.title,
                onClick = { onPathSelected(path.title) },
                primaryTextColor = primaryTextColor,
                borderColor = borderColor,
                containerColor = containerColor
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun GoalStep(
    goal: String,
    onGoalChange: (String) -> Unit,
    primaryTextColor: Color,
    borderColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "What is your career goal?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = primaryTextColor,
            textAlign = TextAlign.Center
        )
        Text(
            "Describe what you're aiming for in a few words.",
            style = MaterialTheme.typography.bodyMedium,
            color = primaryTextColor.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = goal,
            onValueChange = onGoalChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            placeholder = { Text("e.g., Get a Senior Android job at Hummet", color = primaryTextColor.copy(alpha = 0.4f)) },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primaryTextColor,
                unfocusedBorderColor = borderColor
            )
        )
    }
}

@Composable
fun SelectableCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    primaryTextColor: Color,
    borderColor: Color,
    containerColor: Color
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) primaryTextColor else borderColor,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        color = containerColor
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(if (isSelected) primaryTextColor else borderColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    null,
                    tint = if (isSelected) (if (isAppInDarkTheme()) Color.Black else Color.White) else primaryTextColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, color = primaryTextColor)
                Text(subtitle, style = MaterialTheme.typography.labelSmall, color = primaryTextColor.copy(alpha = 0.6f))
            }
            if (isSelected) {
                Icon(Icons.Default.ChevronRight, null, tint = primaryTextColor)
            }
        }
    }
}

data class OnboardingOption(
    val title: String,
    val subtitle: String,
    val icon: ImageVector
)
