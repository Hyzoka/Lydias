package com.test.contact_list.screen

import com.test.domain.User

data class ContactListState(
    val contacts: List<User?>? = null
)
