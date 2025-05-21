package com.test.contact_list.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.test.domain.repo.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    repository: ContactRepository,
) : ViewModel() {

    val contactPagingFlow = repository.getPaginatedContacts().cachedIn(viewModelScope)
}