package com.khalekuzzamanjustcse.tree_visualization

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

    private fun rePosition(): TreeNode<T> {
        treeRoot = layoutAlgorithm.generate()
        return treeRoot
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