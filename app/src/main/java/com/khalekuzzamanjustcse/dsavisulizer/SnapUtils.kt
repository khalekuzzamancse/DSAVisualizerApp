package com.khalekuzzamanjustcse.dsavisulizer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import kotlin.math.abs

class SnapUtils(
    private val cellsPositionRelativeToRoot: Map<Int, Offset>,
    private val currentPositionRelativeToRoot: Offset,
    private val density: Float,
    private val cellWidth: Dp
) {
    fun findNearestCell(): Offset? {
        cellsPositionRelativeToRoot.forEach {
            val snap = shouldSnap(
                cellTopLeftRelativeToRoot = it.value,
                elementTopLeftRelativeToRoot = currentPositionRelativeToRoot,
            )
            if (snap)
                return it.value
        }
        return null
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
