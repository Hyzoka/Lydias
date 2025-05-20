package com.test.contact_list.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.test.domain.User

@Composable
fun ContactListScreen(
    viewModel: ContactListViewModel = hiltViewModel(),
    onContactClick: (User) -> Unit
) {

    val state by viewModel.contactListState.collectAsStateWithLifecycle()
    println("AEAZEAZZA data contact = ${state.contacts}")
}