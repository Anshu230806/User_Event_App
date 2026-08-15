package com.example.eventsapp.feature.auth

import android.R.attr.onClick
import android.R.attr.password
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventsapp.feature.theme.BorderColor
import com.example.eventsapp.feature.theme.EventsAppTheme
import com.example.eventsapp.feature.theme.GrayBackground
import com.example.eventsapp.feature.theme.PurpleMain
import com.example.eventsapp.feature.theme.TextDark
import com.example.eventsapp.feature.theme.TextHint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

// Color Palette Based on Image
//@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun LoginRoute(
    onLoginSuccess:()-> Unit={},
    onForgetPassClick: () -> Unit={},
    onSignUpClick: () -> Unit={},
    loginViewModel: LoginViewModel= hiltViewModel()
){
    // val loginUiState by loginViewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(loginUiState.effect) {

        when (val effect = loginUiState.effect) {

            LoginUiEffect.navigateToHome -> {
                onLoginSuccess()

                loginViewModel.consumeEffect()
            }

            null -> Unit
        }
    }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }

    Login(
        loginUiState= loginUiState,
        onEmailChange= loginViewModel::onEmailChange,
        onPasswordChange= loginViewModel::onPasswordChange,
        onLoginClick= loginViewModel::login,
        onSignUpClick= onSignUpClick ,
        onForgetPassClick= onForgetPassClick
    )
}


@Composable
fun Login(
    modifier:Modifier = Modifier,
    loginUiState: LoginUiState,
    onEmailChange: (String)->Unit,
    onPasswordChange: (String)->Unit,
    onLoginClick:()->Unit,
    onSignUpClick: () -> Unit,
    onForgetPassClick: () -> Unit
) {
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
                    value = loginUiState.email,
                    onValueChange = {onEmailChange(it) },
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
                    value = loginUiState.password,
                    onValueChange = { onPasswordChange(it) },
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
                    modifier = Modifier.clickable(onClick = onForgetPassClick )
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            if (loginUiState.errorMessage != null) {
                Text(
                    text = loginUiState.errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Main Login Button
            Button(
                onClick = {onLoginClick()} ,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !loginUiState.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = PurpleMain)
            ) {
                if (loginUiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Login",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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
                    modifier = Modifier.clickable { onSignUpClick() }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview(){
    EventsAppTheme() {
        Login(
        loginUiState= LoginUiState(
            email="test@email.com",
            password="test123"
        ),
        onEmailChange= {},
        onPasswordChange= {},
        onLoginClick= {},
        onSignUpClick= {},
        onForgetPassClick= {}
        )
    }
}