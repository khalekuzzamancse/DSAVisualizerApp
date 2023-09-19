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
interface TreeNode<T> {
    val value: T
    val sizePx: Float
    val children: MutableList<TreeNode<T>>
    var x: Float
    var y: Float
    val coordinates: MutableState<Offset>
    val color: MutableState<Color>
    fun updateCoordinate()
    fun centerOffset(): Offset
    fun resetColor()
    fun addChild(child: TreeNode<T>)
}


data class Node(
    override val value: Int, // override the value property
    override val sizePx: Float, // override the sizePx property
    override var x: Float = 0f, // override the x property
    override var y: Float = 0f, // override the y property
    override val children: MutableList<TreeNode<Int>> = mutableListOf(), // override the children property
    override val coordinates: MutableState<Offset> = mutableStateOf(Offset.Zero), // override the coordinates property
    override val color: MutableState<Color> = mutableStateOf(Color.Red) // override the color property
) : TreeNode<Int> {
    companion object {
        const val NULL_NODE = -1

    }
    // val emptyNode=Node(value = NULL_NODE, sizePx = sizePx)

    override fun updateCoordinate() {
        coordinates.value = Offset(x * sizePx, y * sizePx)
    }

    override fun centerOffset() = coordinates.value + Offset(sizePx / 2, sizePx / 2)


    override fun resetColor() {
        color.value = Color.Red
    }


    override fun toString(): String {
        return "$value:${children.map { it.value }} ,x=$x,y=$y,offset=${coordinates.value}"

    }

    private fun initializeEmptyChildren() {
//        if (children.size == 0) {
//            children.add(emptyNode())
//            children.add(emptyNode())
//        } else if (children.size == 1) {
//            children.add(emptyNode())
//        }
    }



    override fun addChild(child: TreeNode<Int>) {
        //initialize new children with empty children
        //so that we can avoid operlapping
        //child.initializeEmptyChildren()
        for (i in 0 until children.size) {
            if (children[i].value == NULL_NODE) {
                children[i] = child
                return
            }
        }
        children.add(child)
    }


}
