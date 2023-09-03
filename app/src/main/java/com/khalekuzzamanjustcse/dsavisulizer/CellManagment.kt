package com.khalekuzzamanjustcse.dsavisulizer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp

data class CellManager(
    val cells: MutableMap<Int, Cell> = mutableMapOf(),
    val cellSize: Dp,
) {

    fun addCell(
        cellId: Int,
        positionInRoot: Offset = Offset.Zero,
        currentElement: Element? = null
    ): CellManager {
        val cell = Cell(
            positionInRoot = positionInRoot,
            cellSize = cellSize,
            currentElement = currentElement
        )
        val updatedMap = cells
        updatedMap[cellId] = cell
        return this.copy(cells = updatedMap)
    }

    fun getCellPositionInRoot(cellId: Int): Offset {
        return cells[cellId]?.positionInRoot ?: Offset.Infinite
    }

    fun getElementAtCell(cellId: Int): Element? = cells[cellId]?.currentElement

    fun updateCurrentElement(cellId: Int, element: Element): CellManager {
        val updatedMap = cells
        val oldCell = updatedMap[cellId]
        if (oldCell != null) {
            updatedMap[cellId] = oldCell.updateCurrentElement(element)
        }
        return this.copy(cells = updatedMap)
    }

    fun updateCellPositionInRoot(cellId: Int, position: Offset): CellManager {
        val updatedMap = cells
        val oldCell = updatedMap[cellId]
        if (oldCell != null) {
            updatedMap[cellId] = oldCell.updatePositionInRoot(position)
        }
        return this.copy(cells = updatedMap)
    }

    fun findCellIdByPositionInRoot(position: Offset): Int {
        cells.forEach { cell ->
            if (cell.value.positionInRoot == position)
                return cell.key
        }
        return -1
    }

    fun removeCurrentElement(cellId: Int): CellManager {
        val updatedMap = cells
        val oldCell = updatedMap[cellId]
        if (oldCell != null) {
            updatedMap[cellId] = oldCell.removCurrentElement()
        }
        return this.copy(cells = updatedMap)
    }

    fun isCellEmpty(cellId: Int) = cells[cellId]?.isEmpty() ?: true
    fun isNotCellEmpty(cellId: Int) = !isCellEmpty(cellId)


}

data class Cell(
    val positionInRoot: Offset = Offset.Zero,
    val cellSize: Dp,
    val currentElement: Element? = null,
    val targetElement: Element? = null,
) {
    fun isEmpty() = currentElement == null
    fun isNotEmpty() = !isEmpty()
    fun doesContainTargetElement() = (currentElement != null && targetElement == currentElement)
    fun removCurrentElement() = this.copy(currentElement = null)
    fun updateCurrentElement(element: Element) = this.copy(currentElement = element)
    fun updateTargetElement(element: Element) = this.copy(targetElement = element)
    fun updatePositionInRoot(position: Offset) = this.copy(positionInRoot = position)

}