package com.khalekuzzamanjustcse.dsavisulizer.dd

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

//  Log.i("GlobalPosition", "${it.positionInWindow()}: $currentPostion")
@Preview
@Composable
private fun PPP() {
    Row(modifier = Modifier.fillMaxSize()) {

        val cellPositionInWindow = mutableMapOf<Int, Offset>()
        val elementCurrentPositionInWindow = mutableMapOf<Int, Offset>()
        val elementExactPositionInParent = mutableMapOf<Int, Offset>()

        val n = 2
        val cellWidth = 100.dp
        val density = LocalDensity.current.density
        val cellWidthPx = cellWidth.value * density
        var currentPostion by remember { mutableStateOf(Offset(0f, 0f)) }
        Box(modifier = Modifier
            .size(cellWidth)
            .border(color = Color.Black, width = 2.dp)
            .onGloballyPositioned {
                cellPositionInWindow[1] = it.positionInWindow()
                elementExactPositionInParent[1] = it.positionInWindow()
            }) {
            Element(currentPostion) {
                currentPostion = it.positionInParent()
                elementCurrentPositionInWindow[1] = it.positionInWindow()
                // Log.i("GlobalPositionParent2:","$currentPostion ,:${it.positionInWindow()-it.positionInParent()}")
                val positionInParent =
                    it.positionInWindow() - (it.parentCoordinates?.positionInWindow()
                        ?: Offset.Zero)

//                Log.i("GlobalPositionParent2:","$currentPostion2 ,:${it.positionInWindow()-it.positionInParent()}")
                Log.i("GlobalPositionParent1:", "$currentPostion ,:${positionInParent}")
                Log.i(
                    "GlobalPosition",
                    "Current->$elementCurrentPositionInWindow" +
                            " \nCell->$cellPositionInWindow\n" +
                            "\nExact->$elementExactPositionInParent"

                )
            }
        }
        var currentPostion2 by remember { mutableStateOf(Offset(0f, 0f)) }
        Box(modifier = Modifier
            .size(cellWidth)
            .border(color = Color.Black, width = 2.dp)
            .onGloballyPositioned {
                cellPositionInWindow[2] = it.positionInWindow()
                elementExactPositionInParent[2] = it.positionInWindow()
            }) {
            Element(currentPostion2) {
                currentPostion2 = it.positionInParent()
                elementCurrentPositionInWindow[2] = it.positionInWindow()
                val positionInParent =
                    it.positionInWindow() - (it.parentCoordinates?.positionInWindow()
                        ?: Offset.Zero)

//                Log.i("GlobalPositionParent2:","$currentPostion2 ,:${it.positionInWindow()-it.positionInParent()}")
                Log.i("GlobalPositionParent2:", "$currentPostion2 ,:${positionInParent}")
                Log.i(
                    "GlobalPosition",
                    "Current->$elementCurrentPositionInWindow" +
                            " \nCell->$cellPositionInWindow\n" +
                            "\nExact->$elementExactPositionInParent"

                )
            }
        }

    }


}

@Composable
private fun Element(
    offset: Offset = Offset.Zero,
    size: Dp = 100.dp,
    onGlobalPositionChange: (LayoutCoordinates) -> Unit
) {
    var accumulatedDrag by remember { mutableStateOf(offset) }
    var globalPosition by remember { mutableStateOf<LayoutCoordinates?>(null) }

    Box(
        modifier = Modifier
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
            .size(size)
            .background(color = Color.Blue)
            .onGloballyPositioned {
                globalPosition = it
            }
    )
}