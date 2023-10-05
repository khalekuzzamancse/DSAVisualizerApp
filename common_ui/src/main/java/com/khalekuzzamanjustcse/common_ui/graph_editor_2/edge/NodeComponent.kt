package com.khalekuzzamanjustcse.common_ui.graph_editor_2.edge

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import kotlin.random.Random

@Preview
@Composable
private fun Test() {

    val drawList = remember {
        mutableStateListOf<NodeProsperity>()
    }

    var touchIndex by remember {
        mutableIntStateOf(-1)
    }

    LaunchedEffect(Unit) {
        repeat(5) {
            val properties = NodeProsperity(
                center = Offset(
                    Random.nextInt(100, 1000).toFloat(),
                    Random.nextInt(100, 1500).toFloat()
                )
            )

            drawList.add(properties)
        }
    }

    Canvas(
        modifier = Modifier.fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        touchIndex = -1
                        drawList.forEachIndexed { index, drawProperties ->
                            val isTouched =
                                isTouched(drawProperties.center, offset, drawProperties.radius)

                            if (isTouched) {
                                touchIndex = index
                            }
                        }
                    },
                    onDrag = { change, dragAmount: Offset ->
                        val item = drawList.getOrNull(touchIndex)
                        item?.let { drawItem ->
                            drawList[touchIndex] = drawItem.copy(
                                center = drawItem.center.plus(dragAmount),
                                color = Color.Green
                            )
                        }
                    },
                    onDragEnd = {
                        val item = drawList.getOrNull(touchIndex)
                        item?.let { drawItem ->
                            drawList[touchIndex] = drawItem.copy(
                                color = Color.Red
                            )
                        }
                    }
                )
            }
    ) {
        drawList.forEachIndexed { index, drawProperties ->

            if (touchIndex != index) {
                drawCircle(
                    color = drawProperties.color,
                    center = drawProperties.center,
                    radius = drawProperties.radius
                )
            }
        }

        if (touchIndex > -1) {
            drawList.getOrNull(touchIndex)?.let { drawProperties ->
                drawCircle(
                    color = drawProperties.color,
                    center = drawProperties.center,
                    radius = drawProperties.radius
                )
            }
        }
    }

}

private fun isTouched(center: Offset, touchPosition: Offset, radius: Float): Boolean {
    return center.minus(touchPosition).getDistanceSquared() < radius * radius
}

interface CanvasNode{

}
data class NodeProsperity(
    val center: Offset,
    val radius: Float = 80f,
    val color: Color = Color.Red
)