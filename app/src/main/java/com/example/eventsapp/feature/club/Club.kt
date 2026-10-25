package com.example.eventsapp.feature.club

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eventsapp.feature.theme.EventsAppTheme

private val PrimaryPurple = Color(0xFF501783)
private val LightPurple = Color(0xFFF2EAF9)
private val ScreenBackground = Color(0xFFFAF8FC)

data class Club(
    val name: String,
    val category: String,
    val description: String,
    val members: Int,
    val emoji: String
)

private val sampleClubs = listOf(
    Club(
        "CodeCrafters",
        "Technology",
        "Build projects, solve problems, and explore new technologies.",
        120,
        "💻"
    ),
    Club(
        "Pixel Studio",
        "Creative",
        "Photography, graphic design, and visual storytelling.",
        85,
        "🎨"
    ),
    Club(
        "Rhythm Society",
        "Music",
        "Discover music, perform together, and enjoy live sessions.",
        64,
        "🎵"
    ),
    Club(
        "Innovators Hub",
        "Technology",
        "Turn creative ideas into practical solutions.",
        96,
        "🚀"
    ),
    Club(
        "Literary Circle",
        "Literature",
        "Poetry, debates, writing, and meaningful conversations.",
        52,
        "📚"
    )
)

@Composable
fun ClubScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var joinedClubs by remember { mutableStateOf(setOf<String>()) }

    val categories = listOf(
        "All", "Technology", "Creative", "Music", "Literature"
    )

    val filteredClubs = sampleClubs.filter { club ->
        val matchesSearch =
            club.name.contains(searchQuery, ignoreCase = true) ||
                    club.description.contains(searchQuery, ignoreCase = true)

        val matchesCategory =
            selectedCategory == "All" ||
                    club.category == selectedCategory

        matchesSearch && matchesCategory
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .padding(horizontal = 18.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Explore Clubs",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryPurple
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Find your community. Follow your interests.",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(18.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search clubs...") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryPurple,
                cursorColor = PrimaryPurple,
                unfocusedBorderColor = Color(0xFFE0D8E8)
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.take(3).forEach { category ->
                CategoryChip(
                    text = category,
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.drop(3).forEach { category ->
                CategoryChip(
                    text = category,
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category }
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "${filteredClubs.size} clubs to explore",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(
                items = filteredClubs,
                key = { it.name }
            ) { club ->
                ClubCard(
                    club = club,
                    isJoined = club.name in joinedClubs,
                    onJoinClick = {
                        joinedClubs = if (club.name in joinedClubs) {
                            joinedClubs - club.name
                        } else {
                            joinedClubs + club.name
                        }
                    }
                )
            }

            if (filteredClubs.isEmpty()) {
                item {
                    Text(
                        text = "No clubs found. Try another search.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 30.dp),
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = if (selected) PrimaryPurple else Color.White,
        border = if (selected) null
        else ButtonDefaults.outlinedButtonBorder
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 13.dp,
                vertical = 9.dp
            ),
            color = if (selected) Color.White else PrimaryPurple,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ClubCard(
    club: Club,
    isJoined: Boolean,
    onJoinClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(LightPurple),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = club.emoji,
                        fontSize = 27.sp
                    )
                }

                Spacer(modifier = Modifier.width(13.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = club.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF29212F)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = club.category,
                        fontSize = 12.sp,
                        color = PrimaryPurple
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = club.description,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = Color(0xFF68616D)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "👥 ${club.members} members",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = onJoinClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isJoined)
                            LightPurple else PrimaryPurple,
                        contentColor = if (isJoined)
                            PrimaryPurple else Color.White
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 17.dp,
                        vertical = 7.dp
                    )
                ) {
                    Text(
                        text = if (isJoined) "Joined" else "Join",
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ClubScreenPreview() {
    EventsAppTheme {
        ClubScreen()
    }
}
