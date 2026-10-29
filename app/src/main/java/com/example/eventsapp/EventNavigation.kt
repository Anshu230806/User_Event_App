package com.example.eventsapp

import android.net.http.SslCertificate.restoreState
import android.net.http.SslCertificate.saveState
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable


//private object EventScreens {
//    const val ONBOARDING_SCREEN = "start_screen"
//}
//object EventDestinations {
//    const val ONBOARDING_ROUTE = ONBOARDING_SCREEN
//}


@Serializable
public object ONBOARDING_ROUTE
@Serializable
public object LOGIN_ROUTE
@Serializable
public object SIGNUP_ROUTE
@Serializable
public object FORGOTPASS_ROUTE

@Serializable
public object HOME_ROUTE


@Serializable
public object EVENT_FILTER_ROUTE

@Serializable data class EVENT_DETAIL_ROUTE(val eventId: String)
@Serializable data class REGISTRATION_ROUTE(val eventId: String)
@Serializable object PROFILE_ROUTE
@Serializable object CLUBS_ROUTE
@Serializable data class CLUB_DETAIL_ROUTE(val clubId: String)
@Serializable object OPPORTUNITIES_ROUTE
class EventNavigationActions(private val navController: NavHostController) {

    fun navigateToOnboarding() {
        navController.navigate(ONBOARDING_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToLogin(){
        navController.navigate(LOGIN_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }


    fun navigateToSignUp(){
        navController.navigate(SIGNUP_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

    }

    fun navigateToForgetPassword(){
        navController.navigate(FORGOTPASS_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToHome(){
        navController.navigate(HOME_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToEventFilter() { navController.navigate(EVENT_FILTER_ROUTE) }

    fun navigateToEventDetail(id: String) = navController.navigate(EVENT_DETAIL_ROUTE(id))
    fun navigateToRegistration(id: String) = navController.navigate(REGISTRATION_ROUTE(id))
    fun navigateToProfile() = navController.navigate(PROFILE_ROUTE)
    fun navigateToClubs() = navController.navigate(CLUBS_ROUTE)
    fun navigateToClubDetail(id: String) = navController.navigate(CLUB_DETAIL_ROUTE(id))
    fun navigateToOpportunities() = navController.navigate(OPPORTUNITIES_ROUTE)
}
