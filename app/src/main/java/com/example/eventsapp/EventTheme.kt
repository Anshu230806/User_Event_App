package com.example.eventsapp


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@Composable
fun EventTheme(content: @Composable () -> Unit){

    MaterialTheme(
        colorScheme = lightColorScheme(
            // donnot hardcode the colors , bcz This ensures your components immediately adapt if the user switches themes
            primary = Color(0xFF541A8A),
            secondary = Color(0xFF8B4DF5),
            tertiary = Color(0xFF4E187F)
        )
    ) {
        content()
    }
}