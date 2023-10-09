package com.khalekuzzamanjustcse.graph_editor.edge

import android.util.Range
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.graph_editor.basic_concept_demo.EdgePoint
import kotlin.math.atan2


interface GraphEditorVisualEdge {
    val id: Int
    val start: Offset
    val end: Offset
    val control: Offset
    val cost: String?
    val arrowHeadPosition: Offset
    val isDirected: Boolean
    val slop: Float

    //path
    val path: Path
    val pathCenter: Offset
    val pathColor: Color

    //Selected Point
    val selectedPointColor: Color
    val showSelectedPoint: Boolean
    val anchorPointRadius: Dp
    val selectedPoint: EdgePoint
    fun updatePoint(amount: Offset): GraphEditorVisualEdge
    fun goEditMode(touchPosition: Offset): GraphEditorVisualEdge
}

data class GraphEditorVisualEdgeImp(
    override val id: Int,
    override val start: Offset,
    override val end: Offset,
    override val control: Offset,
    override val cost: String?,
    override val isDirected: Boolean = true,
    override val pathColor: Color = Color.Black,
    override val selectedPointColor: Color = Color.Red,
    override val showSelectedPoint: Boolean = false,
    override val anchorPointRadius: Dp = 4.dp,
    override val selectedPoint: EdgePoint = EdgePoint.None,
    val minTouchTargetPx: Float
) : GraphEditorVisualEdge {
    companion object {
        private val pathMeasurer = PathMeasure()
    }

    override val arrowHeadPosition: Offset
        get() {
            pathMeasurer.setPath(path, false)
            return if (pathMeasurer.length >= 20)
                pathMeasurer.getPosition(pathMeasurer.length - 20)
            else
                Offset.Unspecified
        }

    override val path: Path
        get() = Path().apply {
            moveTo(start.x, start.y)
            quadraticBezierTo(control.x, control.y, end.x, end.y)
        }
    override val slop: Float
        get() {
            val (x1, y1) = end
            val (x2, y2) = start
            val slop = atan2(y1 - y2, x1 - x2)
            return Math.toDegrees(slop.toDouble()).toFloat()
        }

    override val pathCenter: Offset
        get() {
            pathMeasurer.setPath(path, false)
            val pathLength = pathMeasurer.length
            return pathMeasurer.getPosition(pathLength / 2)//path center
        }


    override fun updatePoint(amount: Offset): GraphEditorVisualEdge {
        return when (selectedPoint) {
            EdgePoint.Start -> {
                var (x, y) = start + amount
                if (x < 0f)
                    x = 0f
                if (y < 0f)
                    y = 0f
                val newStart = Offset(x, y)
                this.copy(start = newStart, control = (newStart + end) / 2f)
            }

            EdgePoint.End -> {
                var (x, y) = end + amount
                if (x < 0f)
                    x = 0f
                if (y < 0f)
                    y = 0f
                val newEnd = Offset(x, y)
                this.copy(end = newEnd, control = (start + newEnd) / 2f)
            }

            EdgePoint.Control -> {
                var (x, y) = control + amount
                if (x < 0f)
                    x = 0f
                if (y < 0f)
                    y = 0f
                val newMid = Offset(x, y)
                this.copy(control = newMid)
            }

            else -> this

        }
    }

    override fun goEditMode(touchPosition: Offset): GraphEditorVisualEdge {
        return if (isStartTouched(touchPosition)) {
            this.copy(selectedPoint = EdgePoint.Start)
        } else if (isEndTouched(touchPosition)) {
            this.copy(selectedPoint = EdgePoint.End)
        } else if (isControlTouched(touchPosition)) {
            this.copy(selectedPoint = EdgePoint.Control)
        } else this.copy(selectedPoint = EdgePoint.None)
    }

    private fun isControlTouched(touchPosition: Offset) = isTargetTouched(touchPosition, pathCenter)

    private fun isStartTouched(touchPosition: Offset) = isTargetTouched(touchPosition, start)

    private fun isEndTouched(touchPosition: Offset) = isTargetTouched(touchPosition, end)

    private fun isTargetTouched(touchPosition: Offset, target: Offset): Boolean {
        return touchPosition.x in Range(
            target.x - minTouchTargetPx / 2,
            target.x + minTouchTargetPx / 2
        ) &&
                touchPosition.y in Range(
            target.y - minTouchTargetPx / 2,
            target.y + minTouchTargetPx / 2
        )
    }

}