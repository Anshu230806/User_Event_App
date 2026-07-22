package com.example.eventsapp

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.eventsapp.EventScreens.ONBOARDING_SCREEN


private object EventScreens {
    const val ONBOARDING_SCREEN = "start_screen"
    
}


object EventDestinations {
    const val ONBOARDING_ROUTE = ONBOARDING_SCREEN

}


class EventNavigationActions(private val navController: NavHostController) {

    fun navigateToOnboarding() {
        navController.navigate(EventDestinations.ONBOARDING_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToLogin(){

    }


    fun navigateToSignUP(){

    }
}
