package com.test.contact_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.domain.model.User
import com.test.domain.repo.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ContactDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: ContactRepository
) : ViewModel() {

    private val email = savedStateHandle.get<String>("email") ?: ""

    val user: StateFlow<User?> = repository.getUserByEmail(email)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
