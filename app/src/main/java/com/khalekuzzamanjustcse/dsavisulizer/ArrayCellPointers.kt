package com.khalekuzzamanjustcse.dsavisulizer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun CellPointerPreview() {
    CellPointer(
        label = "i",
    )
}
data class  ArrayCellPointers(
    val label:String,
    val currentPointingIndex:Int?=null,
    val icon: ImageVector,
)
@Composable
fun CellPointer(
    modifier: Modifier = Modifier,
    label: String,
    icon: ImageVector = Icons.Default.KeyboardArrowDown,
    currentOffset: Offset = Offset(0f, 0f),
    size: Dp = 100.dp,
) {
    Column(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                translationX = currentOffset.x
                translationY = currentOffset.y
            },
        horizontalAlignment =Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text=label)
        Icon(
            modifier = Modifier,
            imageVector = icon,
            contentDescription = null
        )
    }
}
