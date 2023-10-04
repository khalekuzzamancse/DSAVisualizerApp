package com.khalekuzzamanjustcse.common_ui.graph_editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.VectorProperty
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Preview
@Composable
fun ConnectableNode() {

    Canvas(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
    }
}
fun DrawScope.drawVector(
    modifier: Modifier = Modifier,
    parentSize: Size,
    sizeOfGrid: Size,
    directions: Pair<Int, Int>, // (2, 1) -> move x-axis 2 grid right, move y-axis 1 grid up
) {
    val originX = parentSize.width / 2f
    val originY = parentSize.height / 2f

    drawLine(
        color = Color.Green,
        start = Offset(x = originX, y = originY),
        end = Offset(
            x = originX + directions.first * sizeOfGrid.width,
            y = originY + -(directions.second) * sizeOfGrid.height
        ),
        strokeWidth = 10f
    )

    // Have to make directions.second negative to adjust with canvas coordinate system
    val arrowPath = Path()
    arrowPath.moveTo(
        x = (originX + directions.first * sizeOfGrid.width) - 20f,
        y = (originY + -(directions.second) * sizeOfGrid.height) - 20f
    )
    arrowPath.lineTo(
        x = (originX + directions.first * sizeOfGrid.width) + 20f,
        y = (originY + -(directions.second) * sizeOfGrid.height) + 20f
    )
    arrowPath.lineTo(
        x = (originX + directions.first * sizeOfGrid.width) + 20f,
        y = (originY + -(directions.second) * sizeOfGrid.height) - 20f
    )
    arrowPath.close()
    drawPath(arrowPath, color = Color.Green)
}