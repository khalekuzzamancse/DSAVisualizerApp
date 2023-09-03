package com.khalekuzzamanjustcse.dsavisulizer

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp

@Composable
 fun CellElement(
    modifier: Modifier = Modifier,
    offset: Offset = Offset.Zero,
    label: String,
    onDragStart: (LayoutCoordinates) -> Unit,
    onDragEnd: (LayoutCoordinates) -> Unit,
) {
    var currentPosition by remember { mutableStateOf(offset) }
    var globalCoordinate by remember { mutableStateOf<LayoutCoordinates?>(null) }


    Box(
        modifier = modifier
            .offset {
                IntOffset(
                    offset.x.toInt() + currentPosition.x.toInt(),
                    offset.y.toInt() + currentPosition.y.toInt()
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        globalCoordinate?.let {
                            onDragStart(it)
                        }
                    },
                    onDragEnd = {
                        globalCoordinate?.let { onDragEnd(it) }
                        currentPosition = offset

                    },
                    onDrag = { change, dragAmount ->
                        currentPosition = currentPosition.plus(dragAmount)
                        change.consume()
                    }
                )
            }
            .background(color = Color.Blue)
            .onGloballyPositioned {
                globalCoordinate = it
            }
    ) {
        Text(
            text = label,
            style = TextStyle(color = Color.White, fontSize = 16.sp),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}