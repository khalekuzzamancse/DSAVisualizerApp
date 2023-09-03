package com.khalekuzzamanjustcse.dsavisulizer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
class  CellManager{

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
    fun removeElement() = this.copy(currentElement = null)
    fun updateCurrentElement(element: Element) = this.copy(currentElement = element)
    fun updateTargetElement(element: Element) = this.copy(targetElement = element)

}

data class Element(
    val positionInRoot: Offset = Offset.Zero,
    val positionInParent: Offset = Offset.Zero,
    val value: Int
) {
    fun updatePositionInRoot(position: Offset) = this.copy(positionInRoot = position)
    fun updatePositionInParent(position: Offset) = this.copy(positionInParent = position)
}