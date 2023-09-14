package com.khalekuzzamanjustcse.dsavisulizer.old_unused

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import kotlin.math.abs

class SnapUtils(
    private val cellsPosition: Map<Int, Offset>,
    private val density: Float,
    private val cellWidth: Dp
) {
    companion object {
        const val NOT_A_CELL = -1
    }

    fun findNearestCellId(elementCurrentPosition: Offset): Int {
        cellsPosition.forEach {
            val snap = shouldSnap(
                cellTopLeftRelativeToRoot = it.value,
                elementTopLeftRelativeToRoot = elementCurrentPosition,
            )
            if (snap)
                return it.key
        }
        return NOT_A_CELL
    }

    private fun shouldSnap(
        cellTopLeftRelativeToRoot: Offset,
        elementTopLeftRelativeToRoot: Offset,
    ): Boolean {
        val cellSizePx = cellWidth.value * density
        val centerDistanceFromTopLeft = cellSizePx / 2
        val dx = abs(cellTopLeftRelativeToRoot.x - elementTopLeftRelativeToRoot.x)
        val dy = abs(cellTopLeftRelativeToRoot.y - elementTopLeftRelativeToRoot.y)
        return dx <= centerDistanceFromTopLeft && dy <= centerDistanceFromTopLeft

    }
}
