package com.khalekuzzamanjustcse.dsavisulizer.visual_array

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class VisualArrayCellElement(
    val position: MutableState<Offset> = mutableStateOf(Offset.Zero),
    val value: Int,
    val color: MutableState<Color> = mutableStateOf(Color.Red),
    var currentCell: Int = OUT_OF_BOUND
) {

    companion object {
        const val OUT_OF_BOUND = -10
    }


}

data class VisualArrayCell(
    var position: Offset = Offset.Zero,
    val size: Dp = 64.dp,
    val backgroundColor: MutableState<Color> = mutableStateOf(Color.Unspecified),
) {

    //takes i so that multiple pointer does not have overlap with each other
    fun getPointerPosition(deviceDensity: Float): Offset {
        val cellSizePX = size.value * deviceDensity
        return position + Offset(cellSizePX / 2, cellSizePX)
    }
}

data class CellsAndElements(
    val cells: List<VisualArrayCell>,
    val elements: List<VisualArrayCellElement>
) {
    companion object {
        fun createInstance(list: List<Int>): CellsAndElements {
            val cells = list.map { _ -> VisualArrayCell() }
            val elements = list.map { VisualArrayCellElement(value = it) }
            return CellsAndElements(cells = cells, elements = elements)
        }
    }

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

    fun changeCellColor(index:Int, color: Color) {
            if (isValidIndex(index))
                cells[index].backgroundColor.value = color
    }

    fun updateElementPosition(index: Int, position: Offset) {
        if (isValidIndex(index))
            elements[index].position.value = position
    }

    fun getCellPosition(index: Int): Offset {
        if (isValidIndex(index))
            return cells[index].position
        return Offset.Infinite

    }

    fun getPointerPosition(cellNo: Int, deviceDensity: Float): Offset {
        return if (isValidIndex(cellNo))
            cells[cellNo].getPointerPosition(deviceDensity)
        else
            Offset.Infinite
    }

    fun updateElementCurrentCell(index: Int, cellNo: Int) {
        if (isValidIndex(index))
            elements[index].currentCell = cellNo
    }

}