package com.khalekuzzamanjustcse.dsavisulizer.visual_array

import android.util.Range
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.dsavisulizer.SwappableElement


@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun VisualArrayPreview() {
    val list = listOf(1, 2, 3, 4)
    var swap by remember {
        mutableStateOf(Pair(-1, -1))
    }
    var i by remember {
        mutableStateOf(0)
    }
    var color by remember {
        mutableStateOf(Pair(Range(-1, -1), Color.Blue))
    }


    val pointers by remember {
        mutableStateOf(
            Pointers(
                listOf(
                    CellPointer(
                        position = mutableStateOf(Offset(0f, 200f)),
                        label = "i",
                        icon = Icons.Default.ArrowForward,
                        name = SelectionSortPointerName.I
                    ),
                    CellPointer(
                        position = mutableStateOf(Offset(200f, 200f)),
                        label = "j",
                        icon = Icons.Default.PlayArrow,
                        name = SelectionSortPointerName.J
                    ),
                )
            )
        )
    }
    var movePointer by remember {
        mutableStateOf(MovePointer(PointerName.NULL, -1))
    }

    Column(
        modifier =
        Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        FlowRow {
            Button(onClick = {
                swap = Pair(i, i + 1)
                i++
                if (i == list.size - 1)
                    i = 0
            }) {
                Text("Swap")
            }
            Button(onClick = {
                color = Pair(Range(2, 3), Color.Blue)
            }) {
                Text("changeColor")
            }
            Button(onClick = {
                movePointer = MovePointer(SelectionSortPointerName.I, 3)
            }) {
                Text("pointer i")
            }
            Button(onClick = {
                movePointer = MovePointer(SelectionSortPointerName.J, 2)
            }) {
                Text("pointer j")
            }

        }
        VisualArray(
            list = list,
            swap = swap,
            changeColor = color,
            cellPointers = pointers,
            movePointer = movePointer
        )
    }

}

data class MovePointer(
    val pointerName: PointerName,
    val arrayCellIndex: Int,
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VisualArray(
    list: List<Int>,
    swap: Pair<Int, Int> = Pair(-1, -1),
    changeColor: Pair<Range<Int>, Color> = Pair(Range(-1, -1), Color.Unspecified),
    cellPointers: Pointers = Pointers.emptyPointers(),
    movePointer: MovePointer = MovePointer(PointerName.NULL, -1)
) {

    val states by remember {
        mutableStateOf(CellsAndElements.createInstance(list))
    }
    val density = LocalDensity.current.density

    LaunchedEffect(states) {
        for (index in 0 until states.cells.size) {
            states.elements[index].position.value = states.cells[index].position
            states.elements[index].currentCell = index
            ///
        }
    }
    LaunchedEffect(swap) {
        states.swapCellElements(swap.first, swap.second)
    }
    LaunchedEffect(changeColor) {
        states.changeCellColor(changeColor.first.lower, changeColor.first.upper, changeColor.second)
    }
    LaunchedEffect(movePointer) {
        val shouldMove =
            movePointer.arrayCellIndex >= 0 &&
                    movePointer.arrayCellIndex < states.cells.size
        if (shouldMove) {
            cellPointers.updatePosition(
                name = movePointer.pointerName,
                position = states.getIthPointerPosition(
                    cellNo = movePointer.arrayCellIndex,
                    deviceDensity = density
                )
            )
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