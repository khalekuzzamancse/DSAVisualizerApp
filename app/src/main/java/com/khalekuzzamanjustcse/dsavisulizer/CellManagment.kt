package com.khalekuzzamanjustcse.dsavisulizer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp

data class CellManager(
    val cells: MutableMap<Int, Cell> = mutableMapOf(),
    val cellSize: Dp,
) {
    companion object{
        const val  NOT_A_CELL=-1;
    }

    fun addCell(
        cellId: Int,
        position: Offset,
        currentElement: Element? = null
    ): CellManager {
        val cell = Cell(
            position = position,
            cellSize = cellSize,
            currentElement = currentElement
        )
        val updatedMap = cells
        updatedMap[cellId] = cell
        return this.copy(cells = updatedMap)
    }

    fun getCellPosition(cellId: Int): Offset {
        return cells[cellId]?.position ?: Offset.Zero
    }

    fun getElementAt(cellId: Int): Element? = cells[cellId]?.currentElement

    fun updateCurrentElementOf(cellId: Int, element: Element): CellManager {
        val updatedMap = cells
        val oldCell = updatedMap[cellId]
        if (oldCell != null) {
            updatedMap[cellId] = oldCell.updateCurrentElement(element)
        }
        return this.copy(cells = updatedMap)
    }



    fun findCellIdByPosition(position: Offset): Int {
        cells.forEach { cell ->
            if (cell.value.position == position)
                return cell.key
        }
        return NOT_A_CELL
    }

    fun getValues() =
        cells.map { if (it.value.currentElement == null) null else it.value.currentElement!!.value }

    fun removeCurrentElement(cellId: Int): CellManager {
        val updatedMap = cells
        val oldCell = updatedMap[cellId]
        if (oldCell != null) {
            updatedMap[cellId] = oldCell.removeCurrentElement()
        }
        return this.copy(cells = updatedMap)
    }

    fun isCellEmpty(cellId: Int) = cells[cellId]?.isEmpty() ?: true
    fun isNotCellEmpty(cellId: Int) = !isCellEmpty(cellId)


}

data class Cell(
    val position: Offset = Offset.Zero,
    val cellSize: Dp,
    val currentElement: Element? = null,
) {
    fun isEmpty() = currentElement == null
    fun isNotEmpty() = !isEmpty()
    fun removeCurrentElement() = this.copy(currentElement = null)
    fun updateCurrentElement(element: Element) = this.copy(currentElement = element)
    fun updatePosition(position: Offset) = this.copy(position = position)

}