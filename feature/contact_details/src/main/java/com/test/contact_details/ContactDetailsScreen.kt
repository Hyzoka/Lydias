package com.test.contact_details

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ContactDetailsScreen(email: String, viewModel: ContactDetailsViewModel = hiltViewModel()) {
    val user by viewModel.user.collectAsStateWithLifecycle()


    user?.let {
        Text(text = it.fullName, modifier = Modifier.fillMaxSize())

    }
}
