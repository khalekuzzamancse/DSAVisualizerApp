package com.khalekuzzamanjustcse.dsavisulizer

import androidx.compose.ui.geometry.Offset

data class ElementManager(
    val elements: MutableMap<Int, Element> = mutableMapOf(),
) {
    fun addElement(element: Element): ElementManager {
        val updatedMap = elements
        updatedMap[element.id] = element
        return this.copy(elements = updatedMap)
    }

    fun removeElement(elementId: Int): ElementManager {
        val updatedMap = elements
        updatedMap.entries.removeAll { it.value.id==elementId }
        return this.copy(elements = updatedMap)
    }



    fun updatePosition(elementId: Int, position: Offset): ElementManager {
        val updatedMap = elements
        val oldElement = updatedMap[elementId]
        if (oldElement != null) {
            updatedMap[elementId] = oldElement.updatePosition(position)
        }
        return this.copy(elements = updatedMap)
    }



    fun getElement(id: Int): Element? {
        return elements[id]
    }


}

data class Element(
    val position: Offset = Offset.Zero,
    val value: Int,
    val id: Int,
) {
    fun updatePosition(position: Offset) = this.copy(position = position)
}