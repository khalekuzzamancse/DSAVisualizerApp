package com.khalekuzzamanjustcse.dsavisulizer

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

@Preview
@Composable
private fun Coor() {
    val cellCoordinates = mutableMapOf<Int, Offset>()
    val elementCoordinates = mutableMapOf<Int, Offset>()

    var initialOffset by remember {
        mutableStateOf(Offset(50f, 50f))
    }


    Row(
        modifier = Modifier.fillMaxSize(
        )
    ) {
        val cellWidth = 100.dp
        val density = LocalDensity.current.density
        val cellSizePx = cellWidth.value * density
        Log.i("CellSize", "$cellSizePx")


        for (i in 1..1) {
            Box(modifier = Modifier
                .size(cellWidth)
                .border(color = Color.Black, width = 2.dp)
                .onGloballyPositioned {
                    cellCoordinates[i] = it.positionInWindow()
                }
            ) {
//                Element {
//                    elementCoordinates[i] = it
//                    Log.i("Coordinate:Cells", cellCoordinates.toString())
//                    Log.i("Coordinate:Elements", elementCoordinates.toString())
////                    Log.i("Coordinate:ElemenentCenter", cellCenterCoordinates.toString())
//
//                }
                Element2(initialOffset) {
                    initialOffset+=it
                    Log.i("OffsetElement2", "$it")
                }
            }
        }

    }
}


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
    return dx <= centerDistanceFromTopLeft && dy <= centerDistanceFromTopLeft
}

@Composable
private fun Element(
    onPositionChanged: (Offset) -> Unit
) {
    var offsetX by remember { mutableStateOf(0.dp) }
    var offsetY by remember { mutableStateOf(0.dp) }



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
                }
            }
            .background(Color(0xFF4DB6AC))
            .onGloballyPositioned {
                onPositionChanged(it.positionInWindow())
            }

    ) {
        Text(
            text = "hello",
            style = TextStyle(color = Color.Black, fontSize = 16.sp),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun Element2(
    initialOffset: Offset,
    onPositionChanged: (Offset) -> Unit
) {

    Box(
        modifier = Modifier
            .size(100.dp)
            .offset {
                IntOffset(initialOffset.x.toInt(), initialOffset.y.toInt())
            }
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    var offset=initialOffset
                    offset = Offset(offset.x + dragAmount.x, offset.y + dragAmount.y)
                    onPositionChanged(offset)
                }
            }
            .background(Color(0xFF4DB6AC))
            .onGloballyPositioned {
               // onPositionChanged(Offset(it.positionInWindow().x, it.positionInWindow().y))
            }
    ) {
        Text(
            text = "Hi",
            style = TextStyle(color = Color.Black, fontSize = 16.sp),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
