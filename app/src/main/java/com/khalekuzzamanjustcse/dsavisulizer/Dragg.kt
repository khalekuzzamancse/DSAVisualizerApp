package com.khalekuzzamanjustcse.dsavisulizer

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Preview
@Preview
@Composable
fun DraggableBox() {
    var offset by remember { mutableStateOf(Offset(50f, 100f)) }
    Column(modifier = Modifier.fillMaxSize()) {
        D(offset = offset) { newOffset ->
            offset = newOffset
        }
    }
}

@Composable
private fun D(offset: Offset, onOffsetChange: (Offset) -> Unit) {
    var accumulatedDrag = Offset(0f, 0f)

    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    offset.x.toInt() + accumulatedDrag.x.toInt(),
                    offset.y.toInt() + accumulatedDrag.y.toInt()
                )
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    accumulatedDrag = accumulatedDrag.plus(dragAmount)
                    val newOffset = offset + accumulatedDrag
                    onOffsetChange(newOffset)
                    change.consume()
                }
            }
            .size(100.dp, 100.dp)
            .background(color = Color.Blue)
    )
}

