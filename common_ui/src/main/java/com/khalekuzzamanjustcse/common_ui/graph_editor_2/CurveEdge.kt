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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun CurveEdge() {
    val start = Offset(0f, 100f)
    val end = Offset(200f, 100f)
    val x1 = (start.x + end.x) / 2
    val y1 = (start.y + end.y) / 2
    var controlPoint by remember {
        mutableStateOf(Offset(x1, y1+30f))
    }
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .drawBehind {
                drawCurveLine(start, controlPoint, end)
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

private fun DrawScope.drawCurveLine(
    start: Offset,
    controlPoint: Offset,
    end: Offset,
) {

    val path = Path().apply {
        moveTo(start.x, start.y)
        quadraticBezierTo(controlPoint.x, controlPoint.y, end.x, end.y)
    }

   val m= PathMeasure().apply {
        setPath(path,false)
    }
    val arrowHead =m.getPosition(m.length-40)
    drawPath(
        path = path, color = Color.Black, style = Stroke(5f)
    )

    rotate(30f, end) {

        drawLine(
            color = Color.Black,
            start = arrowHead,
            end = end,
            strokeWidth = 4f
        )
    }
    rotate(-30f, end) {
        drawLine(
            color = Color.Black,
            start = arrowHead,
            end = end,
            strokeWidth = 4f
        )

    }


}

