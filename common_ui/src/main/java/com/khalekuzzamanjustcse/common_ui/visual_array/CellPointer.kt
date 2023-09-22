package com.khalekuzzamanjustcse.common_ui.visual_array

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset

@Composable
fun CellPointerComposable(
    cellSize: Dp,
    label: String,
    icon: ImageVector = Icons.Default.KeyboardArrowUp,
    currentPosition: Offset = Offset.Zero,
) {
    val offsetAnimation by animateOffsetAsState(currentPosition, label = "")
    Column(
        modifier = Modifier
            .size(cellSize)
            .offset {
                IntOffset(offsetAnimation.x.toInt(), offsetAnimation.y.toInt())
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
        Text(
            text = label,
            modifier = Modifier
        )
    }
}
