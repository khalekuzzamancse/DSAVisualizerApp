package com.khalekuzzamanjustcse.graph_editor.node

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

data class Node(
    val id: Int,
    val center: Offset,
    val color: Color = Color.Blue,
    val density: Float,
    private val radius: Dp,
    private val dragEnabled: Boolean = false
) {

    val radiusPx: Float
        get() = radius.value * density


    private fun isInsideCircle(touchPosition: Offset): Boolean {
        return center.minus(touchPosition).getDistanceSquared() <= radiusPx * radiusPx
    }

    fun enableEdit(touchPosition: Offset): Node {
        return if (isInsideCircle(touchPosition))
            this.copy(dragEnabled = true)
        else this
    }

    fun updateCenter(amount: Offset): Node {
        return if (dragEnabled) {
            this.copy(center = center + amount)
        } else this
    }

}
