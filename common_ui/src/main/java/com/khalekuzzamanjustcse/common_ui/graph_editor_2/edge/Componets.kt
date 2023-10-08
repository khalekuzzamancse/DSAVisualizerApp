package com.khalekuzzamanjustcse.common_ui.graph_editor_2.edge

import android.util.Range
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import kotlin.math.atan2


interface GraphEditorVisualEdge {
    val id: Int
    val start: Offset
    val end: Offset
    val controlPoint: Offset
    val edgeCost: String?
    val showAnchor: Boolean
    val path: Path
    val pathCenter: Offset
    val anchorPointRadius: Float
    val arrowHeadPosition: Offset
    val isDirected: Boolean
    val slopDegree: Float
    val minTouchTargetPx: Float
    val pathColor:Color
    fun onAnchorPointDrag(dragAmount: Offset): GraphEditorVisualEdge
    fun isAnchorTouched(touchPosition: Offset): Boolean
    fun move(dragAmount: Offset): GraphEditorVisualEdge
    fun changeSize(dragAmount: Offset): GraphEditorVisualEdge
    fun onReadyToOperation():GraphEditorVisualEdge
    fun onMoveOperationEnd(): GraphEditorVisualEdge


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
    override val minTouchTargetPx: Float,
    override val pathColor: Color =Color.Black
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
            return pathMeasurer.getPosition(pathLength / 2)//path center
        }
    override val arrowHeadPosition: Offset
        get() {
            pathMeasurer.setPath(path, false)
            return pathMeasurer.getPosition(pathMeasurer.length - 20)
        }
    override val slopDegree: Float
        get() {
            val (x1, y1) = end
            val (x2, y2) = start
            val slop = atan2(y1 - y2, x1 - x2)
            return Math.toDegrees(slop.toDouble()).toFloat()
        }



    override fun onAnchorPointDrag(dragAmount: Offset) =
        this.copy(controlPoint = controlPoint + dragAmount, pathColor = Color.Red)

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

    override fun move(dragAmount: Offset): GraphEditorVisualEdge {
        return this.copy(
            start = start + dragAmount,
            controlPoint = controlPoint + dragAmount,
            end = end + dragAmount,
            pathColor = Color.Red
        )
        /*
        This is creating new path for each drag,since at a time user allow to drag one edge
        so this is not so expensive,but if in future the app perform slow because of movement
        then,try to use the translate approach,in the dragging time translate the path into somewhere
        then at the end of the translate recreate the path again by adding the translation offset to each
        start,control and the end point.
         */
    }

    override fun changeSize(dragAmount: Offset): GraphEditorVisualEdge {
        return this.copy(end = end + dragAmount, pathColor = Color.Red)
    }

    override fun onReadyToOperation(): GraphEditorVisualEdge {
        return  this.copy(pathColor= Color.Blue)
    }

    override fun onMoveOperationEnd(): GraphEditorVisualEdge {
        return  this.copy(pathColor= Color.Black)
    }
}
