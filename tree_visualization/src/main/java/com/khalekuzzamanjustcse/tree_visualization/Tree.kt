package com.khalekuzzamanjustcse.tree_visualization

import androidx.compose.ui.geometry.Offset
import com.khalekuzzamanjustcse.tree_visualization.laying_node.Node
import com.khalekuzzamanjustcse.tree_visualization.laying_node.ShanonTreeNodeCoordinateCalculator
import com.khalekuzzamanjustcse.tree_visualization.laying_node.TreeNode
import com.khalekuzzamanjustcse.tree_visualization.laying_node.TreeNodeCoordinateCalculator

class Tree<T>(
    private var treeRoot: TreeNode<T>
) {
    private lateinit var layoutAlgorithm: TreeNodeCoordinateCalculator<T>
    fun createTree(

    ): TreeNode<T> {
        layoutAlgorithm = ShanonTreeNodeCoordinateCalculator(treeRoot)
        treeRoot = layoutAlgorithm.generate()
        return treeRoot
    }

    fun rePosition(): TreeNode<T> {
        resetAllInfo()
        treeRoot = layoutAlgorithm.generate()
        return treeRoot
    }

    private fun resetAllInfo(node: TreeNode<T>? = treeRoot) {
        if (node == null) return
        resetAllInfo(node.children.firstOrNull())
        node.x = 0f
        node.y = 0f
        node.coordinates.value = Offset.Zero
        node.updateCoordinate()
        node.children.drop(1).forEach { child ->
            resetAllInfo(child)
        }
    }

    fun resetTreeColor(node: TreeNode<T>? = treeRoot) {
        if (node == null) return
        resetTreeColor(node.children.firstOrNull())
        node.resetColor()
        node.children.drop(1).forEach { child ->
            resetTreeColor(child)
        }
    }

    fun addChild(parent: TreeNode<T>, child: TreeNode<T>) {
        parent.addChild(child)
        rePosition()
    }

    fun getRoot() = treeRoot

}