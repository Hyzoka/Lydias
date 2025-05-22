package com.test.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun AvatarUser(
    avatar: String,
    name: String
) {
    Surface(
        modifier = Modifier
            .size(130.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(avatar)
                .crossfade(true)
                .build()
        )

        when (painter.state) {
            is AsyncImagePainter.State.Error -> {
                InitialCircleAvatar(
                    fullName = name, modifier = Modifier
                        .height(100.dp)
                        .clip(shape = RoundedCornerShape(12.dp))
                )
            }

            else -> {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .height(100.dp)
                        .clip(shape = RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

    }
}