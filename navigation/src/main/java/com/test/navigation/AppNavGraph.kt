package com.test.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.test.contact_list.nav.contactListGraph

@Composable
fun AppNavGraph(navController: NavHostController, startDestination: String, modifier: Modifier) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        contactListGraph(navController)
    }
}