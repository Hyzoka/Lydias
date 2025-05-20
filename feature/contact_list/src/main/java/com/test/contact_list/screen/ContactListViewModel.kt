package com.test.contact_list.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.domain.repo.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val repository: ContactRepository,
) : ViewModel() {

    private val _contactListState = MutableStateFlow(ContactListState())
    val contactListState = _contactListState.asStateFlow()

    init {
        fetchContactList()
    }

    private fun fetchContactList(page: Int = 1) {
        viewModelScope.launch {
            repository.fetchContactsPage(page = page).onSuccess { data ->
                _contactListState.update { it.copy(contacts = data) }
            }.onFailure {
                println("Fetch contact list failed : $it")
            }
        }
    }
}