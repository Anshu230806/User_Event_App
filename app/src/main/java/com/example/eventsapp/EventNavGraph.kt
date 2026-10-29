package com.example.eventsapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eventsapp.feature.onboarding.OnBoarding
import com.example.eventsapp.feature.auth.LoginRoute
import com.example.eventsapp.feature.auth.Register
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import com.example.eventsapp.feature.auth.RegisterRoute
import com.example.eventsapp.feature.home.EventRoute
import androidx.compose.material3.Text
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.toRoute
import com.example.eventsapp.feature.clubs.ClubsRoute
import com.example.eventsapp.feature.eventFilterScreen.EventFilterRoute
import com.example.eventsapp.feature.eventdetail.EventDetailRoute
import com.example.eventsapp.feature.opportunities.OpportunitiesRoute
import com.example.eventsapp.feature.profile.ProfileScreen
import com.example.eventsapp.feature.registration.RegistrationRoute

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
                    navActions.navigateToSignUp()
                },
                onLoginClick = {
                    navActions.navigateToLogin()
                }
            )
        }

        composable<LOGIN_ROUTE>{
            LoginRoute(
                onLoginSuccess = {
                    navActions.navigateToHome()
                },
                onForgetPassClick = {
                    navActions.navigateToForgetPassword()
                },
                onSignUpClick = {
                    navActions.navigateToSignUp()
                }
            )
        }

        composable<SIGNUP_ROUTE>{
            RegisterRoute(
                onLoginClick = {
                    navActions.navigateToLogin()
                },
                onSignUpSuccess = {
                    navActions.navigateToHome()
                },
                onForgetPassClick = {
                    navActions.navigateToForgetPassword()
                }
            )
        }

        composable<HOME_ROUTE> {
            EventRoute(
                onEventClick = navActions::navigateToEventDetail,
                onHomeClick = { },
                onMyEventsClick = navActions::navigateToProfile,
                onClubsClick = navActions::navigateToClubs,
                onOppurtunitiesClick = navActions::navigateToOpportunities,
                onProfileClick = navActions::navigateToProfile
            )
        }

        composable<FORGOTPASS_ROUTE> {
            Text("Use the existing password reset screen from your auth feature.")
        }
        composable<EVENT_FILTER_ROUTE> {
            EventFilterRoute(
                onBackClick = { navController.popBackStack() },
                onEventClick = navActions::navigateToEventDetail
            )
        }
        composable<EVENT_DETAIL_ROUTE> { entry ->
            val route = entry.toRoute<EVENT_DETAIL_ROUTE>()
            EventDetailRoute(
                eventId = route.eventId,
                onBackClick = { navController.popBackStack() },
                onRegisterClick = navActions::navigateToRegistration
            )
        }
        composable<REGISTRATION_ROUTE> { entry ->
            val route = entry.toRoute<REGISTRATION_ROUTE>()
            RegistrationRoute(
                eventId = route.eventId,
                onBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }
        composable<PROFILE_ROUTE> {
            ProfileScreen(onLogout = {
                navController.navigate(ONBOARDING_ROUTE) {
                    popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                }
            })
        }
        composable<CLUBS_ROUTE> {
            ClubsRoute(onClubClick = navActions::navigateToClubDetail)
        }
        composable<CLUB_DETAIL_ROUTE> { entry ->
            val route = entry.toRoute<CLUB_DETAIL_ROUTE>()
            Text("Club ${route.clubId}: connect the club-events API to show its events.")
        }
        composable<OPPORTUNITIES_ROUTE> {
            OpportunitiesRoute(onOpportunityClick = {})
        }
    }
}


