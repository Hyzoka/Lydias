package com.test.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun InfoActionRow(
    @DrawableRes icon: Int,
    text: @Composable () -> String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    RoundedCornerShapeRow(onRowClick = onClick, modifier = modifier) {
        Icon(
            modifier = Modifier.padding(end = 8.dp),
            painter = painterResource(icon),
            contentDescription = null
        )
        Text(
            text = text(),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
