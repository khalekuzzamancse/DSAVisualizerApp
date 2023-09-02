package com.khalekuzzamanjustcse.dsavisulizer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Preview
@Composable
private fun Coor() {
    val box1Position = remember { mutableStateOf(Offset(0f, 0f)) }
    val box2Position = remember { mutableStateOf(Offset(0f, 0f)) }

    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .border(color = Color.Black, width = 2.dp)
                .onGloballyPositioned {
                    box1Position.value = it.positionInWindow()
                }
        ) {
            Element(box1Position)
        }

        Box(
            modifier = Modifier
                .size(100.dp)
                .border(color = Color.Black, width = 2.dp)
                .onGloballyPositioned {
                    box2Position.value = it.positionInWindow()
                }
        ) {
            Element(box2Position)
        }
    }
}

@Composable
private fun Element(boxPosition: MutableState<Offset>) {
    var offsetX by remember { mutableStateOf(0.dp) }
    var offsetY by remember { mutableStateOf(0.dp) }

    val isOverlapping = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(100.dp)
            .offset {
                IntOffset(offsetX.roundToPx(), offsetY.roundToPx())
            }
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    offsetX += dragAmount.x.dp
                    offsetY += dragAmount.y.dp

                    val boxRect = Rect(
                        left = boxPosition.value.x.toFloat(),
                        top = boxPosition.value.y.toFloat(),
                        right = boxPosition.value.x.toFloat() + 100.dp.toPx(),
                        bottom = boxPosition.value.y.toFloat() + 100.dp.toPx()
                    )

                    val elementRect = Rect(
                        left = offsetX.toPx(),
                        top = offsetY.toPx(),
                        right = offsetX.toPx() + 100.dp.toPx(),
                        bottom = offsetY.toPx() + 100.dp.toPx()
                    )

                    isOverlapping.value = boxRect.int(elementRect)
                }
            }
            .background(if (isOverlapping.value) Color.Red else Color(0xFF4DB6AC))
    ) {
        Text(
            text = "hello",
            style = TextStyle(color = Color.Black, fontSize = 16.sp),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

private fun Rect.int(other: Rect): Boolean {
    return !(left >= other.right || right <= other.left || top >= other.bottom || bottom <= other.top)
}
