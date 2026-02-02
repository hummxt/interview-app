package com.example.hummet.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hummet.R
import androidx.compose.runtime.*
import com.example.hummet.data.repository.UserRepository
import com.example.hummet.data.repository.UserData
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSection(
    primaryTextColor: Color,
    secondaryTextColor: Color
) {
    val repository = remember { UserRepository() }
    val auth = remember { FirebaseAuth.getInstance() }
    var userData by remember { mutableStateOf<UserData?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        userData = repository.getUserProfile()
        isLoading = false
    }

    val displayName = userData?.name ?: auth.currentUser?.displayName ?: "Hummet User"
    val experienceLevel = userData?.experienceLevel ?: "Beginner"

    CenterAlignedTopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = null,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(
                        "Hello, $displayName",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = primaryTextColor
                    )
                    Text(
                        experienceLevel,
                        style = MaterialTheme.typography.labelSmall,
                        color = secondaryTextColor
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(
                    Icons.Outlined.Notifications,
                    null,
                    modifier = Modifier.size(26.dp),
                    tint = primaryTextColor
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        )
    )
}