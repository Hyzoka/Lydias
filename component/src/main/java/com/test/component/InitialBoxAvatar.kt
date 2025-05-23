package com.test.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun InitialCircleAvatar(
    fullName: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.White
) {
    val initials = rememberSaveable(fullName) {
        fullName.split(" ")
            .filter { it.isNotBlank() }
            .mapNotNull { it.firstOrNull()?.uppercase() }
            .take(2)
            .joinToString("")
    }

    val gradientColors = listOf(
        listOf(Color(0xFFFFA726), Color(0xFFFF7043)),
        listOf(Color(0xFFEC407A), Color(0xFFFFA726)),
        listOf(Color(0xFF66BB6A), Color(0xFF42A5F5)),
        listOf(Color(0xFF42A5F5), Color(0xFFAB47BC)),
        listOf(Color(0xFFAB47BC), Color(0xFFEC407A)),
        listOf(Color(0xFF26C6DA), Color(0xFF00ACC1)),
        listOf(Color(0xFFFFB300), Color(0xFFF57C00)),
        listOf(Color(0xFF8D6E63), Color(0xFF795548)),
        listOf(Color(0xFF7E57C2), Color(0xFF5E35B1)),
    )

    val directions = listOf(
        Pair(Offset.Zero, Offset(100f, 100f)),
        Pair(Offset(100f, 0f), Offset(0f, 100f)),
        Pair(Offset(0f, 100f), Offset(100f, 0f)),
        Pair(Offset(100f, 100f), Offset(0f, 0f)),
    )

    val selectedColors = remember { gradientColors.random() }
    val direction = remember { directions.random() }

    val brush = Brush.linearGradient(
        colors = selectedColors,
        start = direction.first,
        end = direction.second
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(brush)
    ) {
        Text(
            text = initials,
            color = textColor,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
