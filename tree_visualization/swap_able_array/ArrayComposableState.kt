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
    var elementRef: Int? = null
)

data class ArrayComposableState<T>(
  private  val list: List<T>,
    private val cellSizePx: Float,
) {
    val cells = List(list.size) { index -> ArrayCell(elementRef = index) }
    val elements = list.mapIndexed { _, value -> ArrayElement(value = value) }
    private val allCellPlaced: Boolean
        get() = cells.drop(1).all { it.position.value != Offset.Zero }

    val cellsCurrentElements: List<T?>
        get() = cells.map { if (it.elementRef == null) null else elements[it.elementRef!!].value }

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
            cells[cellNo].elementRef = elementIndex
        }
    }

    fun onDragStart(indexOfElement: Int) {
        //find in which cells the element was removed
        val draggedFrom = cells.map { it.elementRef }.indexOf(indexOfElement)
        if (draggedFrom != -1) {
            cells[draggedFrom].elementRef = null
        }
    }


}
