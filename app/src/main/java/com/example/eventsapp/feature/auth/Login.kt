//package com.example.eventsapp.ui.auth
//
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.CenterAlignedTopAppBar
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import com.example.eventsapp.ui.theme.EventsAppTheme
//
//@Composable
//fun Login(
//    modifier: Modifier = Modifier
//){
////    Box(
////        modifier = modifier
////    ){
////
////        Column(
////             modifier = modifier.fillMaxSize()
////        ){
////
////
////        }
//
//}




import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import com.example.eventsapp.feature.theme.BorderColor
import com.example.eventsapp.feature.theme.EventsAppTheme
import com.example.eventsapp.feature.theme.GrayBackground
import com.example.eventsapp.feature.theme.PurpleMain
import com.example.eventsapp.feature.theme.TextDark
import com.example.eventsapp.feature.theme.TextHint

// Color Palette Based on Image


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp), // Adjust padding based on status bar height
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



            // Top Illustration Placeholder
            // Replace R.drawable.login_illustration with your actual vector/image resource
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(130.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                // If you have the image resource:
//                // Image(painter = painterResource(id = R.drawable.login_illustration), contentDescription = "Login Illustration")
//
//                // Temporary visual placeholder for illustration
//                Text(text = "[ Illustration Placeholder ]", color = PurpleMain)
//            }

            Spacer(modifier = Modifier.height(24.dp))

            // Heading Title
            Text(
                text = "Welcome Back!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle text
            Text(
                text = "Login to continue to Event Manager",
                fontSize = 14.sp,
                color = TextHint
            )

            Spacer(modifier = Modifier.height(32.dp))

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

            Spacer(modifier = Modifier.height(16.dp))

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
                    placeholder = { Text("Enter your password", color = TextHint) },
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

            Spacer(modifier = Modifier.height(12.dp))

            // Forgot Password Link
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Forgot Password?",
                    color = PurpleMain,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { /* Handle Forgot Password click */ }
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Main Login Button
            Button(
                onClick = { /* Handle Login action */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleMain)
            ) {
                Text(
                    text = "Login",
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Google Sign In Button
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                        .clickable { /* Google Auth */ },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "G ", color = Color.Red, fontWeight = FontWeight.Bold) // Substitute for Google logo vector
                    Text(text = "Google", color = TextDark, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }

                // Facebook Sign In Button
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                        .clickable { /* Facebook Auth */ },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "f ", color = Color(0xFF1877F2), fontWeight = FontWeight.Bold) // Substitute for FB logo vector
                    Text(text = "Facebook", color = TextDark, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        // Bottom Sign Up Redirection Text pinned to the bottom of the Screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row {
                Text(text = "Don't have an account? ", color = TextDark, fontSize = 14.sp)
                Text(
                    text = "Sign Up",
                    color = PurpleMain,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { /* Navigate to Sign Up Page */ }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview(){
    EventsAppTheme() {
        LoginPage()
    }
}