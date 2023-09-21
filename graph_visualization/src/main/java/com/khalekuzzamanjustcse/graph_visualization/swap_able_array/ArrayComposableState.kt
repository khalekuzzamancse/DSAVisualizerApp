package com.khalekuzzamanjustcse.graph_visualization.swap_able_array

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset

data class ArrayElement<T>(
    val position: MutableState<Offset> = mutableStateOf(Offset.Zero),
    val value: T,
)
data class ArrayCell(
    val position: MutableState<Offset> = mutableStateOf(Offset.Zero),
)

data class ArrayComposableState<T>(
    val list: List<T>,
    private val cellSizePx: Float,
) {
    val cells = list.map { ArrayCell() }
    val elements = list.mapIndexed { _, value -> ArrayElement(value = value) }
    private val allCellPlaced: Boolean
        get() = cells.drop(1).all { it.position.value != Offset.Zero }

    //index represent the cellNo and value represent the index of element in elements list
    private val cellElementReference: MutableList<Int?> =
        List(list.size) { index -> index }.toMutableList()
    val cellsCurrentElements: List<T?>
        get() = cellElementReference.map { if (it == null) null else elements[it].value }

    fun onCellPositionChanged(index: Int, position: Offset) {
        cells[index].position.value = position
        if (allCellPlaced) {
            for (i in cells.indices) {
                elements[i].position.value = cells[i].position.value
            }

        }
    }

    fun onDragElement(index: Int, dragAmount: Offset) {
        elements[index].position.value += dragAmount
    }

    fun onDragEnd(elementIndex: Int) {
        val snapAt = SnapUtils(cells.map { it.position.value }, cellSizePx)
            .findNearestCellId(elements[elementIndex].position.value)
        elements[elementIndex].position.value = snapAt.second
        //added into this cells
        val cellNo = snapAt.first
        if (cellNo != -1) {
            cellElementReference[cellNo] = elementIndex
        }
    }

    fun onDragStart(indexOfElement: Int) {
        //find in which cells the element was removed
        val draggedFrom = cellElementReference.indexOf(indexOfElement)
        if (draggedFrom != -1) {
            cellElementReference[draggedFrom] = null
        }
    }


}
