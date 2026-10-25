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


@Composable
fun LoginRoute(
    onLoginSuccess:()-> Unit={},
    onForgetPassClick: () -> Unit={},
    onSignUpClick: () -> Unit={},
    loginViewModel: LoginViewModel= hiltViewModel()
){
    // val loginUiState by loginViewModel.uiState.collectAsState()
    val loginUiState by loginViewModel.uiState.collectAsStateWithLifecycle()

    // LaunchedEffect called whenever the value of the  loginUiState.effect  changes  then based on its value some task is performed
    LaunchedEffect(loginUiState.effect) {

        when (val effect = loginUiState.effect) {

            LoginUiEffect.navigateToHome -> {
                onLoginSuccess()

                loginViewModel.consumeEffect()
            }

            null -> Unit
        }
    }

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

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Welcome Back!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Login to continue to Event Manager",
                fontSize = 14.sp,
                color = TextHint
            )

            Spacer(modifier = Modifier.height(32.dp))

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



            Spacer(modifier = Modifier.height(24.dp))


        }

        
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