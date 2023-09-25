package com.khalekuzzamanjustcse.graph_visualization.graph_draw

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview
import com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array.MyButton
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Immutable
data class EdgeComposableState(
    val startPoint: Offset,
    val endPoint: Offset,
    val color: Color = Color.Black,
    val thickness: Float = 4f,
    val hasCost: Boolean = false,
    val cost: String = "",
    val hasDirection: Boolean = false,
    val directionStartToEnd: Boolean = false,
)
/*

 */


@Preview
@Composable
fun EdgeComposablePreview() {
    val modifier = Modifier
    var state by remember {
        mutableStateOf(
            EdgeComposableState(
                Offset(20f, 100f), Offset(200f, 150f)
            )
        )
    }

    Column(
        modifier
            .fillMaxSize()
            .drawBehind {
                drawEdge(state)

            }
    ) {
        Row {
            MyButton(label = "->") {
                state = state.copy(hasDirection = true)
            }
            MyButton(label = "<-") {
                state = state.copy(hasDirection = true,directionStartToEnd = true)
            }
        }
    }
}

fun DrawScope.drawEdge(
    state: EdgeComposableState,
) {
    drawLine(
        color = state.color, start = state.startPoint,
        end = state.endPoint, strokeWidth = state.thickness,
        cap = StrokeCap.Round,
    )
    if (state.hasDirection) {
        drawPath(
            path = createArrowPath(state.startPoint, state.endPoint, state.directionStartToEnd),
            color = state.color,
            style = Fill
        )
    }


}

fun createArrowPath(
    startPoint: Offset, endPoint: Offset,
    directionFromStartToEnd: Boolean = false
): Path {
    var start: Offset = startPoint
    var end: Offset = endPoint
    if (directionFromStartToEnd) {
        start = endPoint
        end = startPoint
    }
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

