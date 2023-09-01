package com.khalekuzzamanjustcse.auth.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Preview
@Composable
fun Pp() {
    val list = mutableListOf(1, 2, 3, 4, 5)
    val cells = remember { mutableStateListOf<Rect>() }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            list.forEachIndexed { index, it ->
                Box(
                    modifier = Modifier
                        .border(width = 2.dp, color = Color.Black, shape = RectangleShape)
                        .padding(4.dp)
                        .onGloballyPositioned { coordinates ->
                            if (cells.size <= index) {
                                cells.add(coordinates.boundsInWindow())
                            }
                        }
                ) {
                    Element(text = it.toString(), cells = cells)
                }
            }
        }
    }
}


@Composable
fun Element(text: String, cells: List<Rect>) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val size = 100.dp
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(size)
            .graphicsLayer {
                translationX = offsetX
                translationY = offsetY
                clip = true
                shape = CircleShape
            }
            .background(Color(0xFF4DB6AC))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                    val elementRect = Rect(
                        offset = Offset(offsetX, offsetY),
                        size = Size(size.roundToPx().toFloat(), size.roundToPx().toFloat())
                    )
                    val snappedCell =
                        cells.firstOrNull { it.intersect(elementRect) != null }
                    if (snappedCell != null) {
                        offsetX =
                            snappedCell.center.x - size.toPx() / 2
                        offsetY =
                            snappedCell.center.y - size.toPx() / 2
                    }
                }
            }
    ) {
        Text(
            text = text,
            style = TextStyle(color = Color.Black, fontSize = 46.sp),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
