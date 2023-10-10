package com.khalekuzzamanjustcse.graph_editor.un_used.basic_concept_demo

import android.util.Range
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.atan2

enum class EdgePoint {
    Start, End, Control, None
}

data class DemoEdge(
    val start: Offset,
    val end: Offset,
    val control: Offset = (start + end) / 2f,
    val color: Color = Color.Black,
    val minTouchTargetPx: Float,
    val cost: String? = null,
    val isDirected: Boolean = true,
    //Selected Point
    val selectedPointColor: Color = Color.Blue,
    val showSelectedPoint: Boolean = true,
    val anchorPointRadius: Dp = 4.dp,
    val selectedPoint: EdgePoint = EdgePoint.Control,
    //
    val id: Int = 1,
) {
    companion object {
        private val pathMeasurer = PathMeasure()
    }

    val arrowHeadPosition: Offset
        get() {
            pathMeasurer.setPath(path, false)
            return pathMeasurer.getPosition(pathMeasurer.length - 20)
        }

    val path: Path
        get() = Path().apply {
            moveTo(start.x, start.y)
            quadraticBezierTo(control.x, control.y, end.x, end.y)
        }
    val slopDegree: Float
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
            return pathMeasurer.getPosition(pathLength / 2)//path center
        }

    fun updatePoint(amount: Offset): DemoEdge {
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
                this.copy(control =newMid)
            }

            else -> this

        }
    }

    fun goEditMode(touchPosition: Offset): DemoEdge {
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


@Preview
@Composable
private fun EdgeComposable() {
    val minTouchTargetPx = 30.dp.value * LocalDensity.current.density
    var demoEdge by remember {
        mutableStateOf(
            DemoEdge(
                start = Offset(100f, 100f),
                end = Offset(250f, 100f),
                minTouchTargetPx = minTouchTargetPx,
                cost = "5 Tk"
            )
        )
    }
    val textMeasurer = rememberTextMeasurer()
    var selectedPoint by remember {
        mutableStateOf<EdgePoint?>(null)
    }
    Canvas(
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { touchedPosition ->
                        demoEdge = demoEdge.goEditMode(touchedPosition)
                    })
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { _, dragAmount ->
                        demoEdge = demoEdge.updatePoint(dragAmount)
                    },
                    onDragEnd = {
                        selectedPoint = null
                    }
                )
            }
    ) {
        drawDemoEdge(
            demoEdge,
            textMeasurer
        )
        //draw sample nodes
        drawCircle(
            color = Color.Red,
            radius = 20.dp.toPx(),
            center = Offset(150f, 200f)
        )
        drawCircle(
            color = Color.Red,
            radius = 20.dp.toPx(),
            center = Offset(400f, 300f)
        )

    }


}

 fun DrawScope.drawDemoEdge(
     demoEdge: DemoEdge,
     textMeasurer: TextMeasurer? = null,
) {
    val (start, end, _, color) = demoEdge
    val path = demoEdge.path
    val pathMeasurer = PathMeasure()
    pathMeasurer.setPath(path, false)
    val pathCenter = demoEdge.pathCenter
    val slope = demoEdge.slopDegree
    val anchorPointRadius = demoEdge.anchorPointRadius.toPx()
    val arrowHeadPosition = demoEdge.arrowHeadPosition
    val selectedPoint = demoEdge.selectedPoint
    val selectedPointColor = demoEdge.selectedPointColor

    //drawEdge
    drawPath(path = path, color = color, style = Stroke(3f))
    //draw anchor point
    when (selectedPoint) {
        EdgePoint.Start -> {
            drawCircle(color = selectedPointColor, radius = anchorPointRadius, center = start)
        }

        EdgePoint.End -> {
            drawCircle(color = selectedPointColor, radius = anchorPointRadius, center = end)
        }

        EdgePoint.Control -> {
            drawCircle(color = selectedPointColor, radius = anchorPointRadius, center = pathCenter)
        }

        else -> {}
    }

    //draw edge cost
    if (textMeasurer != null) {
        demoEdge.cost?.let { text ->
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
    //draw Arrow head
    rotate(30f, end) {
        drawLine(
            color = color,
            start = arrowHeadPosition,
            end = end,
            strokeWidth = 3f
        )
    }
    rotate(-30f, end) {
        drawLine(
            color = color,
            start = arrowHeadPosition,
            end = end,
            strokeWidth = 3f
        )
    }
}


