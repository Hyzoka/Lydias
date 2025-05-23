package com.test.contact_details

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Geocoder
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.test.component.AvatarUser
import com.test.component.RoundedCornerShapeRow
import java.io.IOException
import java.util.Locale

@Composable
fun ContactDetailsScreen(
    email: String,
    onBackPress: () -> Unit,
    viewModel: ContactDetailsViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    //list scroll
    val scrollState = rememberLazyListState()
    val scrollOffset by remember { derivedStateOf { scrollState.firstVisibleItemScrollOffset } }
    val mapHeight = if (isLandscape) {
        max(50.dp, 100.dp - (scrollOffset / 2).dp)
    } else {
        max(150.dp, 300.dp - (scrollOffset / 2).dp)
    }
    val avatarSize = max(90.dp, 130.dp - (scrollOffset / 5).dp)

    //map
    val cameraPositionState = rememberCameraPositionState()
    var location by remember { mutableStateOf<LatLng?>(null) }

    user?.let {
        LaunchedEffect(it.location) {
            location = getLatLngFromCity(context, it.location)
            location?.let { loc ->
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLngZoom(loc, 14f),
                    durationMs = 1000
                )
            }
        }
        Box {
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(mapHeight + 24.dp), // hide google logo :)
                cameraPositionState = cameraPositionState, uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    compassEnabled = false,
                    myLocationButtonEnabled = false,
                    mapToolbarEnabled = false
                ), content = {
                    location?.let { loc ->
                        PulseComponentAnimation(latLng = loc)
                    }
                }
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
                                data = "mailto:${it.email}".toUri()
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
                                data = "tel:${it.phone}".toUri()
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
                                data = "geo:0,0?q=${it.location}".toUri()
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
                                stringResource(id = com.test.component.R.string.age_format, it.age),
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


@Composable
fun PulseComponentAnimation(
    maxPulseSize: Float = 150f,
    minPulseSize: Float = 0f,
    latLng: LatLng
) {
    val infiniteTransition = rememberInfiniteTransition(label = "PulseComponentAnimation")

    val radius by infiniteTransition.animateFloat(
        initialValue = minPulseSize,
        targetValue = maxPulseSize,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000),
            repeatMode = RepeatMode.Restart
        ), label = "radius"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000),
            repeatMode = RepeatMode.Restart
        ), label = "alpha"
    )

    // CERCLE pulsant (centré)
    Circle(
        center = latLng,
        clickable = false,
        fillColor = Color(0xff2FAA59).copy(alpha = alpha),
        radius = radius.toDouble(), // en mètres
        strokeColor = Color.Transparent,
        strokeWidth = 0f
    )

    // MARQUEUR au même LatLng
    MarkerComposable(
        state = MarkerState(position = latLng)
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .background(color = Color(0xff2FAA59), shape = CircleShape)
                .border(width = 2.dp, color = Color.White, shape = CircleShape)
        )
    }
}


private fun getLatLngFromCity(context: Context, cityName: String): LatLng? {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocationName(cityName, 1)
        addresses?.let {
            val location = addresses[0]
            LatLng(location.latitude, location.longitude)
        }
    } catch (e: IOException) {
        null
    }
}

