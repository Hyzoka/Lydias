package com.test.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.test.contact_details.ContactDetailsScreen
import com.test.contact_list.screen.ContactListScreen
import com.test.navigation.PassedKey.EMAIL_VALUE

@Composable
fun AppNavGraph(navController: NavHostController, startDestination: String, modifier: Modifier) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(DestinationRoute.ContactList.route) {
            ContactListScreen { email ->
                navController.navigate(DestinationRoute.ContactDetails.withEmail(email))
            }
        }

        composable(
            route = DestinationRoute.ContactDetails.route,
            arguments = listOf(
                navArgument(EMAIL_VALUE) { type = NavType.StringType }
            ),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            }, exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString(EMAIL_VALUE) ?: ""
            ContactDetailsScreen(email = email, onBackPress = {
                navController.popBackStack()
            })
        }
    }
}