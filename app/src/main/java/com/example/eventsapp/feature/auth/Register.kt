package com.example.eventsapp.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eventsapp.feature.theme.EventsAppTheme

// Color Palette (Consistent with Login Page)
val PurpleMain = Color(0xFF6C24E3)
val GrayBackground = Color(0xFFFAFAFA)
val TextDark = Color(0xFF1A1A1A)
val TextHint = Color(0xFF9E9E9E)
val BorderColor = Color(0xFFE0E0E0)

//@OptIn(ExperimentalMaterial3Api::class)


@Composable
fun Register(
    onLoginClick: () -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                // Added scroll state because signup has more input fields
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- TOP NAVIGATION BAR WITH BACK ICON ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* Handle Back navigation click action */ },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Arrow",
                        tint = TextDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Heading Title
            Text(
                text = "Create Account",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Start
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle text
            Text(
                text = "Sign up to get started with Event Manager",
                fontSize = 14.sp,
                color = TextHint,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Start
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Full Name Input Field Section
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Full Name",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = { Text("Enter your full name", color = TextHint) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "User Icon", tint = TextHint)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PurpleMain,
                        unfocusedBorderColor = BorderColor
                    )
                )
            }


            // Email Input Field Section
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Email",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Enter your email", color = TextHint) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon", tint = TextHint)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PurpleMain,
                        unfocusedBorderColor = BorderColor
                    )
                )
            }



            // Password Input Field Section
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Password",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Create a password", color = TextHint) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock Icon", tint = TextHint)
                    },
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.VisibilityOff, contentDescription = "Toggle Visibility", tint = TextHint)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PurpleMain,
                        unfocusedBorderColor = BorderColor
                    )
                )
            }

//            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Input Field Section
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Confirm Password",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = { Text("Repeat your password", color = TextHint) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock Icon", tint = TextHint)
                    },
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.VisibilityOff, contentDescription = "Toggle Visibility", tint = TextHint)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PurpleMain,
                        unfocusedBorderColor = BorderColor
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Main Signup Button
            Button(
                onClick = { /* Handle Signup action */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleMain)
            ) {
                Text(
                    text = "Sign Up",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Divider Line: "or continue with"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = BorderColor)
                Text(
                    text = "or continue with",
                    color = TextHint,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = BorderColor)

            }
            Spacer(modifier = Modifier.height(24.dp))

// Social Buttons Row (Google & Facebook)
Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.spacedBy(16.dp)) {
// Google Sign In
Row(modifier = Modifier.weight(1f).height(48.dp).border(1.dp,
    BorderColor,
    RoundedCornerShape(12.dp)).clickable { /* Google Auth */ },
    horizontalArrangement = Arrangement.Center,verticalAlignment = Alignment.CenterVertically)
{
    Text(text = "G ", color = Color.Red, fontWeight = FontWeight.Bold)
    Text(text = "Google", color = TextDark, fontSize = 14.sp, fontWeight = FontWeight.Medium)
}

}

// Extra spacing at the bottom to prevent layout overlap while scrollingSpacer(modifier = Modifier.height(100.dp))}
// Bottom Login Redirection Text pinned to the bottom of the
  Box(modifier = Modifier.fillMaxSize().padding(bottom = 24.dp),contentAlignment = Alignment.BottomCenter) {
      Row {
          Text(text = "Already have an account? ", color = TextDark, fontSize = 14.sp)

Text(
    text = "Login",
    color = PurpleMain,
    fontSize = 14.sp,
    fontWeight = FontWeight.Bold,
    modifier = Modifier.
               clickable { onLoginClick() }
)
}
  }
    }
}
}


@Preview( showBackground = true)
@Composable
fun signUpPreview(){
    EventsAppTheme{
        Register()
    }
}