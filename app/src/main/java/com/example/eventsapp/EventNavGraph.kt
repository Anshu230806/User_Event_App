package com.example.eventsapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eventsapp.feature.onboarding.onBoarding


@Composable
fun EventNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = EventDestinations.ONBOARDING_ROUTE,
    navActions: EventNavigationActions = remember(navController) {
        EventNavigationActions(navController)
    }
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(EventDestinations.ONBOARDING_ROUTE) {
            onBoarding(
                onGetStartedClick = {
                    // For now, it stays on onboarding or we could add a destination later
                    // navActions.navigateTo...
                },
                onLoginClick = {
                    // navActions.navigateTo...
                }
            )
        }
    }
}
