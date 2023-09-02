package com.khalekuzzamanjustcse.dsavisulizer

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import kotlin.math.abs

@Preview
@Composable
fun DraggableBoxList() {
    val n = 2
    val cellWidth = 100.dp
    val density = LocalDensity.current.density

    var initialOffsets by remember {
        mutableStateOf(
            (1..n).associateWith { Offset(0f, 0f) }
        )
    }
    val cellCoordinates = mutableMapOf<Int, Offset>()
    val elementCoordinate = mutableMapOf<Int, Offset>()


    val onOffsetChange: (Int, Offset) -> Unit = { i, newOffset ->
        val updatedOffsets = initialOffsets.toMutableMap()
        updatedOffsets[i] = newOffset
        initialOffsets = updatedOffsets
    }
    val onGlobalPositionChange: (Int, Offset) -> Unit = { i, newOffset ->
        elementCoordinate[i]=newOffset
        Log.i("GlobalPosition:Cell", "$cellCoordinates")
        Log.i("GlobalPosition:Element", "$elementCoordinate")
        val updatedOffsets = initialOffsets.toMutableMap()
      //  updatedOffsets[i] = newOffset
        initialOffsets = updatedOffsets



    }


    Row(modifier = Modifier.fillMaxSize()) {

        for (i in 1..n) {
            Box(modifier = Modifier
                .size(cellWidth)
                .border(color = Color.Black, width = 2.dp)
                .onGloballyPositioned {
                    cellCoordinates[i]=it.positionInWindow()
                }
            ){
                D(
                    offset = initialOffsets[i] ?: Offset.Zero,
                    onOffsetChange = { onOffsetChange(i, it) },
                    onGlobalPositionChange = { onGlobalPositionChange(i, it) }
                )
            }

        }
    }
}

@Composable
private fun D(
    offset: Offset,
    size: Dp = 100.dp,
    onOffsetChange: (Offset) -> Unit,
    onGlobalPositionChange: (Offset) -> Unit
) {
    var accumulatedDrag by remember { mutableStateOf(Offset(0f, 0f)) }

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
            .size(size)
            .background(color = Color.Blue)
            .onGloballyPositioned {
                onGlobalPositionChange(it.positionInWindow())
            }
    )
}

//        val snap= shouldSnap(
//            cellTopLeft =cellCoordinates[i]?: Offset.Zero,
//            elementTopLeft=initialOffsets[i]?: Offset.Zero,
//            cellSize=cellWidth,
//            density=density
//        )
private fun shouldSnap(
    cellTopLeft: Offset,
    elementTopLeft: Offset,
    cellSize: Dp,
    density: Float
): Boolean {
    val cellSizePx = cellSize.value * density
    val centerDistanceFromTopLeft = cellSizePx / 2
    val dx = abs(cellTopLeft.x - elementTopLeft.x)
    val dy = abs(cellTopLeft.y - elementTopLeft.y)
    Log.i("MethodSnap()","$cellTopLeft : $elementTopLeft : $dx,$dy")

    return dx <= centerDistanceFromTopLeft && dy <= centerDistanceFromTopLeft
}