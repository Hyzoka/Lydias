package com.test.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RoundedCornerShapeColumn(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.onSecondary,
                    shape = RoundedCornerShape(8.dp),
                )
                .padding(16.dp),
        ),
        verticalArrangement = Arrangement.Center,
    ) { content() }
}

@Composable
fun RoundedCornerShapeRow(
    modifier: Modifier = Modifier,
    onRowClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .clickable { onRowClick() }
                .background(
                    color = MaterialTheme.colorScheme.onSecondary,
                    shape = RoundedCornerShape(8.dp),
                )
                .padding(16.dp),
        ),
        horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically
    ) { content() }
}
