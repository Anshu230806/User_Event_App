package com.example.eventsapp.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ProfilePurple = Color(0xFF501783)
private val ProfileLightPurple = Color(0xFFF2EAF9)
private val ProfileBackground = Color(0xFFFAF8FC)

@Composable
fun ProfileScreen(
    onEditProfile: () -> Unit = {},
    onMyEvents: () -> Unit = {},
    onSavedEvents: () -> Unit = {},
    onSettings: () -> Unit = {},
    onHelp: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ProfileBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 22.dp)
    ) {
        Text(
            text = "My Profile",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = ProfilePurple
        )

        Spacer(Modifier.height(22.dp))

        // Profile header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(94.dp)
                        .clip(CircleShape)
                        .background(ProfileLightPurple),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "A",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = ProfilePurple
                    )
                }

                Spacer(Modifier.height(14.dp))

                Text(
                    text = "Ruchi Sharma",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF29212F)
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = "ruchi@example.com",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(Modifier.height(14.dp))

                Surface(
                    color = ProfileLightPurple,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "Student",
                        modifier = Modifier.padding(
                            horizontal = 18.dp,
                            vertical = 7.dp
                        ),
                        color = ProfilePurple,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(Modifier.height(18.dp))

                OutlinedButton(
                    onClick = onEditProfile,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = ProfilePurple
                    )
                ) {
                    Text("Edit Profile")
                }
            }
        }

        Spacer(Modifier.height(22.dp))

        Text(
            text = "Your Activity",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF29212F)
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActivityCard(
                title = "Events Joined",
                value = "08",
                modifier = Modifier.weight(1f)
            )

            ActivityCard(
                title = "Saved Events",
                value = "12",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Account",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF29212F)
        )

        Spacer(Modifier.height(10.dp))

        ProfileMenuItem(
            emoji = "📅",
            title = "My Events",
            subtitle = "View your event registrations",
            onClick = onMyEvents
        )

        ProfileMenuItem(
            emoji = "🔖",
            title = "Saved Events",
            subtitle = "Events you bookmarked",
            onClick = onSavedEvents
        )

        ProfileMenuItem(
            emoji = "⚙️",
            title = "Settings",
            subtitle = "Manage your preferences",
            onClick = onSettings
        )

        ProfileMenuItem(
            emoji = "❓",
            title = "Help & Support",
            subtitle = "Get help with EventHub",
            onClick = onHelp
        )

        Spacer(Modifier.height(18.dp))

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFFB3261E)
            )
        ) {
            Text(
                text = "Log Out",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = "EventHub • Version 1.0",
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun ActivityCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = ProfileLightPurple
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = ProfilePurple
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = title,
                fontSize = 12.sp,
                color = Color(0xFF62566B)
            )
        }
    }
}

@Composable
private fun ProfileMenuItem(
    emoji: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ProfileLightPurple),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 21.sp)
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF29212F)
                )

                Spacer(Modifier.height(3.dp))

                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = "›",
                fontSize = 26.sp,
                color = ProfilePurple
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen()
    }
}
