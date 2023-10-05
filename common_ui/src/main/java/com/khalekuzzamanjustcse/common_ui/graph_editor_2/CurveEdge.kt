package com.khalekuzzamanjustcse.common_ui.graph_editor_2

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.common_ui.graph_editor.GraphEditorVisualEdge

/*
Important note:
If the quadratic bezier curve is not a straight line
then the curve is not passed through the control points.so do not assume that the
curve midpoint and the control points are not will be same or the control passes through line line
 */

@Preview
@Composable
fun CurveEdge() {
    val textMeasure = rememberTextMeasurer()
    val touchTargetPx = 40.dp.value * LocalDensity.current.density
    val edgeManger = remember {
        GraphEditorVisualEdgeMangerImp(touchTargetPx)
    }
    val edges = edgeManger.edges.collectAsState().value

    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .drawBehind {
                edges.forEach { edge ->
                    drawEdge(textMeasurer = textMeasure, edge = edge)
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { _, dragAmount ->
                        edgeManger.onCanvasDragging(dragAmount)
                    }, onDragStart = edgeManger::onDragStart,
                    onDragEnd = edgeManger::onDragEnd

                )
            }
    ) {


    }


}

fun DrawScope.drawEdge(
    edge: GraphEditorVisualEdge,
    textMeasurer: TextMeasurer? = null,
) {
    val path = edge.path
    val pathCenter = edge.pathCenter
    val anchorPointRadius = edge.anchorPointRadius
    val arrowHeadPosition = edge.arrowHeadPosition
    val slope = edge.slopDegree

    //drawEdge
    drawPath(path = path, color = Color.Black, style = Stroke(5f))
    //drawCost
    if (textMeasurer != null) {
        edge.edgeCost?.let { text ->
            val textHalfWidth = textMeasurer.measure(text).size.width / 2
            rotate(slope, pathCenter) {
                drawText(
                    text = text,
                    topLeft = pathCenter - Offset(textHalfWidth.toFloat(), 0f),
                    textMeasurer = textMeasurer
                )
            }
        }
    }
    //draw anchor point
    drawCircle(color = Color.Red, radius = anchorPointRadius, center = pathCenter)
    //draw Arrow head
    if (edge.isDirected) {
        rotate(30f, edge.end) {
            drawLine(
                color = Color.Black,
                start = arrowHeadPosition,
                end = edge.end,
                strokeWidth = 4f
            )
        }
        rotate(-30f, edge.end) {
            drawLine(
                color = Color.Black,
                start = arrowHeadPosition,
                end = edge.end,
                strokeWidth = 4f
            )
        }
    }
}


