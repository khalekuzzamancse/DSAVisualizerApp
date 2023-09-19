package com.khalekuzzamanjustcse.tree_visualization.laying_node

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color


/*
Note:
We are using Shanon's Layout Algorithm to place the tree
but this algorithm has problems when any of the children is missing,
so to avoid this we initially initialize the children with empty children
then when adding children we replace empty children,
when show in the ui or traversal we will skip if any children is empty
the drawback of it it it will take extra space because it contain unnessary null children

 */
data class Node(
    val value: Int,
    val sizePx: Float,
    var x: Float = 0f,
    var y: Float = 0f,
    val children: MutableList<Node> = mutableListOf(),
    val coordinates: MutableState<Offset> = mutableStateOf(Offset.Zero),
    val color: MutableState<Color> = mutableStateOf(Color.Red)
) {
    companion object {
        const val NULL_NODE = -1

    }
    // val emptyNode=Node(value = NULL_NODE, sizePx = sizePx)

    fun updateCoordinate() {
        coordinates.value = Offset(x * sizePx, y * sizePx)
    }

    fun getCenterOffset() = coordinates.value + Offset(sizePx / 2, sizePx / 2)
    fun resetColor() {
        color.value = Color.Red
    }

    override fun toString(): String {
        return "$value:(${children.map { it.value }} )"

    }

    fun initializeEmptyChildren() {
        if (children.size == 0) {
            children.add(emptyNode())
            children.add(emptyNode())
        } else if (children.size == 1) {
            children.add(emptyNode())
        }
    }
    fun createRoot():Node{
        val node =Node(value,sizePx)
        node.initializeEmptyChildren()
        return node
    }
    fun isEmptyNode()=value== NULL_NODE

    fun addChild(child: Node) {
        //initialize new children with empty children
        //so that we can avoid operlapping
        child.initializeEmptyChildren()
        for (i in 0 until children.size) {
            if (children[i].value == NULL_NODE) {
                children[i] = child
                return
            }
        }
        children.add(child)

    }
    private fun emptyNode() = Node(value = -1, sizePx = sizePx)

}
