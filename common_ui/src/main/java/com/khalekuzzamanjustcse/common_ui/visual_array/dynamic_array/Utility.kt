package com.khalekuzzamanjustcse.common_ui.visual_array.dynamic_array

import android.util.Range
import androidx.compose.ui.geometry.Offset
import kotlin.math.abs

data class BoundingRectangle(
    val topLeft: Offset,
    val bottomRight: Offset
) {
    private val xRange: Range<Float>
        get() = Range(topLeft.x, bottomRight.x)
    private val yRange: Range<Float>
        get() = Range(topLeft.y, bottomRight.y)
    val center: Offset
        get() = Offset((topLeft.x + bottomRight.x) / 2, (topLeft.y + bottomRight.y) / 2)


    fun isInside(offset: Offset) = offset.x in xRange && offset.y in yRange
    fun isOverlapping(other: BoundingRectangle): Boolean {
        return xRange.contains(other.topLeft.x) || other.xRange.contains(topLeft.x) &&
                yRange.contains(other.topLeft.y) || other.yRange.contains(topLeft.y)
    }


}

class SnapUtils(
    private val cellsPosition:List<Offset>,
    private val cellSizePx:Float
) {
    fun findNearestCellId(elementCurrentPosition: Offset): Pair<Int,Offset> {
        cellsPosition.forEachIndexed {index,it->
            val snap = shouldSnap(
                cellTopLeft = it,
                elementTopLeft = elementCurrentPosition,
            )
            if (snap)
                return Pair(index,cellsPosition[index])
        }
        return Pair(-1,elementCurrentPosition)
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