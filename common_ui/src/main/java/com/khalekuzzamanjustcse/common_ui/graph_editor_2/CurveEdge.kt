package com.khalekuzzamanjustcse.common_ui.graph_editor_2

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.atan2

@Preview
@Composable
fun CurveEdge() {
    val start = Offset(50f, 100f)
    val end = Offset(200f, 100f)
    val x1 = (start.x + end.x) / 2
    val y1 = (start.y + end.y) / 2
    var controlPoint by remember {
        mutableStateOf(Offset(x1, y1 + 30f))
    }
    val textMeasure = rememberTextMeasurer()

    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .drawBehind {
                withTransform(
                    {

                    }
                ) {

                    EdgeDrawer(
                        drawScope = this,
                        start = start,
                        controlPoint = controlPoint,
                        end = end,
                        textMeasurer = textMeasure,
                        edgeCostLabel = "55 Tk."
                    ).draw()


                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { _, dragAmount ->
                        controlPoint += dragAmount
                    }
                )

            }
    ) {


    }


}


private fun DrawScope.drawEdge(
    start: Offset,
    controlPoint: Offset,
    end: Offset,
    textMeasurer: TextMeasurer? = null,
    edgeCostLabel: String? = null,
) {
    val path = Path().apply {
        moveTo(start.x, start.y)
        quadraticBezierTo(controlPoint.x, controlPoint.y, end.x, end.y)
    }

    val pathMeasurer = PathMeasure().apply {
        setPath(path, false)
        if (edgeCostLabel != null && textMeasurer != null) {
            val center = getPosition(length / 2)
            val textHalfWidth = textMeasurer.measure(edgeCostLabel).size.width / 2
            drawText(
                textMeasurer = textMeasurer,
                text = edgeCostLabel,
                topLeft = center - Offset(textHalfWidth.toFloat(), 0f)
            )
        }
    }

    val arrowHeadPosition = pathMeasurer.getPosition(pathMeasurer.length - 40)
    drawPath(
        path = path, color = Color.Black, style = Stroke(5f)
    )

    rotate(30f, end) {
        drawLine(
            color = Color.Black,
            start = arrowHeadPosition,
            end = end,
            strokeWidth = 4f
        )
    }
    rotate(-30f, end) {
        drawLine(
            color = Color.Black,
            start = arrowHeadPosition,
            end = end,
            strokeWidth = 4f
        )

    }


}


class EdgeDrawer(
    private val drawScope: DrawScope,
    private val start: Offset,
    private val controlPoint: Offset,
    private val end: Offset,
    private val edgeCostLabel: String? = null,
    private val textMeasurer: TextMeasurer? = null,
) {
    val path = createCurvePath()
    private val pathMeasurer = PathMeasure().apply { setPath(path, false) }
    private val pathLength = pathMeasurer.length
    private val pathCenter = pathMeasurer.getPosition(pathLength / 2)

    fun draw() {
        drawEdge()
        drawArrowHeads()
        drawEdgeCost()
    }

    private fun drawEdge() {
        drawScope.drawPath(path = path, color = Color.Black, style = Stroke(5f))
    }

    private fun drawEdgeCost() {
        if (edgeCostLabel != null && textMeasurer != null) {
            val textHalfWidth = textMeasurer.measure(edgeCostLabel).size.width / 2
            val (endX, endY) = end
            val (startX, startY) = start
            val slop = atan2(endY - startY, endX - startX)
            val angleDegrees = Math.toDegrees(slop.toDouble())
            drawScope.rotate(angleDegrees.toFloat(), pathCenter) {
                drawText(
                    text = edgeCostLabel,
                    topLeft = pathCenter - Offset(textHalfWidth.toFloat(), 0f),
                    textMeasurer = textMeasurer
                )
            }

        }
    }

    private fun createCurvePath(
    ): Path {
        return Path().apply {
            moveTo(start.x, start.y)
            quadraticBezierTo(controlPoint.x, controlPoint.y, end.x, end.y)
        }
    }

    private fun drawArrowHeads() {
        val arrowHeadPosition = pathMeasurer.getPosition(pathMeasurer.length - 40)
        drawScope.rotate(30f, end) {
            drawScope.drawLine(
                color = Color.Black,
                start = arrowHeadPosition,
                end = end,
                strokeWidth = 4f
            )
        }
        drawScope.rotate(-30f, end) {
            drawScope.drawLine(
                color = Color.Black,
                start = arrowHeadPosition,
                end = end,
                strokeWidth = 4f
            )
        }
    }
}

