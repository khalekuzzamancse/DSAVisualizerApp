package com.khalekuzzamanjustcse.dsavisulizer.visual_array

import android.util.Log
import android.util.Range
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khalekuzzamanjustcse.dsavisulizer.SwappableElement


data class VisualArrayCellState(
    var position: Offset = Offset.Zero,
    val size: Dp = 64.dp,
    val backgroundColor: MutableState<Color> = mutableStateOf(Color.Unspecified),
)

data class VisualArrayCellElement(
    val position: MutableState<Offset> = mutableStateOf(Offset.Zero),
    val value: Int,
    val color: MutableState<Color> = mutableStateOf(Color.Red),
    var currentCell: Int = OUT_OF_BOUND
) {
    companion object {
        const val OUT_OF_BOUND = -10
    }

    fun isInsideThisCell(cell: Int): Boolean = cell == currentCell
    fun updatePosition(cellNo: Int, position: Offset) {
        this.position.value = position
        currentCell = cellNo
    }
}

data class VisualArrayCell(
    var position: Offset = Offset.Zero,
    val size: Dp = 64.dp,
    val backgroundColor: MutableState<Color> = mutableStateOf(Color.Unspecified),
)

data class CellsAndElements(
    val cells: List<VisualArrayCell>,
    val elements: List<VisualArrayCellElement>
) {
    private fun isValidIndex(index: Int) = index >= 0 && index < cells.size

    fun swapCellElements(i: Int, j: Int) {
        if (isValidIndex(i) && isValidIndex(j)) {
            val a: VisualArrayCellElement? = elements.find { it.currentCell == i }
            val b: VisualArrayCellElement? = elements.find { it.currentCell == j }
            if (a != null && b != null) {
                val temp = a.position.value
                a.position.value = b.position.value
                b.position.value = temp
                a.currentCell = j
                b.currentCell = i
            }
        }

    }

    fun changeCellColor(cellNo: Int, color: Color) {
        if (isValidIndex(cellNo))
            cells[cellNo].backgroundColor.value = color
    }

    fun changeCellColor(start: Int, end: Int, color: Color) {
        for (i in start..end)
            if (isValidIndex(i))
                cells[i].backgroundColor.value = color
    }
}

fun createVisualArrayCellsAndElements(list: List<Int>): CellsAndElements {
    val cells = list.map { _ -> VisualArrayCell() }
    val elements = list.map { VisualArrayCellElement(value = it) }
    return CellsAndElements(cells = cells, elements = elements)
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun SwapTestPreview() {

    val list = createVisualArrayCellsAndElements(listOf(1, 2, 3, 4))
    var swap by remember {
        mutableStateOf(Pair(-1, -1))
    }
    var i by remember {
        mutableStateOf(0)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        FlowRow {
            Button(onClick = {
                swap = Pair(i, i + 1)
                i++;
                if (i == list.cells.size - 1)
                    i = 0
            }) {
                Text("Swap")
            }
            Button(onClick = {

            }) {
                Text("changeColor")
            }
        }
        SwapTest(
            list = list,
            swap = swap,
            changeColor = Pair(Range(i,i+1), Color.Blue)
        )
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SwapTest(
    list: CellsAndElements,
    swap: Pair<Int, Int> = Pair(-1, -1),
    changeColor: Pair<Range<Int>, Color> = Pair(Range(-1, -1), Color.Unspecified)
) {

    val states by remember {
        mutableStateOf(list)
    }

    LaunchedEffect(states) {
        for (index in 0 until states.cells.size) {
            states.elements[index].position.value = states.cells[index].position
            states.elements[index].currentCell = index
        }
    }
    LaunchedEffect(swap) {
        states.swapCellElements(swap.first, swap.second)
    }
    LaunchedEffect(changeColor) {
        states.changeCellColor(changeColor.first.lower, changeColor.first.upper, changeColor.second)
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
        states.elements.forEachIndexed { index, element ->
            SwappableElement(
                currentOffset = element.position.value,
                label = "${element.value}",
                size = states.cells[index].size
            )
        }
    }

}


@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun VisualArrayPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        var swap by remember {
            mutableStateOf(Pair(-1, -1))
        }
        var i by remember {
            mutableStateOf(0)
        }
        var j by remember {
            mutableStateOf(1)
        }
        VisualArray(
            array = listOf(1, 2, 3, 4),
            swapElements = swap
        )
        FlowRow {
            Button(onClick = {
                swap = Pair(0, 1)
            }) {
                Text("Swap(0,1)")
            }
            Button(onClick = {
                swap = Pair(1, 2)
            }) {
                Text("Swap(1,2)")
            }
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VisualArray(
    array: List<Int>,
    cellSize: Dp = 64.dp,
    swapElements: Pair<Int, Int> = Pair(-1, -1),
    changeCellColor: Pair<Range<Int>, Color> = Pair(Range(-1, -1), Color.Unspecified),
) {
    val isValidIndex: (Int) -> Boolean = {
        it >= 0 && it < array.size
    }

    if (array.isEmpty())
        throw IllegalArgumentException("Array must not be empty")

    val cellStates by remember {
        mutableStateOf(List(array.size) { index ->
            index to VisualArrayCellState()
        }.toMap())
    }

    var elementPosition by remember {
        mutableStateOf(List(array.size) { index -> index to Offset.Zero }.toMap())
    }
    val swapElementPosition: (Int, Int) -> Unit = { i, j ->
        val newPositions = elementPosition.toMutableMap()
        val temp = newPositions[i] ?: Offset.Zero
        newPositions[i] = newPositions[j] ?: Offset.Zero
        newPositions[j] = temp
        elementPosition = newPositions
    }
    LaunchedEffect(cellStates) {
        elementPosition = cellStates.mapValues { it.value.position }
    }

//
//
////    //changing cell color
//    if(isValidIndex(changeCellColor.first.lower)&&isValidIndex(changeCellColor.first.upper)){
//        cellStateList.
//    }

    //Swapping Value
    LaunchedEffect(swapElements) {
        if (isValidIndex(swapElements.first) && isValidIndex(swapElements.second)) {
            swapElementPosition(swapElements.first, swapElements.second)
        }

    }
    Box(modifier = Modifier.fillMaxWidth()) {
        FlowRow(
            modifier = Modifier.border(width = 2.dp, color = Color.Black)
        ) {
            for (i in array.indices) {
                ArrayCell(
                    cellSize = cellSize,
                    backgroundColor = if (cellStates[i] == null) Color.Unspecified else cellStates[i]!!.backgroundColor.value,
                )
                {
                    cellStates[i]?.position = it
                }
            }
        }
//        //Placing all element at (0,0) so that moving then become  easy
        for (i in array.indices) {
            SwappableElement(
                currentOffset = elementPosition[i] ?: Offset.Zero,
                label = "${array[i]}",
                size = cellSize
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