package com.test.navigation

import android.net.Uri

sealed class DestinationRoute(val route: String) {
    object ContactList : DestinationRoute("contact_list_screen_route")

    object ContactDetails : DestinationRoute("contactDetails/{email}") {
        fun withEmail(email: String): String = "contactDetails/${Uri.encode(email)}"
    }
}

object PassedKey {
    const val EMAIL_VALUE = "email"
}


