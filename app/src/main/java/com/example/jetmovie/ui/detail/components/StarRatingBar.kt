package com.example.jetmovie.ui.detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StarRatingBar(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        (1..5).forEach { index ->
            Icon(
                imageVector = if (index <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = "$index stars",
                tint = if (index <= rating) Color.Yellow else Color.Gray,
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onRatingChanged(index) }
            )
        }
    }
}
