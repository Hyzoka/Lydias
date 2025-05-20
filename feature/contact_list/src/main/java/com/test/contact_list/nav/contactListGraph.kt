package com.test.contact_list.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.test.contact_list.screen.ContactListScreen
import com.test.core.DestinationRoute

fun NavGraphBuilder.contactListGraph(navController: NavHostController) {
    composable(DestinationRoute.CONTACT_LIST_SCREEN) {
        ContactListScreen { user ->
            navController.navigate(DestinationRoute.CONTACT_DETAILS_SCREEN)
        }
    }
}
