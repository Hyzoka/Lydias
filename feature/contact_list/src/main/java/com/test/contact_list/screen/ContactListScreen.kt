package com.test.contact_list.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.test.domain.model.User

@Composable
fun ContactListScreen(
    viewModel: ContactListViewModel = hiltViewModel(),
    onContactClick: (User) -> Unit = {}
) {
    val contactItems = viewModel.contactPagingFlow.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(contactItems.itemCount) { index ->
            val contact = contactItems[index]
            contact?.let {
                ContactItem(user = it, onClick = { onContactClick(it) })
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


@Composable
fun ContactItem(user: User, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(text = user.fullName, style = MaterialTheme.typography.titleMedium)
        Text(text = user.email, style = MaterialTheme.typography.bodySmall)
    }
}

//@Composable
//fun EmployeeItem(user: User, onClick: () -> Unit) {
//  Card(
//      modifier = Modifier
//          .padding(bottom = 5.dp, top = 5.dp,
//              start = 5.dp, end = 5.dp)
//          .fillMaxWidth()
//          .clickable(onClick = onClick),
//      shape = RoundedCornerShape(15.dp),
//  ) {
//      Row(
//          modifier = Modifier
//              .clip(RoundedCornerShape(4.dp))
//              .background(MaterialTheme.colorScheme.surface)
//      ) {
//          Surface(
//              modifier = Modifier.size(130.dp),
//              shape = RoundedCornerShape(12.dp),
//              color = MaterialTheme.colorScheme.surface.copy(
//                  alpha = 0.2f)
//          ) {
//              val image = rememberCoilPainter(
//                  request = user.pictureUrl,
//                  fadeIn = true)
//              Image(
//                  painter = image,
//                  contentDescription = null,
//                  modifier = Modifier
//                      .height(100.dp)
//                      .clip(shape = RoundedCornerShape(12.dp)),
//                  contentScale = ContentScale.Crop
//              )
//          }
//          Column(
//              modifier = Modifier
//                  .padding(start = 12.dp)
//                  .align(Alignment.CenterVertically)
//          ) {
//              Text(
//                  text = user.first_name,
//                  fontWeight = FontWeight.Bold,
//                  style = TextStyle(fontSize = 22.sp),
//                  color = Color.Black
//              )
//              CompositionLocalProvider(
//                  LocalContentAlpha provides ContentAlpha.medium
//              ) {
//                  Text(
//                      text = user.email,
//                      style = MaterialTheme.typography.bodyMedium,
//                      maxLines = 1,
//                      overflow = TextOverflow.Ellipsis,
//                      modifier = Modifier.padding(end = 25.dp)
//                  )
//              }
//          }
//      }
//  }
//}

@Composable
fun LoadingItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}

@Composable
fun ErrorItem(message: String, onRetry: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Réessayer")
        }
    }
}
