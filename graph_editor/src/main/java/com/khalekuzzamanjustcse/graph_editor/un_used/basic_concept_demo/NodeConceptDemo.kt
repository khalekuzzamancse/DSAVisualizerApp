package com.khalekuzzamanjustcse.graph_editor.un_used.basic_concept_demo

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class DemoNode(
    val center: Offset,
    val color: Color = Color.Red,
    val density: Float,
    private val radius: Dp,
) {
    val radiusPx: Float
        get() = radius.value * density

    fun isInsideCircle(touchPosition: Offset): Boolean {
        return center.minus(touchPosition).getDistanceSquared() <= radiusPx * radiusPx
    }

    fun enableEdit(status: Boolean): DemoNode {
        return if(status)
            this.copy(color = Color.Blue)
        else
            this.copy(color = Color.Red)
    }

    fun updateCenter(amount: Offset): DemoNode {
        return this.copy(center = center + amount)
    }

}

@Preview
@Composable
private fun Test() {

    val density = LocalDensity.current.density
    val drawList = remember {
        mutableStateListOf(
            DemoNode(
                center = Offset(50f, 50f),
                density = density,
                radius = 25.dp
            )
        )
    }

    var touchIndex by remember {
        mutableIntStateOf(-1)
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    /*
                    Detecting a tap point inside the circle boundary or not
                    so that later we can use this concept to determine if a edge
                    terminal point inside a node boundary or not so that we can find
                    which edge is connected with which edge.
                     */
                    drawList.forEachIndexed { index, drawProperties ->
                        val isTouched =
                            isInsideCircle(drawProperties.center, it, drawProperties.radiusPx)
                        if (isTouched) {
                            touchIndex = index
                            Log.i("PointPosition : ", "InsideCircle")
                        }
                    }
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        touchIndex = -1
                        drawList.forEachIndexed { index, drawProperties ->
                            val isTouched =
                                isInsideCircle(
                                    drawProperties.center,
                                    offset,
                                    drawProperties.radiusPx
                                )

                            if (isTouched) {
                                touchIndex = index
                            }
                        }
                    },
                    onDrag = { _, dragAmount: Offset ->
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
                    radius = drawProperties.radiusPx
                )
            }
        }

        if (touchIndex > -1) {
            drawList.getOrNull(touchIndex)?.let { drawProperties ->
                drawCircle(
                    color = drawProperties.color,
                    center = drawProperties.center,
                    radius = drawProperties.radiusPx
                )
            }
        }
    }

}

private fun isInsideCircle(center: Offset, touchPosition: Offset, radius: Float): Boolean {
    return center.minus(touchPosition).getDistanceSquared() < radius * radius
}

