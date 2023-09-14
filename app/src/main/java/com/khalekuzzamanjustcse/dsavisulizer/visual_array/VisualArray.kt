package com.khalekuzzamanjustcse.dsavisulizer.visual_array

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.dsavisulizer.SwappableElement

data class MovePointer(
    val pointerName: PointerName,
    val arrayCellIndex: Int,
)
data class CellColor(
    val cellIndexNo:Int,
    val color:Color
)


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VisualArray(
    list: List<Int>,
    swap: Pair<Int, Int> = Pair(-1, -1),
    cellPointers: Pointers = Pointers.emptyPointers(),
    movePointer: List<MovePointer> = listOf(MovePointer(PointerName.NULL, -1)),
    changeCellColor: List<CellColor> = emptyList(),
) {

    val states by remember {
        mutableStateOf(CellsAndElements.createInstance(list))
    }
    val density = LocalDensity.current.density

    LaunchedEffect(states) {
        for (index in 0 until states.cells.size) {
            states.updateElementPosition(index, states.getCellPosition(index))
            states.updateElementCurrentCell(index, index)
        }
        Log.i(
            "CellStatus", "${
                states.cells.map {
                    it.getPointerPosition(density)
                }
            }"
        )
    }



    LaunchedEffect(swap) {
        states.swapCellElements(swap.first, swap.second)
    }

    LaunchedEffect(movePointer) {
        movePointer.forEach {
            cellPointers.updatePosition(
                name = it.pointerName,
                position = states.getPointerPosition(
                    cellNo = it.arrayCellIndex,
                    deviceDensity = density
                )
            )
        }
    }
    LaunchedEffect(changeCellColor) {
        changeCellColor.forEach { (index, color) ->
            if (index >= 0 && index < states.cells.size)
                states.changeCellColor(index, color)
        }

    }


    Box(modifier = Modifier.fillMaxSize()) {
        FlowRow {
            states.cells.forEachIndexed { _, cell ->
                ArrayCell(
                    cellSize = cell.size,
                    backgroundColor = cell.backgroundColor.value
                ) { pos ->
                    cell.position = pos
                }
            }
        }
        //positioned the element as 0,0 so that origin of them becomes 0,0
        states.elements.forEachIndexed { index, element ->
            SwappableElement(
                currentOffset = element.position.value,
                label = "${element.value}",
                size = states.cells[index].size
            )
        }//
        //positioned the element as 0,0 so that origin of them becomes 0,0
        cellPointers.pointerList.forEach {
            CellPointerComposable(
                currentPosition = it.position.value,
                label = it.label,
                icon = it.icon
            )
        }
    }

}

@Composable
private fun ArrayCell(
    modifier: Modifier = Modifier,
    cellSize: Dp,
    backgroundColor: Color,
    onPositionChanged: (Offset) -> Unit,
) {
    Box(
        modifier = modifier
            .size(cellSize)
            .border(width = 1.dp, color = Color.Black)
            .background(backgroundColor)
            .onGloballyPositioned { position ->
                onPositionChanged(position.positionInParent())
            }
    )

}