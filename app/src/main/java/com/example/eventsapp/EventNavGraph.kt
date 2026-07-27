package com.example.eventsapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eventsapp.feature.onboarding.OnBoarding
import com.example.eventsapp.feature.auth.Login
import com.example.eventsapp.feature.auth.Register



@Composable
fun EventNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination:Any = ONBOARDING_ROUTE,
    navActions: EventNavigationActions = remember(navController) {
        EventNavigationActions(navController)
    }
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<ONBOARDING_ROUTE> {
            OnBoarding(
                onGetStartedClick = {
                    // For now, it stays on onboarding or we could add a destination later
                    // navActions.navigateTo...
                    navActions.navigateToSignUp()
                },
                onLoginClick = {
                    // navActions.navigateTo...
                    navActions.navigateToLogin()
                }
            )
        }

        composable<LOGIN_ROUTE>{
            Login(
                onSignUpClick = {
                    navActions.navigateToSignUp()
                }
            )
        }

        composable<SIGNUP_ROUTE>{
            Register(
                onLoginClick = {
                    navActions.navigateToLogin()
                }
            )
        }
    }
}
