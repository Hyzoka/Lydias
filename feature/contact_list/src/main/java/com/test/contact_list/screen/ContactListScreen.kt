package com.test.contact_list.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.test.component.ErrorItem
import com.test.component.LoadingItem
import com.test.component.UserItem

@Composable
fun ContactListScreen(
    viewModel: ContactListViewModel = hiltViewModel(),
    onContactClick: (String) -> Unit = {}
) {
    val contactItems = viewModel.contactPagingFlow.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(contactItems.itemCount) { index ->
            val contact = contactItems[index]
            contact?.let {
                UserItem(
                    avatar = it.pictureUrl,
                    name = it.fullName,
                    email = it.email,
                    onClick = { onContactClick(it.email) })
            }
        }

        contactItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        LoadingItem(text = "Chargement des contacts...")
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item {
                        LoadingItem(text = "Chargement de plus de contacts...")
                    }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = loadState.refresh as LoadState.Error
                    item {
                        ErrorItem(
                            message = error.error.localizedMessage ?: "Erreur inconnue",
                            onRetry = { retry() }
                        )
                    }
                }

                loadState.append is LoadState.Error -> {
                    val error = loadState.append as LoadState.Error
                    item {
                        ErrorItem(
                            message = error.error.localizedMessage
                                ?: "Erreur lors de la pagination",
                            onRetry = { retry() }
                        )
                    }
                }
            }
        }
    }
}


