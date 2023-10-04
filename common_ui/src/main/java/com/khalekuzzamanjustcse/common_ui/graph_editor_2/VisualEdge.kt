package com.khalekuzzamanjustcse.common_ui.graph_editor_2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Preview
@Composable
fun EdgeDrawPreview() {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .drawBehind {
                drawEdge(start = Offset.Zero, end = Offset(150f, 0f), hasDirection = false)
                drawEdge(start = Offset(0f, 100f), end = Offset(150f, 100f), hasDirection = true)
                drawEdge(start = Offset(50f, 200f), controlPoint =Offset(150f,180f) , end = Offset(250f, 200f), hasDirection = true)
                drawEdge(end = Offset(50f, 220f), controlPoint =Offset(150f,240f) , start = Offset(250f, 220f), hasDirection = true)
            }
    ) {

    }
}

fun DrawScope.drawEdge(
    start: Offset,
    end: Offset,
    controlPoint: Offset? = null,
    hasDirection: Boolean
) {

    var midpoint = Offset((start.x + end.x) / 2, (start.y + end.y) / 2)
    if (controlPoint != null)
        midpoint = controlPoint
    val path = Path().apply {
        moveTo(start.x, start.y)
        quadraticBezierTo(midpoint.x, midpoint.y, end.x, end.y)
    }
    val pathMeasured = PathMeasure().apply {
        setPath(path, false)
    }
    val headStart = pathMeasured.getPosition(pathMeasured.length - 25)


    drawPath(
        path = path, color = Color.Black, style = Stroke(3f)
    )
    if (hasDirection) {
        rotate(30f, end) {
            drawLine(
                color = Color.Black,
                start = headStart,
                end = end,
                strokeWidth = 3f
            )
        }
        rotate(-30f, end) {
            drawLine(
                color = Color.Black,
                start = headStart,
                end = end,
                strokeWidth = 3f
            )

        }
    }


}


fun calculateCoordinates(
    angleDegree: Float, radius: Float,
    center: Offset
): Offset {
    val angleRadian = Math.toRadians(angleDegree.toDouble())
    val x = center.x + radius * cos(angleRadian).toFloat()
    val y = center.y + radius * sin(angleRadian).toFloat()
    return Offset(x, y)
}