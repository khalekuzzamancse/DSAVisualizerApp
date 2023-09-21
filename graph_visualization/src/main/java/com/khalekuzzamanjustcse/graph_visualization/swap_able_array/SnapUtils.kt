package com.khalekuzzamanjustcse.graph_visualization.swap_able_array

import androidx.compose.ui.geometry.Offset
import kotlin.math.abs

class SnapUtils(
    private val cellsPosition:List<Offset>,
    private val cellSizePx:Float
) {
    fun findNearestCellId(elementCurrentPosition: Offset): Offset {
        cellsPosition.forEachIndexed {index,it->
            val snap = shouldSnap(
                cellTopLeft = it,
                elementTopLeft = elementCurrentPosition,
            )
            if (snap)
                return cellsPosition[index]
        }
        return elementCurrentPosition
    }
    private fun shouldSnap(
        cellTopLeft: Offset,
        elementTopLeft: Offset,
    ): Boolean {
        val centerDistanceFromTopLeft = cellSizePx / 2
        val dx = abs(cellTopLeft.x - elementTopLeft.x)
        val dy = abs(cellTopLeft.y - elementTopLeft.y)
        return dx <= centerDistanceFromTopLeft && dy <= centerDistanceFromTopLeft
    }
}
