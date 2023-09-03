package com.khalekuzzamanjustcse.dsavisulizer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
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
private fun PPP() {

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {


        var parentPositionRelativeToRoot by remember { mutableStateOf(emptyMap<Int, Offset>()) }
        var currentPositionRelativeToParent by remember { mutableStateOf(emptyMap<Int, Offset>()) }
        var currentPositionRelativeToRoot by remember { mutableStateOf(emptyMap<Int, Offset>()) }

        //
        val cellWidth = 100.dp
        val density = LocalDensity.current.density

        //lambdas
        val updateCurrentPositionRelativeParent: (Int, Offset) -> Unit = { i, positionInRoot ->
            val updatedMap = currentPositionRelativeToParent.toMutableMap()
            updatedMap[i] = (positionInRoot - parentPositionRelativeToRoot[i]!!)
            currentPositionRelativeToParent = updatedMap
        }
        val updateCurrentPositionRelativeRoot: (Int, Offset) -> Unit = { i, positionInRoot ->
            val relativeToRoot = currentPositionRelativeToRoot.toMutableMap()
            relativeToRoot[i] = positionInRoot
            currentPositionRelativeToRoot = relativeToRoot
        }
        val updateCellsPositionRelativeToRoot: (Int, Offset) -> Unit = { i, positionInRoot ->
            val updatedMap = parentPositionRelativeToRoot.toMutableMap()
            updatedMap[i] = positionInRoot
            parentPositionRelativeToRoot = updatedMap
        }
        val onGlobalPositionChange: (Int, LayoutCoordinates) -> Unit = { i, it ->
            updateCurrentPositionRelativeParent(i, it.positionInRoot())
            updateCurrentPositionRelativeRoot(i, it.positionInRoot())
            val nearestCell: Offset? = SnapUtils(
                cellsPositionRelativeToRoot = parentPositionRelativeToRoot,
                currentPositionRelativeToRoot = currentPositionRelativeToRoot[i]
                    ?: Offset.Zero,
                density = density,
                cellWidth = cellWidth
            ).findNearestCell()
            if (nearestCell != null)
                updateCurrentPositionRelativeParent(i, nearestCell)
        }


        for (i in 1..3) {
            Box(modifier = Modifier
                .size(cellWidth)
                .border(color = Color.Black, width = 2.dp)
                .padding(8.dp)
                .onGloballyPositioned {
                    updateCellsPositionRelativeToRoot(i, it.positionInRoot())
                }) {
                Element(
                    modifier = Modifier
                        .size(cellWidth),
                    offset = currentPositionRelativeToParent[i] ?: Offset.Zero,
                    label = "$i"
                ) { globalPosition -> onGlobalPositionChange(i, globalPosition) }
            }
        }


    }

}


@Composable
private fun Element(
    modifier: Modifier = Modifier,
    offset: Offset = Offset.Zero,
    label: String,
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
    ) {
        Text(
            text = label,
            style = TextStyle(color = Color.White, fontSize = 16.sp),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

class SnapUtils(
    private val cellsPositionRelativeToRoot: Map<Int, Offset>,
    private val currentPositionRelativeToRoot: Offset,
    private val density: Float,
    private val cellWidth: Dp
) {
    fun findNearestCell(): Offset? {
        cellsPositionRelativeToRoot.forEach {
            val snap = shouldSnap(
                cellTopLeftRelativeToRoot = it.value,
                elementTopLeftRelativeToRoot = currentPositionRelativeToRoot,
            )
            if (snap)
                return it.value
        }
        return null
    }

    private fun shouldSnap(
        cellTopLeftRelativeToRoot: Offset,
        elementTopLeftRelativeToRoot: Offset,
    ): Boolean {
        val cellSizePx = cellWidth.value * density
        val centerDistanceFromTopLeft = cellSizePx / 2
        val dx = abs(cellTopLeftRelativeToRoot.x - elementTopLeftRelativeToRoot.x)
        val dy = abs(cellTopLeftRelativeToRoot.y - elementTopLeftRelativeToRoot.y)
        return dx <= centerDistanceFromTopLeft && dy <= centerDistanceFromTopLeft

    }
}
