package com.test.contact_list.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.test.component.ErrorItem
import com.test.component.LoadingItem
import com.test.component.R
import com.test.component.UserItem
import kotlinx.coroutines.launch

@Composable
fun ContactListScreen(
    viewModel: ContactListViewModel = hiltViewModel(),
    onContactClick: (String) -> Unit = {}
) {
    val contactItems = viewModel.contactPagingFlow.collectAsLazyPagingItems()
    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(isConnected) {
        if (!isConnected) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.your_offline),
                    duration = SnackbarDuration.Long,
                )
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(), contentPadding = padding
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
                            LoadingItem(text = stringResource(id = R.string.loading_contacts))
                        }
                    }

                    loadState.append is LoadState.Loading -> {
                        item {
                            LoadingItem(text = stringResource(id = R.string.loading_more_contacts))
                        }
                    }

                    loadState.refresh is LoadState.Error -> {
                        item {
                            ErrorItem(
                                message = stringResource(id = R.string.unknown_error),
                                onRetry = { retry() }
                            )
                        }
                    }

                    loadState.append is LoadState.Error -> {
                        item {
                            ErrorItem(
                                message = stringResource(id = R.string.pagination_error),
                                onRetry = { retry() }
                            )
                        }
                    }
                }
            }
        }
    }
}



