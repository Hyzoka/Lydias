package com.test.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState

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

    Circle(
        center = latLng,
        clickable = false,
        fillColor = Color(0xff2FAA59).copy(alpha = alpha),
        radius = radius.toDouble(), // en mètres
        strokeColor = Color.Transparent,
        strokeWidth = 0f
    )

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