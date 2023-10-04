package com.khalekuzzamanjustcse.common_ui.graph_editor_2

import android.util.Log
import android.util.Range
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.common_ui.graph_editor.GraphEditorVisualEdge
import com.khalekuzzamanjustcse.common_ui.graph_editor.GraphEditorVisualEdgeImp
import kotlin.math.atan2


@Preview
@Composable
fun CurveEdge() {

    val textMeasure = rememberTextMeasurer()
    var dragStartAt by remember {
        mutableStateOf<Offset?>(null)
    }
    var enableDrag by remember {
        mutableStateOf(false)
    }
    var currentlyDragEdgeId by remember {
        mutableIntStateOf(-1111)
    }

    var edges by remember {
        mutableStateOf(
            listOf(
                GraphEditorVisualEdgeImp(
                    id = 1,
                    start = Offset(50f, 100f),
                    end = Offset(200f, 100f),
                    edgeCost = "55 Tk.",
                    isDirected = true
                ),
                GraphEditorVisualEdgeImp(
                    id = 2,
                    start = Offset(150f, 200f),
                    end = Offset(300f, 200f),
                    edgeCost = "50 Tk.",
                    isDirected = true
                )
            )
        )
    }

    val touchTargetPx = 40.dp.value * LocalDensity.current.density

    val updateAnchorPoint: (Int, Offset) -> Unit = { id, dragAmount ->
        edges = edges.map { edge ->
            if (edge.id == id) {
                edge.onAnchorPointDrag(dragAmount)
            } else edge
        }
    }

    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .drawBehind {
                edges.forEach { edge ->
                    val edgeDrawer = EdgeDrawer(
                        textMeasurer = textMeasure,
                        edge = edge,
                        onControlPointTap = { tappedEdge ->
                            Log.i("ControlPointTap:", "${tappedEdge.id}")
                            currentlyDragEdgeId=tappedEdge.id
                            enableDrag=true
                        }
                    )
                    edgeDrawer.draw(this)
                    edgeDrawer.observeCanvasTap(dragStartAt, touchTargetPx)
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { _, dragAmount ->
                        if (enableDrag) {
                            updateAnchorPoint(currentlyDragEdgeId, dragAmount)
                        }

                    }, onDragStart = {
                        dragStartAt = it
                    },
                    onDragEnd = {
                        dragStartAt = null
                        currentlyDragEdgeId = -111
                    }
                )


            }
    ) {


    }


}


class EdgeDrawer(
    private val edge: GraphEditorVisualEdge,
    private val textMeasurer: TextMeasurer? = null,
    private val onControlPointTap: (GraphEditorVisualEdge) -> Unit = {}
) {
    private lateinit var drawScope: DrawScope
    val path = createCurvePath()
    private val pathMeasurer = PathMeasure().apply { setPath(path, false) }
    private val pathLength = pathMeasurer.length
    private val pathCenter = pathMeasurer.getPosition(pathLength / 2)
    private val anchorPointRadius = 5f


    fun draw(drawScope: DrawScope) {
        this.drawScope = drawScope
        drawEdge()
        drawArrowHeads()
        drawEdgeCost()
        drawAnchorPoint()
    }

    private fun drawEdge() {
        drawScope.drawPath(path = path, color = Color.Black, style = Stroke(5f))
    }


    fun observeCanvasTap(lastTappedPosition: Offset?, minTouchTargetPX: Float) {
        lastTappedPosition?.let { position ->
            val isTouched = position.x in Range(
                pathCenter.x - minTouchTargetPX / 2,
                pathCenter.x + minTouchTargetPX / 2
            ) &&
                    position.y in Range(
                pathCenter.y - minTouchTargetPX / 2,
                pathCenter.y + minTouchTargetPX / 2
            )
            if (isTouched) {
                onControlPointTap(edge)
            }
        }

    }

    private fun drawEdgeCost() {
        if (textMeasurer != null) {
            edge.edgeCost?.let { text ->
                val textHalfWidth = textMeasurer.measure(text).size.width / 2
                val (endX, endY) = edge.end
                val (startX, startY) = edge.start
                val slop = atan2(endY - startY, endX - startX)
                val angleDegrees = Math.toDegrees(slop.toDouble())
                drawScope.rotate(angleDegrees.toFloat(), pathCenter) {
                    drawText(
                        text = text,
                        topLeft = pathCenter - Offset(textHalfWidth.toFloat(), 0f),
                        textMeasurer = textMeasurer
                    )
                }
            }


        }
    }

    private fun drawAnchorPoint() {
        drawScope.drawCircle(
            color = Color.Red,
            radius = anchorPointRadius,
            center = pathCenter
        )
        Log.i("AnchorPointCenter:", "$pathCenter")
    }

    private fun createCurvePath(
    ): Path {
        return Path().apply {
            moveTo(edge.start.x, edge.start.y)
            quadraticBezierTo(edge.controlPoint.x, edge.controlPoint.y, edge.end.x, edge.end.y)
        }
    }

    private fun drawArrowHeads() {
        val arrowHeadPosition = pathMeasurer.getPosition(pathMeasurer.length - 40)
        drawScope.rotate(30f, edge.end) {
            drawScope.drawLine(
                color = Color.Black,
                start = arrowHeadPosition,
                end = edge.end,
                strokeWidth = 4f
            )
        }
        drawScope.rotate(-30f, edge.end) {
            drawScope.drawLine(
                color = Color.Black,
                start = arrowHeadPosition,
                end = edge.end,
                strokeWidth = 4f
            )
        }
    }
}

