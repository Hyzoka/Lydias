package com.test.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest


@Composable
fun UserItem(avatar: String, name: String, email: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(
                5.dp
            )
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(15.dp),
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))

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
                        .crossfade(true) // ✅ Activation du fade-in
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
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    modifier = Modifier.padding(end = 25.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)

                )
            }
        }
    }
}