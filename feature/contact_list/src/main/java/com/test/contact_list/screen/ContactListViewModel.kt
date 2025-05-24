package com.test.contact_list.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.test.domain.NetworkConnectivityHelper
import com.test.domain.repo.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    repository: ContactRepository,
    private val networkConnectivity: NetworkConnectivityHelper
) : ViewModel() {

    val contactPagingFlow = repository.getPaginatedContacts().cachedIn(viewModelScope)
    val isConnected: StateFlow<Boolean> = networkConnectivity.isConnected

}