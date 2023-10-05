package com.khalekuzzamanjustcse.common_ui.graph_editor

import android.util.Range
import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.Dp
import kotlin.math.atan2


interface GraphBasicNode {
    val id: Int
    val label: String
    val position: Offset
    val sizePx: Float
    val size: Dp
}

interface GraphBasicEdge {
    val startNodeId: Int
    val endNodeId: Int
}

interface VisualEdge {
    val start: Offset
    val end: Offset
    val isDirected: Boolean
}

interface GraphEditorVisualEdge {
    val id: Int
    val start: Offset
    val end: Offset
    val controlPoint: Offset
    val edgeCost: String?
    val isDirected: Boolean
    val showAnchor: Boolean
    val path: Path
    val pathCenter: Offset
    val anchorPointRadius: Float
    val arrowHeadPosition: Offset
    val slopDegree: Float
    val minTouchTargetPx: Float

    fun onAnchorPointDrag(dragAmount: Offset): GraphEditorVisualEdge
    fun isAnchorTouched(touchPosition: Offset): Boolean


}
/*
Important note:
If the quadratic bezier curve is not a straight line
then the curve is not passed through the control points.so do not assume that the
curve midpoint and the control points are not will be same or the control passes through line line.
anchor points will be always in the middle of the path regardless of the control points
 */
data class GraphEditorVisualEdgeImp(
    override val id: Int,
    override val start: Offset,
    override val end: Offset,
    override val controlPoint: Offset = start.plus(end).div(2f),
    override val edgeCost: String?,
    override val isDirected: Boolean,
    override val showAnchor: Boolean = true,
    override val anchorPointRadius: Float = 5f,
    override val minTouchTargetPx: Float =80f
) : GraphEditorVisualEdge {
    private val pathMeasurer = PathMeasure()
    override val path: Path
        get() = Path().apply {
            moveTo(start.x, start.y)
            quadraticBezierTo(controlPoint.x, controlPoint.y, end.x, end.y)
        }


    override val pathCenter: Offset
        get() {
            pathMeasurer.setPath(path, false)
            val pathLength = pathMeasurer.length
            return pathMeasurer.getPosition(pathLength / 2) //path center
        }
    override val arrowHeadPosition: Offset
        get() {
            pathMeasurer.setPath(path, false)
            return pathMeasurer.getPosition(pathMeasurer.length - 40)
        }
    override val slopDegree: Float
        get() {
            val (x1, y1) = end
            val (x2, y2) = start
            val slop = atan2(y1 - y2, x1 - x2)
            return Math.toDegrees(slop.toDouble()).toFloat()
        }

    override fun onAnchorPointDrag(dragAmount: Offset) =
        this.copy(controlPoint = controlPoint + dragAmount)

    override fun isAnchorTouched(touchPosition: Offset): Boolean {
        return touchPosition.x in Range(
            pathCenter.x - minTouchTargetPx / 2,
            pathCenter.x + minTouchTargetPx / 2
        ) &&
                touchPosition.y in Range(
            pathCenter.y - minTouchTargetPx / 2,
            pathCenter.y + minTouchTargetPx / 2
        )
    }

}


data class GraphEditorVisualNode(
    val basicNode: GraphBasicNode,
    val onClick: (GraphEditorVisualNode) -> Unit = {},
    val onDragEnd: (GraphEditorVisualNode) -> Unit = { },
    override val size: Dp,
    override val id: Int = basicNode.id,
    override val label: String = basicNode.label,
    override val position: Offset = basicNode.position,
    override val sizePx: Float = 0f,
    val showAnchor: Boolean,
) : GraphBasicNode


/*
Used the Decorator Design Pattern to give
additional properties and behavior(method)
 */
@Immutable
data class GraphSimulationNode(
    val basicNode: GraphBasicNode,
    val color: Color = Color.Green,
    val backgroundColor: Color = Color.Unspecified,
    override val label: String = basicNode.label,
    override val size: Dp = basicNode.size,
    override val sizePx: Float = basicNode.sizePx,
    override val id: Int = basicNode.id,
    override val position: Offset = Offset.Zero,
) : GraphBasicNode {
    val textColor: Color
        get() = if (color.luminance() > 0.5) Color.Black else Color.White
    private val halfSize: Float
        get() = sizePx / 2
    val center: Offset
        get() = position + Offset(halfSize, halfSize)

    val topCenter: Offset
        get() = position + Offset(halfSize, 0f)
    val bottomCenter: Offset
        get() = Offset(position.x + halfSize, position.y + sizePx)

    val leftCenter: Offset
        get() = Offset(position.x, position.y + halfSize)
    val rightCenter: Offset
        get() = Offset(position.x + sizePx, position.y + sizePx)
}

data class Edge(
    override val startNodeId: Int,
    override val endNodeId: Int,
) : GraphBasicEdge

data class DrawingEdge(
    override val start: Offset,
    override val end: Offset,
    override val isDirected: Boolean = false,
) : VisualEdge
