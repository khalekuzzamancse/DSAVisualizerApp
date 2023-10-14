package com.khalekuzzamanjustcse.graph_editor.ui.ui.edge

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.graph_editor.ui.ui.basic_concept_demo.EdgePoint
import kotlin.math.atan2


data class GraphEditorVisualEdgeImp(
    val id: String,
    val start: Offset,
    val end: Offset,
    val control: Offset,
    val cost: String?,
    val isDirected: Boolean,
    val pathColor: Color = Color.Black,
    val selectedPointColor: Color = Color.Red,
    val showSelectedPoint: Boolean = false,
    val anchorPointRadius: Dp = 4.dp,
    val selectedPoint: EdgePoint = EdgePoint.None,
    val minTouchTargetPx: Float
) {
    companion object {
        private val pathMeasurer = PathMeasure()
    }

    val arrowHeadPosition: Offset
        get() {
            pathMeasurer.setPath(path, false)
            return if (pathMeasurer.length >= 20)
                pathMeasurer.getPosition(pathMeasurer.length - 20)
            else
                Offset.Unspecified
        }

    val path: Path
        get() = Path().apply {
            moveTo(start.x, start.y)
            quadraticBezierTo(control.x, control.y, end.x, end.y)
        }
    val slop: Float
        get() {
            val (x1, y1) = end
            val (x2, y2) = start
            val slop = atan2(y1 - y2, x1 - x2)
            return Math.toDegrees(slop.toDouble()).toFloat()
        }

    val pathCenter: Offset
        get() {
            pathMeasurer.setPath(path, false)
            val pathLength = pathMeasurer.length
            return if (pathMeasurer.length < 20)
                Offset.Unspecified
            else pathMeasurer.getPosition(pathLength / 2)//path center
        }
}