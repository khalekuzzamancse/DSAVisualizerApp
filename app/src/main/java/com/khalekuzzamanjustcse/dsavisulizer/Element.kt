package com.khalekuzzamanjustcse.dsavisulizer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp

data class ElementManager(
    val elements: MutableMap<Int, Element> = mutableMapOf(),
) {
    fun addElement(id: Int, element: Element): ElementManager {
        val updatedMap = elements
        updatedMap[id] = element
        return this.copy(elements = updatedMap)
    }

    fun updatePositionInParent(elementId: Int, position: Offset): ElementManager {
        val updatedMap = elements
        val oldElement = updatedMap[elementId]
        if (oldElement != null) {
            updatedMap[elementId] = oldElement.updatePositionInParent(position)
        }
        return this.copy(elements = updatedMap)
    }

    fun updatePositionInRoot(elementId: Int, position: Offset): ElementManager {
        val updatedMap = elements
        val oldElement = updatedMap[elementId]
        if (oldElement != null) {
            updatedMap[elementId] = oldElement.updatePositionInRoot(position)
        }
        return this.copy(elements = updatedMap)
    }
    fun updatePreviousPositionInRoot(elementId: Int, position: Offset): ElementManager {
        val updatedMap = elements
        val oldElement = updatedMap[elementId]
        if (oldElement != null) {
            updatedMap[elementId] = oldElement.updatePreviousPositionInRoot(position)
        }
        return this.copy(elements = updatedMap)
    }


    fun getElement(id: Int): Element? {
        return elements[id]
    }


}

data class Element(
    val positionInRoot: Offset = Offset.Zero,
    val previousPositionInRoot: Offset = Offset.Zero,
    val positionInParent: Offset = Offset.Zero,
    val value: Int
) {

    fun updatePositionInRoot(position: Offset) = this.copy(positionInRoot = position)
    fun updatePositionInParent(position: Offset) = this.copy(positionInParent = position)
    fun updatePreviousPositionInRoot(position: Offset) = this.copy(previousPositionInRoot = position)
}