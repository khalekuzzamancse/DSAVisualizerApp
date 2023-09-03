package com.khalekuzzamanjustcse.dsavisulizer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp

data class CellManager(
    val cells: MutableMap<Int, Cell> = mutableMapOf(),
    val cellSize: Dp,
) {

    fun addCell(
        id: Int,
        positionInRoot: Offset = Offset.Zero,
        currentElement: Element? = null
    ): CellManager {
        val cell = Cell(positionInRoot = positionInRoot, cellSize = cellSize)
        val updatedMap = cells
        updatedMap[id] = cell
        return this.copy(cells = updatedMap)
    }

    fun removeCell(id: Int): CellManager {
        val updatedMap = cells
        updatedMap.remove(id)
        return this.copy(cells = updatedMap)
    }

    fun getCellPositionInRoot(id: Int): Offset {
        return cells[id]?.positionInRoot ?: Offset.Infinite
    }

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

    fun updateTargetElement(cellId: Int, element: Element): CellManager {
        val updatedMap = cells
        val oldCell = updatedMap[cellId]
        if (oldCell != null) {
            updatedMap[cellId] = oldCell.updateTargetElement(element)
        }
        return this.copy(cells = updatedMap)
    }

    fun findCellByPositionInRoot(position: Offset): Int {
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
    fun isCellNotEmpty(cellId: Int) = cells[cellId]?.isNotEmpty() ?: false

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