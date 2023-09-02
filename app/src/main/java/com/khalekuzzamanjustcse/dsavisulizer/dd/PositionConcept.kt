package com.khalekuzzamanjustcse.dsavisulizer.dd

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun PPP() {
    var parentOriginRelativeToRoot by remember { mutableStateOf(emptyMap<Int, Offset>()) }
    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val cellWidth = 100.dp
        var currentPostion by remember { mutableStateOf(emptyMap<Int, Offset>()) }

        for (i in 1..2) {
            Box(modifier = Modifier
                .size(cellWidth)
                .border(color = Color.Black, width = 2.dp)
                .onGloballyPositioned {
                    val updatedMap = parentOriginRelativeToRoot.toMutableMap()
                    updatedMap[i] = it.positionInParent()
                    parentOriginRelativeToRoot = updatedMap
                }) {
                D(
                    modifier = Modifier
                        .size(cellWidth), offset = currentPostion[i] ?: Offset.Zero
                ) {
                    val updatedMap = currentPostion.toMutableMap()
                    updatedMap[i] = (it.positionInRoot() - parentOriginRelativeToRoot[i]!!)
                    currentPostion = updatedMap
                }

            }
        }

        Log.i("ParentPositionRelativeRoot", "$parentOriginRelativeToRoot")
    }

}


@Composable
private fun D(
    modifier: Modifier = Modifier,
    offset: Offset = Offset.Zero,
    size: Dp = 100.dp,
    onGlobalPositionChange: (LayoutCoordinates) -> Unit
) {
    var accumulatedDrag by remember { mutableStateOf(offset) }
    var globalPosition by remember { mutableStateOf<LayoutCoordinates?>(null) }

    Box(
        modifier = modifier
            .offset {
                IntOffset(
                    offset.x.toInt() + accumulatedDrag.x.toInt(),
                    offset.y.toInt() + accumulatedDrag.y.toInt()
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        globalPosition?.let { onGlobalPositionChange(it) }
                        accumulatedDrag = offset
                    }
                ) { change, dragAmount ->
                    accumulatedDrag = accumulatedDrag.plus(dragAmount)
                    change.consume()
                }
            }
            .background(color = Color.Blue)
            .onGloballyPositioned {
                globalPosition = it
            }
    )
}