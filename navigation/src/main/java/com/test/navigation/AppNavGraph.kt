package com.test.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.test.contact_list.screen.ContactListScreen

@Composable
fun AppNavGraph(navController: NavHostController, startDestination: String, modifier: Modifier) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(DestinationRoute.CONTACT_LIST_SCREEN) {
            ContactListScreen { userId ->
                navController.navigate(DestinationRoute.CONTACT_DETAILS_SCREEN)
            }
        }
    }
}