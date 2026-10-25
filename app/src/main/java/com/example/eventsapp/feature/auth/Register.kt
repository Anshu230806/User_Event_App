package com.example.eventsapp.feature.auth

import android.R.attr.password
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eventsapp.feature.theme.EventsAppTheme

val PurpleMain = Color(0xFF6C24E3)
val GrayBackground = Color(0xFFFAFAFA)
val TextDark = Color(0xFF1A1A1A)
val TextHint = Color(0xFF9E9E9E)
val BorderColor = Color(0xFFE0E0E0)



@Composable
fun RegisterRoute(
    onLoginClick: ()->Unit,
    onSignUpSuccess: ()->Unit,
    onForgetPassClick: ()->Unit,
    registerViewModel: RegisterViewModel= hiltViewModel()
){
    val registerUiState by registerViewModel.uiState.collectAsStateWithLifecycle()


    LaunchedEffect(registerUiState.effect) {

        when (val effect = registerUiState.effect) {

            RegisterUiEffect.navigateToHome -> {
                onSignUpSuccess()

                registerViewModel.consumeEffect()
            }

            null -> Unit
        }
    }

    Register(
        registerUiState= registerUiState,
        onLoginClick = onLoginClick,
        onSignUpClick = registerViewModel::signUp,
        onForgetPassClick = onForgetPassClick,
        onEmailChange = registerViewModel::onEmailChange,
        onPasswordChange = registerViewModel::onPasswordChange,
        onConfirmPassChange = registerViewModel::onConfirmPassChange
    )
}

@Composable
fun Register(
    mofifier: Modifier= Modifier,
    registerUiState: RegisterUiState,
    onLoginClick: () -> Unit ,
    onSignUpClick: () -> Unit,
    onForgetPassClick: () -> Unit,
    onEmailChange:(String)->Unit,
    onPasswordChange:(String)->Unit,
    onConfirmPassChange:(String)->Unit,

) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Create Account",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Start
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sign up to get started with Event Manager",
                fontSize = 14.sp,
                color = TextHint,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Start
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Full Name",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value ="",
                    onValueChange = { },
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


            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Email",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = registerUiState.email,
                    onValueChange = { onEmailChange(it)},
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

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Password",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = registerUiState.password,
                    onValueChange = { onPasswordChange(it) },
                    placeholder = { Text("Create a password", color = TextHint) },
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

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Confirm Password",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = registerUiState.confirmPass,
                    onValueChange = {onConfirmPassChange(it)},
                    placeholder = { Text("Repeat your password", color = TextHint) },
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

            Spacer(modifier = Modifier.height(32.dp))

            if (registerUiState.errorMessage != null) {
                Text(
                    text = registerUiState.errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Button(
                onClick = { onSignUpClick()},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !registerUiState.isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleMain)
            ) {
                if (registerUiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Sign Up",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


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
        Register(
            registerUiState= RegisterUiState(),
            onLoginClick = {},
            onSignUpClick ={},
            onForgetPassClick = {},
            onEmailChange = {},
            onPasswordChange ={},
            onConfirmPassChange = {}
        )
    }
}