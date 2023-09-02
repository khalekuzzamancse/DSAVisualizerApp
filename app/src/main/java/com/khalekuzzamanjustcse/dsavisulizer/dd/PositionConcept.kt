package com.khalekuzzamanjustcse.dsavisulizer.dd

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Preview
@Composable
private fun PPP() {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {


        var parentPositionRelativeToRoot by remember { mutableStateOf(emptyMap<Int, Offset>()) }
        var currentPositionRelativeToParent by remember { mutableStateOf(emptyMap<Int, Offset>()) }
        var currentPositionRelativeToRoot by remember { mutableStateOf(emptyMap<Int, Offset>()) }
        var cellPositionRelativeToRoot = parentPositionRelativeToRoot

        //
        //
        val cellWidth = 100.dp
        val density = LocalDensity.current.density
        val cellWidthPx = cellWidth.value * density


        for (i in 1..3) {
            Box(modifier = Modifier
                .size(cellWidth)
                .border(color = Color.Black, width = 2.dp)
                .onGloballyPositioned {
                    val updatedMap = parentPositionRelativeToRoot.toMutableMap()
                    updatedMap[i] = it.positionInParent()
                    parentPositionRelativeToRoot = updatedMap

                }) {
                D(
                    modifier = Modifier
                        .size(cellWidth), offset = currentPositionRelativeToParent[i] ?: Offset.Zero
                ) {
                    val updatedMap = currentPositionRelativeToParent.toMutableMap()
                    updatedMap[i] = (it.positionInRoot() - parentPositionRelativeToRoot[i]!!)
                    currentPositionRelativeToParent = updatedMap
                    //
                    val relativeToRoot = currentPositionRelativeToRoot.toMutableMap()
                    relativeToRoot[i] = it.positionInRoot()
                    currentPositionRelativeToRoot = relativeToRoot

                    //Snapping
                    val nearestCell=parentPositionRelativeToRoot[i]?: Offset.Zero
                   val snap= shouldSnap(
                        cellTopLeftRelativeToRoot =nearestCell ,
                        elementTopLeftRelativeToRoot = currentPositionRelativeToRoot[i]?: Offset.Zero,
                        density=density,
                        cellSize = cellWidth
                    )
                    if(snap){
                        Log.i("SnapYES$i","aj")
                        val updatedMap = currentPositionRelativeToParent.toMutableMap()
                        updatedMap[i] =Offset(0f,0f)
                        currentPositionRelativeToParent = updatedMap
                    }

                }

            }
        }
        Log.i(
            "currentPositionRelativeToRoot", "" +
                    "$currentPositionRelativeToRoot" +
                    "\n$cellPositionRelativeToRoot"
        )
    }

}


@Composable
private fun D(
    modifier: Modifier = Modifier,
    offset: Offset = Offset.Zero,
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

private fun shouldSnap(
    cellTopLeftRelativeToRoot: Offset,
    elementTopLeftRelativeToRoot: Offset,
    cellSize: Dp,
    density: Float
): Boolean {
    val cellSizePx = cellSize.value * density
    val centerDistanceFromTopLeft = cellSizePx / 2
    val dx = abs(cellTopLeftRelativeToRoot.x - elementTopLeftRelativeToRoot.x)
    val dy = abs(cellTopLeftRelativeToRoot.y - elementTopLeftRelativeToRoot.y)
    Log.i("MethodSnap()", "$cellTopLeftRelativeToRoot : $elementTopLeftRelativeToRoot : $dx,$dy")

    return dx <= centerDistanceFromTopLeft && dy <= centerDistanceFromTopLeft
}
