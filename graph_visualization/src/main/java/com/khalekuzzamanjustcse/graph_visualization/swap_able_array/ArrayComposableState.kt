package com.khalekuzzamanjustcse.graph_visualization.swap_able_array

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset

data class ArrayComposableState<T>(
    val list: List<T>,
    val cellSizePx: Float,
    var autoSnap:Boolean=true
) {
    private val cellsPosition = Array(list.size) { mutableStateOf(Offset.Zero) }
    val elementsPosition = Array(list.size) { mutableStateOf(Offset.Zero) }
    private val allCellPlaced: Boolean
        get() = cellsPosition.sliceArray(1 until cellsPosition.size).all { it.value != Offset.Zero }
    fun onCellPositionChanged(index: Int, position: Offset) {
        cellsPosition[index].value = position
        if (allCellPlaced) {
            for (i in cellsPosition.indices)
                elementsPosition[i].value = cellsPosition[i].value
        }
    }
    fun onDragElement(index: Int, dragAmount: Offset) {
        elementsPosition[index].value += dragAmount
    }
    fun onDragEnd(elementIndex: Int) {
        if (autoSnap) {
            val snapAt = SnapUtils(cellsPosition.map { it.value }, cellSizePx)
                .findNearestCellId(elementsPosition[elementIndex].value)
            elementsPosition[elementIndex].value = snapAt
        }
    }
    override fun toString(): String {
        return "${cellsPosition.map { it.value }}\n${elementsPosition.map { it.value }}"
    }
}
