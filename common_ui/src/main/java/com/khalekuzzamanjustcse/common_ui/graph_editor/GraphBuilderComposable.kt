package com.khalekuzzamanjustcse.common_ui.graph_editor

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun GraphBuilder(
    modifier: Modifier = Modifier,
    nodes: List<GraphEditorNode>,
    edges: List<DrawingEdge>,
    onCanvasTapped: (Offset) -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                edges.forEach { edgeState ->
                    drawEdge(edgeState)
                }
            }
            .pointerInput(Unit) {
                detectTapGestures {
                    onCanvasTapped(it)
                }
            }
    ) {
        nodes.forEach { nodeState ->
            GraphEditorNodeComposable(nodeState)
        }

    }
}


fun DrawScope.drawEdge(
    state: DrawingEdge,
) {
    drawLine(
        color = Color.Black, start = state.start,
        end = state.end, strokeWidth = 4f,
    )
//    drawPath(
//        path = createArrowPath(state.startPoint, state.endPoint),
//        color = state.color,
//        style = Fill
//    )


}

fun createArrowPath(
    start: Offset, end: Offset,
): Path {
    val arrowSize = 20f
    val arrowAngle = 45f
    val dx = end.x - start.x
    val dy = end.y - start.y
    val lineAngle = atan2(dy, dx) * 180 / PI.toFloat()
    val leftAngle = lineAngle + arrowAngle
    val rightAngle = lineAngle - arrowAngle
    val leftX = end.x - arrowSize * cos(leftAngle * PI / 180).toFloat()
    val leftY = end.y - arrowSize * sin(leftAngle * PI / 180).toFloat()
    val rightX = end.x - arrowSize * cos(rightAngle * PI / 180).toFloat()
    val rightY = end.y - arrowSize * sin(rightAngle * PI / 180).toFloat()
    return Path().apply {
        moveTo(end.x, end.y)
        lineTo(leftX, leftY)
        lineTo(rightX, rightY)
        close()
    }
}



