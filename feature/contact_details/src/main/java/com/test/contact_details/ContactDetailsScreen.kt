package com.test.contact_details

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.test.component.AvatarUser
import com.test.component.RoundedCornerShapeRow

@Composable
fun ContactDetailsScreen(
    email: String,
    onBackPress: () -> Unit,
    viewModel: ContactDetailsViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsStateWithLifecycle()

    val scrollState = rememberLazyListState()
    val context = LocalContext.current
    val scrollOffset by remember { derivedStateOf { scrollState.firstVisibleItemScrollOffset } }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE


    val mapHeight = if (isLandscape) {
        max(50.dp, 100.dp - (scrollOffset / 2).dp)
    } else {
        max(150.dp, 300.dp - (scrollOffset / 2).dp)
    }

    val avatarSize = max(90.dp, 130.dp - (scrollOffset / 5).dp)

    user?.let {
        Box {
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(mapHeight + 24.dp), // hide google logo :)
                cameraPositionState = rememberCameraPositionState(), uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    compassEnabled = false,
                    myLocationButtonEnabled = false,
                    mapToolbarEnabled = false
                )
            )

            Image(
                painter = painterResource(com.test.component.R.drawable.baseline_arrow_back_24),
                contentDescription = "Arrow Back",
                modifier = Modifier
                    .clickable { onBackPress() }
                    .padding(16.dp)
            )
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = mapHeight)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(MaterialTheme.colorScheme.surface)

            ) {
                item {
                    Spacer(modifier = Modifier.height(avatarSize / 2 + 16.dp))
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        Text(
                            text = it.fullName,
                            style = MaterialTheme.typography.headlineSmall,
                        )

                        RoundedCornerShapeRow(onRowClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:${it.email}")
                            }
                            context.startActivity(intent)
                        }) {
                            Icon(
                                modifier = Modifier.padding(end = 8.dp),
                                painter = painterResource(com.test.component.R.drawable.outline_alternate_email_24),
                                contentDescription = "email"
                            )
                            Text(
                                text = it.email,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }

                        RoundedCornerShapeRow(onRowClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${it.phone}")
                            }
                            context.startActivity(intent)
                        }) {
                            Icon(
                                modifier = Modifier.padding(end = 8.dp),
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Téléphone"
                            )
                            Text(
                                text = it.phone,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }

                        RoundedCornerShapeRow(onRowClick = {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("geo:0,0?q=${it.location}")
                            }
                            context.startActivity(intent)
                        }) {
                            Icon(
                                modifier = Modifier.padding(end = 8.dp),
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "location"
                            )
                            Text(
                                text = it.location,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }

                        RoundedCornerShapeRow {
                            Icon(
                                modifier = Modifier.padding(end = 8.dp),
                                painter = painterResource(com.test.component.R.drawable.cake_icon),
                                contentDescription = "birthday"
                            )

                            Text(
                                text = "${it.age} ans",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        RoundedCornerShapeRow {
                            Icon(
                                modifier = Modifier.padding(end = 8.dp),
                                painter = painterResource(com.test.component.R.drawable.material_symbols_icon),
                                contentDescription = "nationality"
                            )

                            Text(
                                text = it.nationality,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
            }
            // Avatar centré
            AvatarUser(
                avatar = it.pictureUrl,
                name = it.fullName,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = mapHeight - (avatarSize / 2))
                    .size(avatarSize)
            )
        }
    }
}


