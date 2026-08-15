package com.example.eventsapp.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.eventsapp.feature.auth.Register
import com.example.eventsapp.feature.auth.RegisterUiState
import com.example.eventsapp.feature.auth.TextDark
import com.example.eventsapp.feature.theme.EventsAppTheme

@Composable
fun Home(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
        Text(text = "Home Screen - Welcome!")
    }
}



@Preview( showBackground = true)
@Composable
fun HomePreview(){
    EventsAppTheme{
        Home()
    }
}